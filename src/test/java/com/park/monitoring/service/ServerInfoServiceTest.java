package com.park.monitoring.service;

import com.park.monitoring.mapper.ServerInfoMapper;
import com.park.monitoring.model.ServerInfo;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.NoSuchElementException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@Transactional(readOnly = true)
@ActiveProfiles("test")
@Sql({"classpath:sql/testTable.sql", "classpath:sql/testData.sql"})
public class ServerInfoServiceTest {
    Logger log = LoggerFactory.getLogger(ServerInfoServiceTest.class);

    @Autowired
    ServerInfoMapper serverInfoMapper;

    ServerInfoService serverInfoService;

    @BeforeEach
    void init() {
        serverInfoService = new ServerInfoService(serverInfoMapper);
    }

    @DisplayName("서버 데이터 조회")
    @Test
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t01_testFindAll() {
        List<ServerInfo> serverInfo = serverInfoService.findAllServerInfo();
        assertEquals(10, serverInfo.size());
        //없을때
    }

    @DisplayName("서버 데이터 조회 - id")
    @Test
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t02_testFindById() {
        ServerInfo serverInfo = serverInfoService.findServerInfoById(1L);
        assertEquals("Linux", serverInfo.getServerOs());

        // 예외 테스트

        //없는 id
        //
    }
    @DisplayName("서버 데이터 조회 - 미존재 id")
    @Test
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t03_testFindById_noElement() {
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(()->serverInfoService.findServerInfoById(-1L));
    }

    @DisplayName("서버 데이터 등록")
    @Test
    @Transactional
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t04_testAddServer() {
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverOs("window")
                .serverHostname("test")
                .memoryTotal(8012)
                .purpose("서버")
                .serverIp("192.168.1.11")
                .build();
        assertEquals(1, serverInfoService.addServerInfo(serverInfo));
    }

    @DisplayName("서버 데이터 등록 - 필수값 null")
    @Test
    @Transactional
    void t05_testAddServer_nullUnique(){
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverOs("window")
                .serverHostname("test")
                .memoryTotal(8012)
                .purpose("서버")
                .serverIp(null)
                .build();
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> serverInfoService.addServerInfo(serverInfo));
    }

    @DisplayName("서버 데이터 등록 - unique 중복")
    @Test
    @Transactional
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t06_testAddServer_DuplicateIp() {
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverOs("windows")
                .serverHostname("test")
                .memoryTotal(8012)
                .purpose("서버")
                .serverIp("192.168.1.1") // 중복 IP 설정
                .build();


        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->serverInfoService.addServerInfo(serverInfo));
    }


    @DisplayName("서버 데이터 수정")
    @Test
    @Transactional
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t06_testUpdateServerInfoTest() {
        //정상 값
        Long testId = 1L;
        ServerInfo updateInfo = new ServerInfo.Builder()
                .serverId(testId)
                .serverOs("테스트Os")
                .memoryTotal(11223)
                .purpose("테스트용")
                .serverHostname("testServer")
                .serverIp("111.222.333.444")
                .build();

        int result = serverInfoService.updateServerInfo(updateInfo);
        assertEquals(1, result);
        assertEquals("111.222.333.444", serverInfoService.findServerInfoById(testId).getServerIp());
        assertEquals("테스트Os", serverInfoService.findServerInfoById(testId).getServerOs());
    }

    @DisplayName("서버 데이터 수정 - 미존재 데이터")
    @Test
    @Transactional
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t07_testUpdateServer_NotExist() {
        //없는 id값
        Long testId = 22L;
        ServerInfo updateInfo = new ServerInfo.Builder()
                .serverId(testId)
                .serverOs("테스트Os")
                .memoryTotal(11223)
                .purpose("테스트용")
                .serverHostname("testServer")
                .serverIp("111.222.333.444")
                .build();

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(()->serverInfoService.updateServerInfo(updateInfo));
    }

    @DisplayName("서버 데이터 수정 - 필수값 null")
    @Test
    @Transactional
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t08_testUpdateServer_DIV() {
        //필수 값 null
        Long testId = 1L;
        ServerInfo updateInfo = new ServerInfo.Builder()
                .serverId(testId)
                .serverOs("테스트Os")
                .memoryTotal(11223)
                .purpose("테스트용")
                .serverHostname("testServer")
                .serverIp(null)
                .build();
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() ->serverInfoService.updateServerInfo(updateInfo));
    }

    @DisplayName("서버 데이터 삭제")
    @Test
    @Transactional
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t09_deleteServer() {
        Long testId = 1L;
        //정상
        int result = serverInfoService.deleteServerInfo(testId);
        assertEquals(1, result);
    }

    @DisplayName("서버 데이터 삭제 - 미존재 데이터")
    @Test
    @Transactional
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void deleteServerNotExistInfoTest() {
        Long testId = 11L;
        //정상
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(()->serverInfoService.deleteServerInfo(testId));
    }
}
