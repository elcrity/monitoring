package com.park.monitoring.controller;

import com.park.monitoring.model.Disk;
import com.park.monitoring.model.ServerInfo;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
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
    @DisplayName("/접속 테스트")
    @Test
    void t01_accessRoot() throws Exception {
        List<ServerInfo> serverInfoList = Collections.emptyList();
        List<Disk> diskList = Collections.emptyList();
        //serverInfoService.findAllServerInfo() 호출 발생시 반환값 설정
        given(serverInfoService.findAllServerInfo()).willReturn(serverInfoList);
        given(diskService.findAllDisks()).willReturn(diskList);
        //http.get 요청
        mvc.perform(get("/"))
                //응답이 isOK(200)인지 확인
                .andExpect((status().isOk()))
                //모델에 "serverInfoList"라는 이름으로 serverInfoList가 포함되어 있는지 확인
                .andExpect(model().attribute("serverInfoList", serverInfoList))
                .andExpect(model().attribute("diskList", diskList));
    }

    @DisplayName("/diskService.findAllDisks() 호출 시 예외 발생 테스트")
    @Test
    void t02_diskServiceException() throws Exception {
        List<ServerInfo> serverInfoList = Collections.emptyList();

        // serverInfoService.findAllServerInfo() 호출 발생 시 반환값 설정
        given(serverInfoService.findAllServerInfo()).willReturn(serverInfoList);
        // diskService.findAllDisks() 호출 시 예외 발생 설정
        given(diskService.findAllDisks()).willThrow(new IllegalArgumentException("디스크 데이터를 찾을 수 없습니다."));

        // http.get 요청
        mvc.perform(get("/"))
                // 응답이 isBadRequest(400)인지 확인
                .andExpect(status().isBadRequest())
                // 응답 본문에 예외 메시지가 포함되어 있는지 확인
                .andExpect(model().attribute("exception", "디스크 데이터를 찾을 수 없습니다."));
    }
}
