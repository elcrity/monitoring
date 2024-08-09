package com.park.monitoring.controller;

import com.park.monitoring.util.ServerInfoUtil;
import com.sun.management.OperatingSystemMXBean;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional(readOnly = true)
@Sql({"classpath:sql/testTable.sql", "classpath:sql/testData.sql"})
public class ServerInfoControllerTest {

    @Autowired
    private MockMvc mvc;

    private MockedStatic<ServerInfoUtil> mockedStatic;

    @BeforeEach
    void setUp() {
//      osBeand을 이용한 데이터 수집 메소드.
        mockedStatic = Mockito.mockStatic(ServerInfoUtil.class);
        mockedStatic.when(ServerInfoUtil::getServerOs).thenReturn("Windows 10");
        mockedStatic.when(ServerInfoUtil::getServerHostname).thenReturn("test-hostname");
        given(ServerInfoUtil.getTotalMemory(any(OperatingSystemMXBean.class)))
                .willReturn(16L * 1024 * 1024 * 1024); // 8GB in KB
        mockedStatic.when(() -> ServerInfoUtil.getServerIp(anyString())).thenReturn("192.168.1.21");
    }

    @AfterEach
    void tearDown() {
        mockedStatic.close();
    }

    @DisplayName("/ 테스트 ")
    @Test
    void t01_accessRoot() throws Exception {
        //http.get 요청
        mvc.perform(get("/api/dashboard")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].serverId", is(1)))
                .andExpect(jsonPath("$[0].serverOs", is("Ubuntu 20.04")))
                .andExpect(jsonPath("$[0].serverHostname", is("server1")))
                .andExpect(jsonPath("$[0].memoryTotal", is(16384)))
                .andExpect(jsonPath("$[0].purpose", is("Web Server")))
                .andExpect(jsonPath("$[0].serverIp", is("192.168.1.1")));
    }

