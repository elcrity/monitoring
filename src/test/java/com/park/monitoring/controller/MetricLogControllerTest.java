package com.park.monitoring.controller;

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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional(readOnly = true)
@Sql({"classpath:sql/testTable.sql", "classpath:sql/testData.sql"})
@DisplayName("로그 컨트롤러 테스트")
public class MetricLogControllerTest {

    @Autowired
    private MockMvc mvc;


    @DisplayName("/log 정상 동작 테스트")
    @Test
    void t01_getMetricLog() throws Exception {
        mvc.perform(post("/log"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].logId",is(4)))// assuming the view name is "log"
                .andDo(print()); // Print the result to console for debugging
    }

    @DisplayName("/log - method not Allowed")
    @Test
    void t01_e01_getDashboardLog_method() throws Exception {
        // 서비스가 빈 리스트를 반환하도록 설정

        mvc.perform(get("/log"))
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());
    }

    @DisplayName("/log/history/{serverId}")
    @Test
    void t02_getServerLog_history() throws Exception {
        int serverId = 1;
        mvc.perform(post("/log/history/{serverId}", serverId))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("/log/history/null")
    @Test
    void t02_e01_getServerLog_badRequest() throws Exception {
        // 서비스가 빈 리스트를 반환하도록 설정
        mvc.perform(post("/log/history/"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("/log/history/invalid")
    @Test
    void t02_e02_getServerLog_nonExistentId() throws Exception {
        int nonExistentServerId = 999; // 존재하지 않는 ID

        mvc.perform(post("/log/history/{serverId}", nonExistentServerId))
                .andExpect(status().isNotFound()) // 상태 코드가 NOT FOUND인지 확인
                .andDo(print());

    }
}
