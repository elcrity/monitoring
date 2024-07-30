package com.park.monitoring.controller;

import com.park.monitoring.dto.ServerInfoWithDiskDto;
import com.park.monitoring.service.DiskService;
import com.park.monitoring.service.ServerInfoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @DisplayName("/ 테스트 ")
    @Test
    void t01_accessRoot() throws Exception {
        List<ServerInfoWithDiskDto> ServerDtoList = Collections.emptyList();
        //serverInfoService.findAllServerInfo() 호출 발생시 반환값 설정
        given(serverInfoService.findServerInfoWithDisk()).willReturn(ServerDtoList);
        //http.get 요청
        mvc.perform(get("/"))
                .andExpect((status().isOk()))
                .andExpect(model().attribute("serverDtoList", ServerDtoList));
    }

    @DisplayName("/ 테스트 - not Allowed method")
    @Test
    void t02_accessRoot_methodNotAllowed() throws Exception {
        List<ServerInfoWithDiskDto> ServerDtoList = Collections.emptyList();
        //serverInfoService.findAllServerInfo() 호출 발생시 반환값 설정
        given(serverInfoService.findServerInfoWithDisk()).willReturn(ServerDtoList);
        //http.get 요청
        mvc.perform(post("/"))
                .andExpect(status().isMethodNotAllowed());
    }

    @DisplayName("/ 테스트 -noSuchElementException")
    @Test
    void t03_accessRoot_exception() throws Exception {
        when(serverInfoService.findServerInfoWithDisk()).thenThrow(new NoSuchElementException("해당되는 데이터가 없습니다 - "));
        mvc.perform(get("/"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("/history/{serverId} 테스트")
    @Test
    void t04_accessDetail() throws Exception {
        // Arrange
        int serverId = 1;

        ServerInfoWithDiskDto serverDtoList = new ServerInfoWithDiskDto();

        // Mock service response
        given(serverInfoService.findServerInfoAtHistory(serverId)).willReturn(serverDtoList);
        // Act and Assert
        mvc.perform(post("/history/{serverId}", serverId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("serverDtoList"))
                .andExpect(model().attribute("serverDtoList", hasProperty("serverId")))
                .andExpect(model().attribute("serverDtoList", hasProperty("serverOs")))
                .andExpect(model().attribute("serverDtoList", hasProperty("serverHostname")))
                .andExpect(model().attribute("serverDtoList", hasProperty("purpose")))
                .andExpect(model().attribute("serverDtoList", hasProperty("serverIp")))
                .andExpect(model().attribute("serverDtoList", hasProperty("disk")));
//                .andExpect(model().attribute("serverDtoList", hasProperty("disks", hasItem(allOf(hasProperty("disk_name", is(not(emptyOrNullString()))))))));
    }

    @DisplayName("/history/{serverId} methodNotAllowed 테스트")
    @Test
    void t05_history_methodNotAllowed() throws Exception {
        ServerInfoWithDiskDto serverDtoList = new ServerInfoWithDiskDto();
        when(serverInfoService.findServerInfoAtHistory(1)).thenReturn(serverDtoList);
        mvc.perform(get("/history/1"))
                .andExpect(status().isMethodNotAllowed());
    }
    @DisplayName("/history/{serverId} badRequest 테스트")
    @Test
    void t06_history_badRequest() throws Exception {
        when(serverInfoService.findServerInfoAtHistory(null)).thenThrow(new IllegalArgumentException("입력된 id값 이상 : "));
        mvc.perform(post("/history/null"))
                .andExpect(status().isBadRequest());
    }
    @DisplayName("/history/{serverId} notFound 테스트")
    @Test
    void t07_history_notFound() throws Exception {
        int id = 123;
        when(serverInfoService.findServerInfoAtHistory(id)).thenThrow(new NoSuchElementException("요청한 데이터를 찾을 수 없습니다."));
        mvc.perform(post("/history/",id))
                .andExpect(status().isNotFound());
    }

    @DisplayName("/delete/{serverId}")
    @Test
    public void t08_deleteServer() throws Exception {
        // given
        Integer serverId = 1;

        mvc.perform(delete("/delete/{serverId}", serverId))
                .andExpect(status().is3xxRedirection()) // 리디렉션 상태 코드 검증
                .andExpect(redirectedUrl("/dashboard")) // 리디렉션된 URL 검증
                .andExpect(flash().attribute("message", "서버 삭제 성공.")); // 플래시 메시지 검증
    }

    @DisplayName("/delete/{serverId} NoSuchElementException")
    @Test
    public void t09_deleteServer_NoSuchElementException() throws Exception {

        int serverId = 123;
        when(serverInfoService.deleteServerInfo(serverId)).thenThrow(new NoSuchElementException("해당하는 서버 없음."));

        mvc.perform(delete("/delete/{serverId}", serverId))
                .andExpect(status().is3xxRedirection()) // 리디렉션 상태 코드 검증
                .andExpect(redirectedUrl("/error")) // 리디렉션된 URL 검증
                .andExpect(flash().attribute("error", "해당하는 서버 없음.")); // 플래시 메시지 검증
    }

//Todo:null일때 검증하기
    @DisplayName("/delete/{serverId} IllegalArgumentException")
    @Test
    public void t10_deleteServer_IllegalArgumentException() throws Exception {

        Integer serverId = null;
        when(serverInfoService.deleteServerInfo(serverId)).thenThrow(new IllegalArgumentException("유효하지 않은 서버 아이디."));

        mvc.perform(delete("/delete/{serverId}", serverId))
                .andExpect(status().is3xxRedirection()) // 리디렉션 상태 코드 검증
                .andExpect(redirectedUrl("/error")) // 리디렉션된 URL 검증
                .andExpect(flash().attribute("error", "해당하는 서버 없음.")); // 플래시 메시지 검증
    }

    @DisplayName("/delete/{serverId} Exception")
    @Test
    public void t10_deleteServer_Exception() throws Exception {

        Integer serverId = 1;
        when(serverInfoService.deleteServerInfo(serverId)).thenThrow(new RuntimeException("예상하지 못한 에러 발생."));

        mvc.perform(delete("/delete/{serverId}", serverId))
                .andExpect(status().is3xxRedirection()) // 리디렉션 상태 코드 검증
                .andExpect(redirectedUrl("/error")) // 리디렉션된 URL 검증
                .andExpect(flash().attribute("error", "예상하지 못한 에러 발생.")); // 플래시 메시지 검증
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException e, Model model) {
        model.addAttribute("exception", e.getMessage());
        return "error";
    }
}