    @DisplayName("/ 테스트 - not Allowed method")
    @Test
    void t02_accessRoot_methodNotAllowed() throws Exception {
        //serverInfoService.findAllServerInfo() 호출 발생시 반환값 설정
//        given(serverInfoService.findAllServerInfo()).willReturn(ServerDtoList);
        //http.get 요청
        mvc.perform(post("/api/dashboard"))
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());
    }

    @DisplayName("/api/history/{serverId} 테스트")
    @Test
    void t04_accessDetail() throws Exception {
        mvc.perform(post("/api/history/{serverId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serverId", is(1)))
                .andExpect(jsonPath("$.serverOs", is("Ubuntu 20.04")))
                .andExpect(jsonPath("$.serverHostname", is("server1")))
                .andExpect(jsonPath("$.memoryTotal", is(16384)))
                .andExpect(jsonPath("$.purpose", is("Web Server")))
                .andExpect(jsonPath("$.serverIp", is("192.168.1.1")));
    }

    @DisplayName("/api/history/{serverId} methodNotAllowed 테스트")
    @Test
    void t05_history_methodNotAllowed() throws Exception {
        mvc.perform(get("/api/history/1"))
                .andExpect(status().isMethodNotAllowed());
    }

    @DisplayName("/api/history/{serverId} BAD_REQUEST 테스트")
    @Test
    void t06_history_BAD_REQUEST() throws Exception {
        mvc.perform(post("/api/history/"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/history/undefined"))
                .andExpect(status().isBadRequest());
    }
    @DisplayName("/api/history/{serverId} Invalid 테스트")
    @Test
    void t07_history_IllegalArgumentException() throws Exception {
        mvc.perform(post("/api/history/192"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("/api/history/{serverId} NoSuchElementException 테스트")
    @Test
    void t08_history_NoSuchElementException() throws Exception {
        mvc.perform(post("/api/history/" + 123))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @DisplayName("/regForm")
    @Test
    void t09_testToRegFrom() throws Exception {
        mvc.perform(get("/api/regform"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serverId").value(nullValue()))
                .andExpect(jsonPath("$.memoryTotal").value(nullValue()))
                .andExpect(jsonPath("$.serverHostname").value(nullValue()))
                .andExpect(jsonPath("$.serverIp").value(nullValue()))
                .andExpect(jsonPath("$.serverOs").value(nullValue()))
                .andExpect(jsonPath("$.purpose").value(nullValue()))
                .andDo(print());
    }

    @DisplayName("/regserver")
    @Test
    void t09_testAddServer_Success() throws Exception {
        String purpose = "test";
        mvc.perform(post("/api/regserver")
                        .param("purpose", purpose)
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{'message': '등록되었습니다'}"))
                .andDo(print());
    }

    @DisplayName("/regServer duplicate ip")
    @Test
    void t11_addServer_duplicate() throws Exception {
        String purpose = "test";
        mvc.perform(post("/api/regserver"))
                .andExpect(status().isOk());
        mvc.perform(post("/api/regserver")
                        .param("purpose", purpose))
                .andExpect(status().isInternalServerError()) // Expect 409 Conflict
                .andDo(print());
    }

    @DisplayName("/regform/{serverId}")
    @Test
    void t12_toRegPage() throws Exception {
        int serverId = 1;
        mvc.perform(post("/api/regform/{serverId}", serverId))
                .andExpect(status().isOk());
    }

    @DisplayName("/regform/{serverId}")
    @Test
    void t12_01_toRegPage_badRequest() throws Exception {
        mvc.perform(post("/api/regform/"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/regform/undefined"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("/regform/{serverId} invalid id")
    @Test
    void t12_02_toRegPage() throws Exception {


        mvc.perform(post("/regform/1233"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
//
    @DisplayName("/updateServer")
    @Test
    void t13_updateServer() throws Exception {
        int serverId = 1;
        String purpose = "New Purpose";
        String ip = "192.168.2.222";
        mvc.perform(put("/api/updateServer")
                        .param("serverId", String.valueOf(serverId))
                        .param("serverOs", "Linux")
                        .param("serverHostname", "test-hostname")
                        .param("memoryTotal", String.valueOf(8192L))
                        .param("purpose", purpose)
                        .param("serverIp", "192.168.2.222"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'message': '192.168.2.222 서버를 수정했습니다'}"))
                .andDo(print());  // Use serverId variable here
    }

    @DisplayName("/updateServer invalid Ip")
    @Test
    void t13_updateServer_invalid() throws Exception {
        int serverId = 1;
        // 예외 발생을 위한 설정
        // PUT 요청에 서버 정보 포함
        // @ModelAttribute를 사용하면 서버 정보까지 담아 보내야함
        mvc.perform(put("/api/updateServer")
                        .param("serverOs", "Linux")
                        .param("serverHostname", "test-hostname")
                        .param("memoryTotal", String.valueOf(8192L))
                        .param("purpose", "Test Server")
                        .param("serverId", String.valueOf(serverId)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("/updateServer NoSuch")
    @Test
    void t09_updateServer_noSuch() throws Exception {
        int serverId = 21;
        mvc.perform(put("/api/updateServer")
                        .param("serverOs", "Linux")
                        .param("serverHostname", "test-hostname")
                        .param("memoryTotal", String.valueOf(8192L))
                        .param("purpose", "Test Server")
                        .param("serverIp", "192.168.2.22")
                        .param("serverId", String.valueOf(serverId)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("/updateServer Duplicate")
    @Test
    void t09_updateServer_Dup() throws Exception {
        int serverId = 1;
        mvc.perform(put("/api/updateServer")
                        .param("serverOs", "Linux")
                        .param("serverHostname", "test-hostname")
                        .param("memoryTotal", String.valueOf(8192L))
                        .param("purpose", "Test Server")
                        .param("serverIp", "192.168.1.1")
                        .param("serverId", String.valueOf(serverId)))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }
    @DisplayName("/delete/{serverId}")
    @Test
    public void t10_deleteServer() throws Exception {
        Integer serverId = 1;

        mvc.perform(delete("/api/delete/{serverId}", serverId))
                .andExpect(status().isOk());
    }

    @DisplayName("/delete/{serverId} IllegalArgumentException")
    @Test
    public void t09_deleteServer_IllegalArgumentException() throws Exception {
        int serverId = 111;
        mvc.perform(delete("/api/delete/{serverId}", serverId))
                .andExpect(status().isNotFound()); // 리디렉션 상태 코드 검증// 플래시 메시지 검증
    }

    @DisplayName("/delete/{serverId} invalid id")
    @Test
    public void t10_deleteServer_invalid() throws Exception {
        int serverId = 111;
        mvc.perform(delete("/api/delete/"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
