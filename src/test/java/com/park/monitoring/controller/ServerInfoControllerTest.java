package com.park.monitoring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.park.monitoring.dto.DashBoardDto;
import com.park.monitoring.model.Disk;
import com.park.monitoring.model.ServerInfo;
import com.park.monitoring.service.DiskService;
import com.park.monitoring.service.ServerInfoService;
import com.park.monitoring.util.ServerInfoUtil;
import com.sun.management.OperatingSystemMXBean;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
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

//    @DisplayName("/api/history/{serverId} BAD_REQUEST 테스트")
//    @Test
//    void t06_history_BAD_REQUEST() throws Exception {
//        mvc.perform(post("/api/history/" + 1.1))
//                .andExpect(status().isBadRequest());
//    }
//
//    @DisplayName("/api/history/{serverId} IllegalArgumentException 테스트")
//    @Test
//    void t07_history_IllegalArgumentException() throws Exception {
//        mvc.perform(post("/api/history/"))
//                .andExpect(status().isBadRequest());
//    }
//
//
//    @DisplayName("/api/history/{serverId} NoSuchElementException 테스트")
//    @Test
//    void t08_history_NoSuchElementException() throws Exception {
//        mvc.perform(post("/api/history/" + 123))
//                .andExpect(status().isNotFound())
//                .andDo(print());
//
//    }
//
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
                .andExpect(content().string("성공"))
                .andDo(print());
    }
//    @DisplayName("/regServer null테스트")
//    @Test
//    void t10_addServer_null() throws Exception {
//        String purpose = "test";
//        when(serverInfoService.addServerInfo(purpose))
//                .thenThrow(new IllegalArgumentException("필수 값이 null입니다."));
//
//        mvc.perform(post("/regserver")
//                        .param("purpose", purpose))
//                .andExpect(status().isBadRequest()) // Expect 409 Conflict
//                .andDo(print());
//
//        verify(serverInfoService, times(1)).addServerInfo(purpose);
//    }
//
//    @DisplayName("/regServer duplicate ip")
//    @Test
//    void t11_addServer_duplicate() throws Exception {
//        String purpose = "test";
//        when(serverInfoService.addServerInfo(purpose))
//                .thenThrow(new DataIntegrityViolationException("이미 존재하는 IP입니다."));
//
//        mvc.perform(post("/regserver")
//                        .param("purpose", purpose))
//                .andExpect(status().isConflict()) // Expect 409 Conflict
//                .andDo(print());
//
//        verify(serverInfoService, times(1)).addServerInfo(purpose);
//    }
//
    @DisplayName("/regform/{serverId}")
    @Test
    void t12_toRegPage() throws Exception {
        int serverId = 1;
        mvc.perform(post("/api/regform/{serverId}", serverId))
                .andExpect(status().isOk());
    }
//
//    @DisplayName("/editForm/{serverId}")
//    @Test
//    void t12_01_toRegPage_badRequest() throws Exception {
//        Integer serverId = 1;
//
//        when(serverInfoService.findServerInfoById(null)).thenThrow(new IllegalArgumentException("입력된 Id가 null입니다."));
//
//        mvc.perform(post("/regform/"))
//                .andExpect(status().isBadRequest());
//    }
//
//    @DisplayName("/regform/{serverId} invalid id")
//    @Test
//    void t12_02_toRegPage() throws Exception {
//        int serverId = 111;
//
//        when(serverInfoService.findServerInfoById(serverId)).thenThrow(new NoSuchElementException("service, 해당 ip로 가져온 데이터 없음. - "));
//
//        mvc.perform(post("/regform/{serverId}", serverId))
//                .andExpect(status().isNotFound())
//                .andDo(print());
//    }
//
    @DisplayName("/updateServer")
    @Test
    void t13_updateServer() throws Exception {
        int serverId = 1;
        String purpose = "New Purpose";

        mvc.perform(put("/api/updateServer")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("serverId", String.valueOf(serverId))
                        .param("serverOs", "Linux")
                        .param("serverHostname", "test-hostname")
                        .param("memoryTotal", String.valueOf(8192L))
                        .param("purpose", purpose)
                        .param("serverIp", "192.168.2.2"))
                .andExpect(status().isOk())
                .andExpect(content().string("성공"))
                .andDo(print());  // Use serverId variable here
    }
