package com.park.monitoring.controller;

import com.park.monitoring.mapper.MetricLogMapper;
import com.park.monitoring.model.MetricLog;
import com.park.monitoring.service.DiskService;
import com.park.monitoring.service.MetricLogService;
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

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = MetricLogController.class)
public class MetricLogControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MetricLogService metricLogService;

    @DisplayName("/metric/{id} 정상 동작 테스트")
    @Test
    void t01_getMetricLog() throws Exception {
        Long serverId = 1L;
        List<MetricLog> metricLogList = Collections.emptyList();

        // metricLogService.getMetricLogAllByServerId 호출 발생시 반환값 설정
        given(metricLogService.getMetricLogAllByServerId(serverId)).willReturn(metricLogList);

        // POST 요청
        mvc.perform(post("/metric/{id}", serverId))
                // 응답이 isOK(200)인지 확인
                .andExpect(status().isOk())
                // JSON 응답 확인
                .andExpect(jsonPath("$.length()").value(metricLogList.size()));
    }

    @Test
    void t02_metricLogServiceException() throws Exception {
        Long serverId = 1L;

        // metricLogService.getMetricLogAllByServerId 호출 시 IllegalArgumentException 발생 설정
        given(metricLogService.getMetricLogAllByServerId(serverId)).willThrow(new IllegalArgumentException("잘못된 요청입니다."));

        // GET 요청
        mvc.perform(post("/metric/{id}",serverId))
                // 응답이 BAD_REQUEST(400)인지 확인
                .andExpect(status().isBadRequest())
                // 응답 본문에 예외 메시지가 포함되어 있는지 확인
                .andExpect(content().string("잘못된 요청입니다."));
    }

}
