package com.park.monitoring.controller;

import com.park.monitoring.model.ServerInfo;
import com.park.monitoring.service.DiskService;
import com.park.monitoring.service.ServerInfoService;
import com.park.monitoring.util.ServerInfoUtil;
import com.sun.management.OperatingSystemMXBean;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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
@WebMvcTest(controllers = ServerInfoController.class)
public class ServerInfoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ServerInfoService serverInfoService;
    @MockBean
    private DiskService diskService;
    @MockBean
    private OperatingSystemMXBean osBean;

    private MockedStatic<ServerInfoUtil> mockedStatic;
    @BeforeEach
    void setUp() {
        mockedStatic = Mockito.mockStatic(ServerInfoUtil.class);
    }

    @AfterEach
    void tearDown() {
        mockedStatic.close();
    }


    String purpose = "Test Server";
    String os = "Linux";
    String hostname = "test-hostname";
    long totalMemory = 8192L;
    String serverIp = "192.168.1.1";

    @DisplayName("/ 테스트 ")
    @Test
    void t01_accessRoot() throws Exception {
        List<ServerInfo> serverDtoList = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            ServerInfo serverInfo = new ServerInfo.Builder()
                    .serverOs("linux" + i)
                    .serverHostname("testHost" + i)
                    .purpose("테스트" + i)
                    .memoryTotal(8090L + i)
                    .serverIp("192.1.1." + i)
                    .build();
            serverDtoList.add(serverInfo);
        }

        //serverInfoService.findAllServerInfo() 호출 발생시 반환값 설정
        given(serverInfoService.findAllServerInfo()).willReturn(serverDtoList);
        //http.get 요청
        mvc.perform(get("/"))
                .andExpect((status().isOk()))
                .andExpect(model().attribute("serverDtoList", serverDtoList))
                .andExpect(model().attribute("serverDtoList", hasItem(
                        allOf(
                                hasProperty("serverOs", is("linux1")),
                                hasProperty("serverHostname", is("testHost1")),
                                hasProperty("purpose", is("테스트1")),
                                hasProperty("memoryTotal", is(8091L)),
                                hasProperty("serverIp", is("192.1.1.1"))
                        )
                )))
                .andExpect(view().name("dashboard"));
    }

    @DisplayName("/ 테스트 - not Allowed method")
    @Test
    void t02_accessRoot_methodNotAllowed() throws Exception {
        List<ServerInfo> ServerDtoList = Collections.emptyList();
        //serverInfoService.findAllServerInfo() 호출 발생시 반환값 설정
        given(serverInfoService.findAllServerInfo()).willReturn(ServerDtoList);
        //http.get 요청
        mvc.perform(post("/"))
                .andExpect(status().isMethodNotAllowed());
    }

    @DisplayName("/ 테스트 -noSuchElementException")
    @Test
    void t03_accessRoot_exception() throws Exception {
        when(serverInfoService.findAllServerInfo()).thenThrow(new NoSuchElementException("반환된 데이터가 없습니다."));
        mvc.perform(get("/"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("/history/{serverId} 테스트")
    @Test
    void t04_accessDetail() throws Exception {
        // Arrange
        int serverId = 0;

        ServerInfo serverDto = new ServerInfo.Builder()
                .serverOs("linux")
                .serverHostname("testHost")
                .purpose("테스트")
                .memoryTotal(8092L)
                .serverIp("192.1.1.1")
                .build();

        // Mock service response
        given(serverInfoService.findServerInfoAtHistory(serverId)).willReturn(serverDto);
        // Act and Assert
        mvc.perform(post("/history/{serverId}", serverId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("serverDto"))
                .andExpect(model().attribute("serverDto", hasProperty("serverId")))
                .andExpect(model().attribute("serverDto", hasProperty("serverOs", is("linux"))))
                .andExpect(model().attribute("serverDto", hasProperty("serverHostname", is("testHost"))))
                .andExpect(model().attribute("serverDto", hasProperty("purpose", is("테스트"))))
                .andExpect(model().attribute("serverDto", hasProperty("serverIp", is("192.1.1.1"))))
                .andExpect(model().attribute("serverDto", hasProperty("memoryTotal", is(8092L))))
                .andExpect(view().name("history"));
        ;
    }

    @DisplayName("/history/{serverId} methodNotAllowed 테스트")
    @Test
    void t05_history_methodNotAllowed() throws Exception {
        ServerInfo serverDto = new ServerInfo();
        when(serverInfoService.findServerInfoAtHistory(1)).thenReturn(serverDto);
        mvc.perform(get("/history/1"))
                .andExpect(status().isMethodNotAllowed());
    }

    @DisplayName("/history/{serverId} BAD_REQUEST 테스트")
    @Test
    void t06_history_BAD_REQUEST() throws Exception {
        double id = 1.1;
        when(serverInfoService.findServerInfoAtHistory(any(Integer.class))).thenThrow(new NumberFormatException("입력된 id값 이상 : "));
        mvc.perform(post("/history/" + id))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("/history/{serverId} IllegalArgumentException 테스트")
    @Test
    void t07_history_IllegalArgumentException() throws Exception {
        when(serverInfoService.findServerInfoAtHistory(null)).thenThrow(new IllegalArgumentException("입력된 id값 이상 : "));
        mvc.perform(post("/history/"))
                .andExpect(status().isBadRequest());
    }


    @DisplayName("/history/{serverId} NoSuchElementException 테스트")
    @Test
    void t08_history_NoSuchElementException() throws Exception {
        int id = 123;
        when(serverInfoService.findServerInfoAtHistory(id)).thenThrow(new NoSuchElementException("요청한 데이터를 찾을 수 없습니다."));
        mvc.perform(post("/history/" + id))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("/regForm")
    @Test
    void t09_testToRegFrom() throws Exception {
        mvc.perform(get("/regForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("serverDto"))
                .andExpect(model().attribute("serverDto", new ServerInfo()));
    }

    @DisplayName("/regServer")
    @Test
    void t09_testAddServer_Success() throws Exception {
        String purpose = "Test Purpose";
        String serverIp = "192.168.1.1";
        int serverId = 1;

        // Mocking service layer methods
        when(serverInfoService.addServerInfo(purpose)).thenReturn(serverIp);
        when(serverInfoService.findServerInfoByIp(serverIp)).thenReturn(new ServerInfo.Builder()
                .serverId(serverId)
                .serverOs("Ubuntu")
                .serverHostname("server")
                .memoryTotal(16000L)
                .purpose(purpose)
                .serverIp(serverIp)
                .build());

        when(diskService.insertDisk(serverId)).thenReturn(1); // Assuming the insert returns a success indicator

        mvc.perform(post("/regServer")
                        .param("purpose", purpose))
                .andExpect(status().is3xxRedirection()) // Expecting a redirect
                .andExpect(redirectedUrl("/"));
    }

    @DisplayName("/regServer null테스트")
    @Test
    void t10_addServer_null() throws Exception {
        String purpose = "test";

        when(ServerInfoUtil.getServerOs()).thenReturn(os);
        when(ServerInfoUtil.getServerHostname()).thenReturn(hostname);
        when(ServerInfoUtil.getTotalMemory(any(OperatingSystemMXBean.class))).thenReturn(totalMemory);
        when(ServerInfoUtil.getServerIp(os)).thenReturn(null);

        // Configure the service mock to throw DataIntegrityViolationException
        when(serverInfoService.addServerInfo(purpose))
                .thenThrow(new IllegalArgumentException("필수 값이 null입니다."));

        // Act and Assert
        mvc.perform(post("/regServer")
                        .param("purpose", purpose))
                .andExpect(status().isBadRequest()) // Expect 409 Conflict
                .andDo(print());

        // Verify that addServerInfo was called with any ServerInfo object
        verify(serverInfoService, times(1)).addServerInfo(purpose);

    }

    @DisplayName("/regServer duplicate ip")
    @Test
    void t11_addServer_duplicate() throws Exception {

        when(ServerInfoUtil.getServerOs()).thenReturn(os);
        when(ServerInfoUtil.getServerHostname()).thenReturn(hostname);
        when(ServerInfoUtil.getTotalMemory(any(OperatingSystemMXBean.class))).thenReturn(totalMemory);
        when(ServerInfoUtil.getServerIp(os)).thenReturn(serverIp);

        when(serverInfoService.addServerInfo(purpose))
                .thenThrow(new DataIntegrityViolationException("이미 존재하는 IP입니다."));

        // Act and Assert
        mvc.perform(post("/regServer")
                        .param("purpose", purpose))
                .andExpect(status().isConflict()) // Expect 409 Conflict
                .andDo(print());

        // Verify that addServerInfo was called with any ServerInfo object
        verify(serverInfoService, times(1)).addServerInfo(purpose);
    }

    @DisplayName("/editForm/{serverId}")
    @Test
    void t12_toRegPage() throws Exception {
        int serverId = 1;
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverId(serverId)
                .serverOs("Ubuntu")
                .serverHostname("server1")
                .memoryTotal(16384L)
                .purpose("Web Server")
                .serverIp("192.168.1.1")
                .build();

        when(serverInfoService.findServerInfoById(serverId)).thenReturn(serverInfo);

        mvc.perform(post("/editForm/{serverId}",serverId))
                .andExpect(status().isOk())
                .andExpect(model().attribute("serverDto",serverInfo))
                .andExpect(view().name("register"));
    }

    @DisplayName("/editForm/{serverId}")
    @Test
    void t12_01_toRegPage_badRequest() throws Exception {
        Integer serverId = 1;
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverId(serverId)
                .serverOs("Ubuntu")
                .serverHostname("server1")
                .memoryTotal(19222L)
                .purpose("Web Server")
                .serverIp("192.168.1.1")
                .build();

        when(serverInfoService.findServerInfoById(null)).thenThrow(new IllegalArgumentException("입력된 Id가 null입니다."));

        mvc.perform(post("/editForm/"))
                .andExpect(status().isBadRequest());
    }
    @DisplayName("/editForm/{serverId}")
    @Test
    void t12_02_toRegPage() throws Exception {
        int serverId = 111;

        when(serverInfoService.findServerInfoById(serverId)).thenThrow(new NoSuchElementException("service, 해당 ip로 가져온 데이터 없음. - "));

        mvc.perform(post("/editForm/{serverId}",serverId))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("/updateServer")
    @Test
    void t13_updateServer() throws Exception {
        int serverId = 1;
        String purpose = "New Purpose";

        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverOs("Linux")
                .serverHostname("host1")
                .memoryTotal(1024L)
                .purpose(purpose)
                .serverIp("192.168.1.1")
                .build();
        serverInfo.setServerId(serverId);
        // Mocking the service method to return the serverId
        when(serverInfoService.updateServerInfo(serverInfo)).thenReturn(1);

        mvc.perform(put("/updateServer")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("serverId", String.valueOf(serverInfo.getServerId()))
                        .param("serverOs", serverInfo.getServerOs())
                        .param("serverHostname", serverInfo.getServerHostname())
                        .param("memoryTotal", String.valueOf(serverInfo.getMemoryTotal()))
                        .param("purpose", serverInfo.getPurpose())
                        .param("serverIp", serverInfo.getServerIp()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/history/" + serverId));  // Use serverId variable here

        verify(serverInfoService, times(1)).updateServerInfo(any(ServerInfo.class));
    }

    @DisplayName("/updateServer")
    @Test
    void t13_updateServer_Illegal() throws Exception {
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverOs("Linux")
                .serverHostname("host1")
                .memoryTotal(1024L)
                .purpose(purpose)
                .serverIp(null)  // 이 경우 IllegalArgumentException을 던지도록 설계됨
                .build();

        // 예외 발생을 위한 설정
        doThrow(new IllegalArgumentException("입력받은 값이 비정상입니다.")).when(serverInfoService).updateServerInfo(any(ServerInfo.class));

        // PUT 요청에 서버 정보 포함
        // @ModelAttribute를 사용하면 서버 정보까지 담아 보내야함
        mvc.perform(put("/updateServer")
                        .param("serverOs", serverInfo.getServerOs())
                        .param("serverHostname", serverInfo.getServerHostname())
                        .param("memoryTotal", serverInfo.getMemoryTotal().toString())
                        .param("purpose", serverInfo.getPurpose())
                        .param("serverIp", serverInfo.getServerIp()))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(serverInfoService, times(1)).updateServerInfo(any(ServerInfo.class));
    }


    @DisplayName("/delete/{serverId}")
    @Test
    public void t10_deleteServer() throws Exception {
        Integer serverId = 1;

        mvc.perform(delete("/delete/{serverId}", serverId))
                .andExpect(status().is3xxRedirection()) // 리디렉션 상태 코드
                .andExpect(redirectedUrl("/")) // 리디렉션된 URL 검증
                .andExpect(flash().attribute("message", "서버 삭제 성공.")); // 플래시 메시지 검증
    }

    @DisplayName("/delete/{serverId} IllegalArgumentException")
    @Test
    public void t09_deleteServer_IllegalArgumentException() throws Exception {

        int serverId = 123;
        when(serverInfoService.deleteServerInfo(serverId)).thenThrow(new NoSuchElementException("존재하지 않는 서버입니다."));

        mvc.perform(delete("/delete/{serverId}", serverId))
                .andExpect(status().isNotFound()); // 리디렉션 상태 코드 검증// 플래시 메시지 검증
    }

    @DisplayName("/delete/{serverId} Exception")
    @Test
    public void t10_deleteServer_Exception() throws Exception {
        when(serverInfoService.deleteServerInfo(null)).thenThrow(new IllegalArgumentException("입력된 Id가 null입니다."));

        mvc.perform(delete("/delete/"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("/delete/{serverId} RuntimeException")
    @Test
    public void t11_deleteServer_RuntimeException() throws Exception {
        when(serverInfoService.deleteServerInfo(null)).thenThrow(new RuntimeException("데이터 삭제 실패"));

        mvc.perform(delete("/delete/"))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }
}
