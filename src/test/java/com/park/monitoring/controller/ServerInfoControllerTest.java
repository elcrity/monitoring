package com.park.monitoring.controller;

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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional(readOnly = true)
@Sql({"classpath:sql/testTable.sql", "classpath:sql/testData.sql"})
@DisplayName("서버 컨트롤러 테스트")
public class ServerInfoControllerTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("/api/dashboard ")
    @Test
    void t01_01accessRoot() throws Exception {
        mvc.perform(get("/api/dashboard"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].serverIp", is("192.168.1.1")));
    }

    @DisplayName("/api/dashboard - not Allowed method")
    @Test
    void t01_02accessRoot_methodNotAllowed() throws Exception {
        mvc.perform(post("/api/dashboard"))
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());
    }

    @DisplayName("/api/history/{serverId}")
    @Test
    void t02_01history() throws Exception {
        mvc.perform(post("/api/history/{serverId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serverIp", is("192.168.1.1")));
    }

    @DisplayName("/api/history/{serverId} methodNotAllowed")
    @Test
    void t02_02history_methodNotAllowed() throws Exception {
        mvc.perform(get("/api/history/1"))
                .andExpect(status().isMethodNotAllowed());
    }

    @DisplayName("/api/history/{serverId} null")
    @Test
    void t02_03history_BAD_REQUEST1() throws Exception {
        mvc.perform(post("/api/history/"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("/api/history/{serverId} undefined")
    @Test
    void t02_04history_BAD_REQUEST2() throws Exception {
        mvc.perform(post("/api/history/undefined"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("/api/history/{serverId} Invalid")
    @Test
    void t02_05history_NotFoundException() throws Exception {
        mvc.perform(post("/api/history/192"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("/regserver")
    @Test
    void t03_01regServer_Success() throws Exception {
        String requestBody = """
                    {
                        "serverOs": "windows 11",
                        "serverHostname": "DESKTOP-61V7M8K",
                        "purpose": "test",
                        "serverIp": "192.168.2.1"
                    }
                """;
        mvc.perform(post("/api/regserver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json("{'message': '등록되었습니다'}"))
                .andDo(print());
    }

    @DisplayName("/regServer duplicatedIp")
    @Test
    void t03_02addServer_duplicate() throws Exception {
        String requestBody = """
                    {
                        "serverOs": "windows 11",
                        "serverHostname": "DESKTOP-61V7M8K",
                        "memoryTotal": 16440,
                        "purpose": "test",
                        "serverIp": "192.168.1.1"
                    }
                """;

        mvc.perform(post("/api/regserver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @DisplayName("/updateServer")
    @Test
    void t04_01updateServer() throws Exception {
        String requestBody = """
                    {
                        "serverId": 1,
                        "serverOs": "Linux",
                        "serverHostname": "test-hostname",
                        "memoryTotal": 8192,
                        "purpose": "test",
                        "serverIp": "192.168.2.222"
                    }
                """;
        mvc.perform(put("/api/server")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json("{'message': '192.168.2.222 서버가 수정됐습니다'}"))
                .andDo(print());
    }

    @DisplayName("/updateServer invalid Ip")
    @Test
    void t04_02updateServer_invalid() throws Exception {
        //ip null
        String requestBody = """
                    {
                        "serverId": 1,
                        "serverOs": "Linux",
                        "serverHostname": "test-hostname",
                        "memoryTotal": 8192,
                        "purpose": "test",
                        "serverIp": 
                    }
                """;
        mvc.perform(put("/api/server")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("/updateServer invalid")
    @Test
    void t04_03updateServer_noSuch() throws Exception {
        //id not exist
        String requestBody = """
                    {
                        "serverId": -1,
                        "serverOs": "Linux",
                        "serverHostname": "test-hostname",
                        "memoryTotal": 8192,
                        "purpose": "test",
                        "serverIp": "192.168.2.222"
                    }
                """;

        mvc.perform(put("/api/server")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("/updateServer duplicatedIp")
    @Test
    void t04_04updateServer_Dup() throws Exception {
        String requestBody = """
                    {
                        "serverId": 1,
                        "serverOs": "Linux",
                        "serverHostname": "test-hostname",
                        "memoryTotal": 8192,
                        "purpose": "test",
                        "serverIp": "192.168.1.2"
                    }
                """;

        mvc.perform(put("/api/server")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @DisplayName("/delete/{serverId}")
    @Test
    void t05_01deleteServer() throws Exception {
        Integer serverId = 1;

        mvc.perform(delete("/api/delete/{serverId}", serverId))
                .andExpect(status().isOk());
    }

    @DisplayName("/delete/{serverId} invalid id")
    @Test
    void t05_02deleteServer_badRequestException() throws Exception {
        mvc.perform(delete("/api/delete/{serverId}", -1))
                .andExpect(status().isNotFound());
    }

    @DisplayName("/delete/{serverId} null id")
    @Test
    void t05_01deleteServer_invalid() throws Exception {
        mvc.perform(delete("/api/delete/"))
                .andExpect(status().isBadRequest());
    }
}
