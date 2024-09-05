package com.park.monitoring.controller;

import com.park.monitoring.service.MetricLogService;
import com.park.monitoring.service.ServerInfoService;
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

    @Test
    void testIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void testServer() throws Exception {
        // Prepare test data if needed, or ensure that your application context is pre-loaded with necessary data

        mockMvc.perform(get("/getServer"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
        // Add more assertions as needed to verify content
    }

    @Test
    void testGetLog() throws Exception {
        // Ensure there is a server with ID 1 and corresponding logs in the data source

        mockMvc.perform(get("/getLogs/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("historyContainer")));
        // Add more assertions as needed to verify content
    }

    @Test
    void testGetHistory() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        String formattedDate = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String requestBody = String.format("{\"serverId\": 1, \"isRepeat\": false, \"date\": \"%s\"}", formattedDate);

        mockMvc.perform(post("/getHistory")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk()); // Adjust based on actual expected JSON response
    }
}