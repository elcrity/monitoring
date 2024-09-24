package com.park.monitoring.controller;

import com.park.monitoring.config.error.ErrorCode;
import com.park.monitoring.service.MetricLogService;
import com.park.monitoring.service.ServerInfoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
                .andExpect(view().name("index :: #serverTableBody"));
    }

    @DisplayName("/getLogs/{serverId}")
    @Test
    void t03_getLogs() throws Exception {

        mockMvc.perform(get("/getLogs/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("historyContainer")));
    }

    @DisplayName("/getLogs/ notFound")
    @Test
    void t03_getLogs_badRequest() throws Exception {
        mockMvc.perform(get("/getLogs/1000"))
                .andExpect(status().isNotFound()) // HTTP 상태 코드를 404으로 검증
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // JSON 응답을 검증
                .andExpect(jsonPath("$.status").value(ErrorCode.NOT_FOUND.getStatus().value())) // status 값 검증
                .andExpect(jsonPath("$.name").value(ErrorCode.NOT_FOUND.getStatus().name())) // name 값 검증
                .andExpect(jsonPath("$.message").value(ErrorCode.NOT_FOUND.getMessage())) // message 값 검증
                .andExpect(jsonPath("$.code").value(ErrorCode.NOT_FOUND.getCode())); // 오류 페이지로 리다이렉트 검증
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
                .andExpect(status().isBadRequest()) // HTTP 상태 코드를 404으로 검증
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // JSON 응답을 검증
                .andExpect(jsonPath("$.status").value(ErrorCode.INVALID_INPUT_VALUE.getStatus().value())) // status 값 검증
                .andExpect(jsonPath("$.name").value(ErrorCode.INVALID_INPUT_VALUE.getStatus().name())) // name 값 검증
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage())) // message 값 검증
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode())); // 오류 페이지로 리다이렉트 검증
    }
}