//
////    @DisplayName("/updateServer null")
////    @Test
////    void t13_updateServer_Illegal() throws Exception {
////        int serverId = 1;
//////        ServerInfo serverInfo = new ServerInfo.Builder()
//////                .serverId(serverId)
//////                .serverOs("Linux")
//////                .serverHostname("test-hostname")
//////                .memoryTotal(8192L)
//////                .purpose("Test Server")
//////                .serverIp(null)  // 이 경우 IllegalArgumentException을 던지도록 설계됨
//////                .build();
////
////
////        // 예외 발생을 위한 설정
//////        doThrow(new IllegalArgumentException("입력받은 값이 비정상입니다.")).when(serverInfoService).updateServerInfo(any(ServerInfo.class));
////        when(serverInfoService.updateServerInfo(any(ServerInfo.class))).thenThrow(new IllegalArgumentException("service, 해당 ip로 가져온 데이터 없음. - "));
////        // PUT 요청에 서버 정보 포함
////        // @ModelAttribute를 사용하면 서버 정보까지 담아 보내야함
////        mvc.perform(put("/updateServer")
////                        .param("serverOs", "Linux")
////                        .param("serverHostname", "test-hostname")
////                        .param("memoryTotal", String.valueOf(8192L))
////                        .param("purpose", "Test Server")
////                        .param("serverIp", null)
////                        .param("serverId", String.valueOf(serverId)))
////                .andExpect(status().isBadRequest())
////                .andDo(print());
////
////        verify(serverInfoService, times(1)).updateServerInfo(any(ServerInfo.class));
////    }
////
////    @DisplayName("/updateServer NoSuch")
////    @Test
////    void t09_updateServer_noSuch() throws Exception {
////        int id = 1;
////        ServerInfo serverInfo = new ServerInfo.Builder()
////                .serverId(id+111)
////                .serverOs("테스트Os")
////                .memoryTotal(11223L)
////                .purpose("테스트용")
////                .serverHostname("testServer")
////                .build();
////
////        // 예외 발생을 위한 설정
//////        doThrow(new IllegalArgumentException("입력받은 값이 비정상입니다.")).when(serverInfoService).updateServerInfo(any(ServerInfo.class));
////        when(serverInfoService.updateServerInfo(serverInfo)).thenThrow(new IllegalArgumentException("입력받은 값이 비정상입니다."));
////        // PUT 요청에 서버 정보 포함
////        // @ModelAttribute를 사용하면 서버 정보까지 담아 보내야함
////        mvc.perform(put("/updateServer")
////                        .param("serverOs", os)
////                        .param("serverHostname", hostname)
////                        .param("memoryTotal", String.valueOf(totalMemory))
////                        .param("purpose", purpose)
////                        .param("serverIp", serverIp))
////                .andExpect(status().isNotFound())
////                .andDo(print());
////
////        verify(serverInfoService, times(1)).updateServerInfo(any(ServerInfo.class));
////    }
//
    @DisplayName("/delete/{serverId}")
    @Test
    public void t10_deleteServer() throws Exception {
        Integer serverId = 1;

        mvc.perform(delete("/api/delete/{serverId}", serverId))
                .andExpect(status().isOk())
                .andExpect(content().string("삭제 성공"));
    }
//
//    @DisplayName("/delete/{serverId} IllegalArgumentException")
//    @Test
//    public void t09_deleteServer_IllegalArgumentException() throws Exception {
//
//        int serverId = 123;
//        when(serverInfoService.deleteServerInfo(serverId)).thenThrow(new NoSuchElementException("존재하지 않는 서버입니다."));
//
//        mvc.perform(delete("/delete/{serverId}", serverId))
//                .andExpect(status().isNotFound()); // 리디렉션 상태 코드 검증// 플래시 메시지 검증
//    }
//
//    @DisplayName("/delete/{serverId} Exception")
//    @Test
//    public void t10_deleteServer_Exception() throws Exception {
//        when(serverInfoService.deleteServerInfo(null)).thenThrow(new IllegalArgumentException("입력된 Id가 null입니다."));
//
//        mvc.perform(delete("/delete/"))
//                .andExpect(status().isBadRequest())
//                .andDo(print());
//    }
//
//    @DisplayName("/delete/{serverId} RuntimeException")
//    @Test
//    public void t11_deleteServer_RuntimeException() throws Exception {
//        when(serverInfoService.deleteServerInfo(null)).thenThrow(new RuntimeException("데이터 삭제 실패"));
//
//        mvc.perform(delete("/delete/"))
//                .andExpect(status().isInternalServerError())
//                .andDo(print());
//    }
}
