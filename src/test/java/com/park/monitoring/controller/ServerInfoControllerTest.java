package com.park.monitoring.controller;

import com.park.monitoring.model.ServerInfo;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;

@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = ServerInfoController.class)
public class ServerInfoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ServerInfoService serverInfoService;
    @DisplayName("/접속 테스트")
    @Test
    void t01_accessRoot() throws Exception {
        List<ServerInfo> serverInfoList = Collections.emptyList();
        //serverInfoService.findAllServerInfo() 호출 발생시 반환값 설정
        given(serverInfoService.findAllServerInfo()).willReturn(serverInfoList);
        //http.get 요청
        mvc.perform(get("/"))
                //응답이 isOK(200)인지 확인
                .andExpect((status().isOk()))
                //모델에 "serverInfoList"라는 이름으로 serverInfoList가 포함되어 있는지 확인
                .andExpect(model().attribute("serverInfoList", serverInfoList));
    }
}
