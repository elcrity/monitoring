package com.park.monitoring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.park.monitoring.model.MetricLog;
import com.park.monitoring.service.MetricLogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
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

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("/log 정상 동작 테스트")
    @Test
    void t01_getMetricLog() throws Exception {
        MetricLog metricLog1 = new MetricLog.Builder()
                .serverMetricFk(1)
                .cpuUsage(75.5)
                .memoryUsage(50.2)
                .createdDate(LocalDateTime.parse("2024-07-01T00:00:00"))
                .diskUsage1(20.3)
                .diskUsage2(30.7)
                .diskUsage3(40.4)
                .diskUsage4(10.9)
                .diskTotal1(100L)
                .diskTotal2(200L)
                .diskTotal3(3007L)
                .diskTotal4(4008L)
                .build();

        MetricLog metricLog2 = new MetricLog.Builder()
                .serverMetricFk(2)
                .cpuUsage(80.8)
                .memoryUsage(55.6)
                .createdDate(LocalDateTime.parse("2024-07-02T00:00:00"))
                .diskUsage1(25.4)
                .diskUsage2(35.3)
                .diskUsage3(45.1)
                .diskUsage4(11.8)
                .diskTotal1(110L)
                .diskTotal2(210L)
                .diskTotal3(3103L)
                .diskTotal4(4104L)
                .build();
        List<MetricLog> metricLogList = Arrays.asList(metricLog1, metricLog2);

        // Setup mock service to return sample data
        given(metricLogService.getMetricLogByLatest()).willReturn(metricLogList);

        // Perform POST request and verify the response
        mvc.perform(MockMvcRequestBuilders.post("/log"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(metricLogList)
                ))
                .andDo(MockMvcResultHandlers.print()); // Print the result to console for debugging
    }

    @DisplayName("/log - 비어 있는 경우")
    @Test
    void t02_getDashboardLog_empty() throws Exception {
        // 서비스가 빈 리스트를 반환하도록 설정
        when(metricLogService.getMetricLogByLatest()).thenReturn(Collections.emptyList());

        mvc.perform(MockMvcRequestBuilders.post("/log"))
                .andExpect(status().isNoContent());
    }

    @DisplayName("/log - method not Allowed")
    @Test
    void t03_getDashboardLog_method() throws Exception {
        // 서비스가 빈 리스트를 반환하도록 설정
        when(metricLogService.getMetricLogByLatest()).thenReturn(Collections.emptyList());

        mvc.perform(MockMvcRequestBuilders.get("/log"))
                .andExpect(status().isMethodNotAllowed());
    }

    @DisplayName("/log/{serverId}")
    @Test
    void t04_getServerLog_history() throws Exception {
        int serverId = 1;
        MetricLog metricLog1 = new MetricLog.Builder()
                .serverMetricFk(1)
                .cpuUsage(75.5)
                .memoryUsage(50.2)
                .createdDate(LocalDateTime.parse("2024-07-01T00:00:00"))
                .diskUsage1(20.3)
                .diskUsage2(30.7)
                .diskUsage3(40.4)
                .diskUsage4(10.9)
                .diskTotal1(100L)
                .diskTotal2(200L)
                .diskTotal3(3007L)
                .diskTotal4(4008L)
                .build();
        List<MetricLog> metricLogList = Arrays.asList(metricLog1);

        // Setup mock service to return sample data
        given(metricLogService.getMetricLogAtHistory(serverId)).willReturn(metricLogList);

        // Perform POST request and verify the response
        mvc.perform(MockMvcRequestBuilders.post("/log/{serverId}", serverId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(metricLogList)
                ))
                .andDo(MockMvcResultHandlers.print()); // Print the result to console for debugging
    }

    @DisplayName("/log/null")
    @Test
    void t05_getServerLog_history_null() throws Exception {
        // 서비스가 빈 리스트를 반환하도록 설정
        when(metricLogService.getMetricLogByLatest()).thenThrow(new IllegalArgumentException("입력받은 id 값을 확인해주세요"));

        mvc.perform(MockMvcRequestBuilders.post("/log/null"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("/log/nonExistingId")
    @Test
    void t06_getServerLog_nonExistentId() throws Exception {
        int nonExistentServerId = 999; // 존재하지 않는 ID

        when(metricLogService.getMetricLogAtHistory(nonExistentServerId))
                .thenThrow(new NoSuchElementException("없는 서버입니다"));
        mvc.perform(post("/log/" + nonExistentServerId))
                .andExpect(status().isNotFound()); // 예외가 발생하면 HTTP 상태 코드는 404가 되어야 함

    }
}
