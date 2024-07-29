package com.park.monitoring.controller;

import com.park.monitoring.dto.DetailDto;
import com.park.monitoring.dto.ServerInfoWithDiskDto;
import com.park.monitoring.model.Disk;
import com.park.monitoring.model.ServerInfo;
import com.park.monitoring.service.DiskService;
import com.park.monitoring.service.ServerInfoService;
import org.apache.catalina.Server;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @DisplayName("/접속 테스트")
    @Test
    void t01_accessRoot() throws Exception {
        List<ServerInfoWithDiskDto> ServerDtoList = Collections.emptyList();
        //serverInfoService.findAllServerInfo() 호출 발생시 반환값 설정
        given(serverInfoService.findServerInfoWithDisk()).willReturn(ServerDtoList);
        //http.get 요청
        mvc.perform(get("/"))
                //응답이 isOK(200)인지 확인
                .andExpect((status().isOk()))
                //모델에 "serverInfoList"라는 이름으로 serverInfoList가 포함되어 있는지 확인
                .andExpect(model().attribute("serverDtoList", ServerDtoList));
    }

    //    @DisplayName("/diskService.findAllDisks() 호출 시 예외 발생 테스트")
//    @Test
//    void t02_diskServiceException() throws Exception {
//        // 서비스에서 NoSuchElementException 예외를 던지도록 설정
//        given(serverInfoService.findServerInfoWithDisk()).willThrow(new NoSuchElementException("err"));
//
//        // 요청을 보내고 응답을 검증
//        mvc.perform(get("/"))
//                .andExpect(status().isInternalServerError()) // 상태 코드 검증
//                .andExpect((ResultMatcher) content().string(containsString("err"))); // 내용 검증
//    }
//
    @DisplayName("/detail/{serverId} 호출 테스트")
    @Test
    void t03_accessDetail() throws Exception {
        ServerInfoWithDiskDto serverDtoList = null;
        int serverId = 1;
        //serverInfoService.findAllServerInfo() 호출 발생시 반환값 설정
        given(serverInfoService.findServerInfoAtHistory(serverId)).willReturn(serverDtoList);
        //http.get 요청
        mvc.perform(post("/detail/{serverId}", serverId))
                .andExpect(status().isOk())
                .andExpect(model().attribute("serverDtoList", serverDtoList))
                //detailDto라는 이름의 속성이 있는지 확인
                .andExpect(model().attributeExists("serverDtoList"))
                //detaildto라는 속성에 각 필드가 있는지, get으로 가져온 데이터와 일치하는지 확인
                .andExpect(model().attribute("serverDtoList", hasProperty("serverId", is(serverDtoList.getServerId()))))
                .andExpect(model().attribute("serverDtoList", hasProperty("serverOs", is(serverDtoList.getServerOs()))))
                .andExpect(model().attribute("serverDtoList", hasProperty("serverHostname", is(serverDtoList.getServerHostname()))))
                .andExpect(model().attribute("serverDtoList", hasProperty("disks", is(serverDtoList.getDisks()))));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException e, Model model) {
        model.addAttribute("exception", e.getMessage());
        return "error";
    }
}
