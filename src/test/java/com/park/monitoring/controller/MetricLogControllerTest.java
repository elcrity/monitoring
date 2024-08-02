package com.park.monitoring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.park.monitoring.model.MetricLog;
import com.park.monitoring.service.MetricLogService;
import com.park.monitoring.service.ServerInfoService;
import com.park.monitoring.util.ServerInfoUtil;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = MetricLogController.class)
public class MetricLogControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MetricLogService metricLogService;
    @MockBean
    private ServerInfoService serverInfoService;

    @Autowired
    private ObjectMapper objectMapper;
    private MockedStatic<ServerInfoUtil> mockedStatic;
    @BeforeEach
    void setUp() {
        mockedStatic = Mockito.mockStatic(ServerInfoUtil.class);
    }

    @AfterEach
    void tearDown() {
        mockedStatic.close();
    }

    @DisplayName("/log 정상 동작 테스트")
    @Test
    void t01_getMetricLog() throws Exception {
        List<MetricLog> metricLogList = new ArrayList<>();
        for(int i = 1; i<3; i++){
            MetricLog metricLog = new MetricLog.Builder()
                    .logId(i)
                    .serverMetricFk(i)
                    .cpuUsage(0.5+i)
                    .memoryUsage(0.2+i)
                    .createdDate(LocalDateTime.now())
                    .diskUsage1(0.3+i)
                    .diskUsage2(0.7+i)
                    .diskUsage3(0.4+i)
                    .diskUsage4(0.9+i)
                    .diskTotal1(100.0+i)
                    .diskTotal2(200.0+i)
                    .diskTotal3(3000.0+i)
                    .diskTotal4(4000.0+i)
                    .diskName1("disk1-"+i)
                    .diskName2("disk2-"+i)
                    .diskName3("disk3-"+i)
                    .diskName4("disk4-"+i)
                    .build();
            metricLogList.add(metricLog);
        }

        // Setup mock service to return sample data
        given(metricLogService.findMetricLogByLatest()).willReturn(metricLogList);

        //MetricLogList를 Json으로
        String expectedJson = objectMapper.writeValueAsString(metricLogList);

        // Perform POST request and verify the response
        mvc.perform(post("/log"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson)) // assuming the view name is "log"
                .andDo(print()); // Print the result to console for debugging
    }

    @DisplayName("/log - 비어 있는 경우")
    @Test
    void t02_getDashboardLog_empty() throws Exception {
        // 서비스가 빈 리스트를 반환하도록 설정
        when(metricLogService.findMetricLogByLatest()).thenReturn(Collections.emptyList());

        mvc.perform(post("/log"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @DisplayName("/log - null인 경우")
    @Test
    void t02_getDashboardLog_null() throws Exception {
        // 서비스가 빈 리스트를 반환하도록 설정
        when(metricLogService.findMetricLogByLatest()).thenThrow(new NoSuchElementException("로그가 존재하지 않습니다"));

        mvc.perform(post("/log"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("/log - method not Allowed")
    @Test
    void t03_getDashboardLog_method() throws Exception {
        // 서비스가 빈 리스트를 반환하도록 설정
        when(metricLogService.findMetricLogByLatest()).thenReturn(Collections.emptyList());

        mvc.perform(get("/log"))
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());


    }

    @DisplayName("/log/history/{serverId}")
    @Test
    void t04_getServerLog_history() throws Exception {
        int serverId = 1;
        MetricLog metricLog1 = new MetricLog.Builder()
                .logId(1)
                .serverMetricFk(1)
                .cpuUsage(75.5)
                .memoryUsage(50.2)
                .createdDate(LocalDateTime.parse("2024-07-01T00:00:00"))
                .diskName1("disk1")
                .diskUsage1(20.3)
                .diskUsage2(30.7)
                .diskUsage3(40.4)
                .diskUsage4(10.9)
                .diskTotal1(100.0)
                .diskTotal2(200.0)
                .diskTotal3(3007.0)
                .diskTotal4(4008.0)
                .build();
        List<MetricLog> metricLogList = Arrays.asList(metricLog1);

        given(metricLogService.findMetricLogAtHistory(serverId)).willReturn(metricLogList);

        mvc.perform(post("/log/history/{serverId}", serverId))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(metricLogList)
                ))
                .andDo(print());

        verify(metricLogService).findMetricLogAtHistory(serverId);
    }

    @DisplayName("/log/history/null")
    @Test
    void t05_getServerLog_history_null() throws Exception {
        // 서비스가 빈 리스트를 반환하도록 설정
        when(metricLogService.findMetricLogAtHistory(null))
                .thenThrow(new IllegalArgumentException("입력받은 서버의 id가 null입니다."));
        mvc.perform(post("/log/history"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("/log/history/nonExistingId")
    @Test
    void t06_getServerLog_nonExistentId() throws Exception {
        int nonExistentServerId = 999; // 존재하지 않는 ID

        when(metricLogService.findMetricLogAtHistory(nonExistentServerId))
                .thenThrow(new NoSuchElementException("없는 서버입니다"));
        mvc.perform(post("/log/history" + nonExistentServerId))
                .andExpect(status().isNotFound())
                .andDo(print()); // 예외가 발생하면 HTTP 상태 코드는 404가 되어야 함

    }

    @DisplayName("/log/start")
    @Test
    void t07_insertServerLog() throws Exception {
        String ip = "";
        when(ServerInfoUtil.getServerIp(ServerInfoUtil.getServerOs())).thenReturn(ip);


        when(metricLogService.insertMetricLog(ip)).thenReturn(1);

        mvc.perform(post("/log/start"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(metricLogService).insertMetricLog(ip);
    }
}
