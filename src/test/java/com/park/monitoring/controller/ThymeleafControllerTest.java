package com.park.monitoring.controller;

import com.park.monitoring.service.MetricLogService;
import com.park.monitoring.service.ServerInfoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional(readOnly = true)
@Sql({"classpath:sql/testTable.sql", "classpath:sql/testData.sql"})
//@WebMvcTest(ThymeleafController.class)
public class ThymeleafControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ServerInfoService serverInfoService;

    @Autowired
    private MetricLogService metricLogService;

    @DisplayName("/index")
    @Test
    void t01_index() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @DisplayName("/getServer")
    @Test
    void t02_getServer() throws Exception {

        mockMvc.perform(get("/getServer"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @DisplayName("/getLogs/{serverId}")
    @Test
    void t03_getLogs() throws Exception {

        mockMvc.perform(get("/getLogs/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("historyContainer")));
    }

    @DisplayName("/getLogs/ badRequest")
    @Test
    void t03_getLogs_badRequest() throws Exception {
        mockMvc.perform(get("/getLogs/1000"))
                .andExpect(view().name("errPage"));
    }

    @DisplayName("/getHistory")
    @Test
    void t04_getHistory() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        String formattedDate = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String requestBody = String.format("{\"serverId\": 1, \"isRepeat\": false, \"date\": \"%s\"}", formattedDate);

        mockMvc.perform(post("/getHistory")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @DisplayName("/getHistory")
    @Test
    void t04_getHistory_badRequest() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        String formattedDate = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String requestBody = String.format("{\"isRepeat\": false, \"date\": \"%s\"}", formattedDate);

        mockMvc.perform(post("/getHistory")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(view().name("errPage"));
    }
}