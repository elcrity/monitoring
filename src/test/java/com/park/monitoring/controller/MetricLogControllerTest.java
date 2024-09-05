//package com.park.monitoring.controller;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.hamcrest.Matchers.is;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@TestMethodOrder(MethodOrderer.MethodName.class)
//@ActiveProfiles("test")
//@AutoConfigureMockMvc
//@SpringBootTest
//@Transactional(readOnly = true)
//@Sql({"classpath:sql/testTable.sql", "classpath:sql/testData.sql"})
//@DisplayName("로그 컨트롤러 테스트")
//public class MetricLogControllerTest {
//
//    @Autowired
//    private MockMvc mvc;
//
//
//    @DisplayName("/log")
//    @Test
//    void t01_01getMetricLog() throws Exception {
//        mvc.perform(post("/log"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].logId",is(4)))
//                .andDo(print());
//    }
//
//    @DisplayName("/log - method not Allowed")
//    @Test
//    void t01_02getDashboardLog_methodNotAllowed() throws Exception {
//        mvc.perform(get("/log"))
//                .andExpect(status().isMethodNotAllowed())
//                .andDo(print());
//    }
//
//    @DisplayName("/log/history/{serverId}")
//    @Test
//    void t02_01getServerLog_history() throws Exception {
//        String request = "2024-08-21";
//        mvc.perform(post("/log/history/{serverId}", 1)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(request))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//
//    @DisplayName("/log/history/ - null")
//    @Test
//    void t02_02getServerLog_badRequest() throws Exception {
//        mvc.perform(post("/log/history/"))
//                .andExpect(status().isBadRequest())
//                .andDo(print());
//    }
//
//}
