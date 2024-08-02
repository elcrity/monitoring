package com.park.monitoring.service;

import com.park.monitoring.mapper.ServerInfoMapper;
import com.park.monitoring.model.ServerInfo;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@Transactional(readOnly = true)
@ActiveProfiles("test")
@Sql({"classpath:sql/testTable.sql", "classpath:sql/testData.sql"})
public class ServerInfoServiceTest {
    Logger log = LoggerFactory.getLogger(ServerInfoServiceTest.class);
    int testId = 1;

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
        assertThat(serverInfo.size()).isEqualTo(10);
    }

    @DisplayName("서버 데이터 조회 - id")
    @Test
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t02_testFindById() {
        ServerInfo serverInfo = serverInfoService.findServerInfoById(testId);
        assertThat(serverInfo).isNotNull();
        assertThat(serverInfo.getServerId()).isEqualTo(testId);
        assertThat(serverInfo.getServerOs()).isEqualTo("Ubuntu 20.04"); // 예상되는 OS 버전
        assertThat(serverInfo.getServerHostname()).isNotNull(); // 예상되는 호스트명
        assertThat(serverInfo.getMemoryTotal()).isNotNull(); // 예상되 // 예상되는 메모리 총합
        assertThat(serverInfo.getPurpose()).isNotNull(); // 예상되 // 예상되는 목적
        assertThat(serverInfo.getServerIp()).isNotNull(); // 예상되 // 예상되는 IP 주소
    }

    @DisplayName("서버 데이터 조회 - 없는 id, null")
    @Test
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t03_testFindById_noElement() {
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> serverInfoService.findServerInfoById(testId * -1));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> serverInfoService.findServerInfoById(null));
    }

    @DisplayName("서버 데이터 조회 - 디스크")
    @Test
    void t04_testFindAll_withDisk() {
        List<ServerInfo> dtoList = serverInfoService.findAllServerInfo();
        List<String> serverOsList = dtoList.stream()
                .map(ServerInfo::getServerOs)
                .collect(Collectors.toList());
        assertThat(serverOsList)
                .anyMatch(os -> os.contains("Ubuntu"));
        assertThat(serverOsList)
                .anyMatch(os -> os.contains("Windows"));

        dtoList.forEach(dto -> {
            assertThat(dto.getServerId()).isNotNull();
            assertThat(dto.getServerOs()).isNotNull();
            assertThat(dto.getServerHostname()).isNotNull();
            assertThat(dto.getPurpose()).isNotNull();
            assertThat(dto.getServerIp()).isNotNull();
        });
    }

    @DisplayName("서버 데이터 조회 - 디스크 noData")
    @Sql("classpath:sql/testTable.sql")
    @Test
    void t04_01_testFindAll_withDisk_noData() {
        assertThatExceptionOfType(EmptyResultDataAccessException.class)
                .isThrownBy(() -> serverInfoService.deleteAll());
    }

    @DisplayName("서버 데이터 조회 - 히스토리")
    @Test
    void t05_testFindAll_history() {
        ServerInfo dtoList = serverInfoService.findServerInfoAtHistory(1);
        assertThat(dtoList).isNotNull();
        assertThat(dtoList.getServerHostname()).isEqualTo("server1");
    }

    @DisplayName("서버 데이터 조회 - 히스토리 noData")
    @Test
    void t05_testFindAll_history_noData() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> serverInfoService.findServerInfoAtHistory(null));
        int result = serverInfoService.deleteAll();
        assertThat(result).isGreaterThan(0);
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> serverInfoService.findServerInfoAtHistory(1));

    }

    @DisplayName("서버 데이터 등록")
    @Test
    @Transactional
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t04_testAddServer() {
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverOs("window")
                .serverHostname("test")
                .memoryTotal(8012L)
                .purpose("서버")
                .serverIp("192.168.1.11")
                .build();
        assertThat(serverInfoService.addServerInfo(serverInfo)).isEqualTo(1);
    }

    @DisplayName("서버 데이터 등록 - 필수값 null")
    @Test
    @Transactional
    void t05_testAddServer_nullUnique() {
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverOs("window")
                .serverHostname("test")
                .memoryTotal(8012L)
                .purpose("서버")
                .serverIp(null)
                .build();
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> serverInfoService.addServerInfo(serverInfo));
        serverInfo.setServerIp("192.168.1.11");
        serverInfo.setServerOs(null);
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> serverInfoService.addServerInfo(serverInfo));
        serverInfo.setServerOs("window");
        serverInfo.setServerHostname(null);
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> serverInfoService.addServerInfo(serverInfo));
    }

    @DisplayName("서버 데이터 등록 - unique 중복")
    @Test
    @Transactional
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t06_testAddServer_DuplicateIp() {
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverOs("windows")
                .serverHostname("test")
                .memoryTotal(8012L)
                .purpose("서버")
                .serverIp("192.168.1.1") // 중복 IP 설정
                .build();

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> serverInfoService.addServerInfo(serverInfo));
    }

    @DisplayName("서버 데이터 수정")
    @Test
    @Transactional
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t06_testUpdateServerInfoTest() {
        //정상 값
        ServerInfo updateInfo = new ServerInfo.Builder()
                .serverId(testId)
                .serverOs("테스트Os")
                .memoryTotal(11223L)
                .purpose("테스트용")
                .serverHostname("testServer")
                .serverIp("111.222.333.444")
                .build();

        int result = serverInfoService.updateServerInfo(updateInfo);
        assertThat(result).isEqualTo(1);
        assertThat(serverInfoService.findServerInfoById(testId).getServerIp()).isEqualTo("111.222.333.444");
        assertThat(serverInfoService.findServerInfoById(testId).getServerOs()).isEqualTo("테스트Os");
    }

    @DisplayName("서버 데이터 수정 - notExist")
    @Test
    @Transactional
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t07_testUpdateServer_NotExist() {
        //없는 id값
        ServerInfo updateInfo = new ServerInfo.Builder()
                .serverId(testId + 21)
                .serverOs("테스트Os")
                .memoryTotal(11223L)
                .purpose("테스트용")
                .serverHostname("testServer")
                .serverIp("111.222.333.444")
                .build();

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> serverInfoService.updateServerInfo(updateInfo));
    }

    @DisplayName("서버 데이터 수정 - 필수값 null")
    @Test
    @Transactional
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t08_testUpdateServer_DIV() {
        //필수 값 null
        ServerInfo updateInfo = new ServerInfo.Builder()
                .serverId(testId)
                .serverOs("테스트Os")
                .memoryTotal(11223L)
                .purpose("테스트용")
                .serverHostname("testServer")
                .serverIp(null)
                .build();
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> serverInfoService.updateServerInfo(updateInfo));
    }

    @DisplayName("서버 데이터 삭제")
    @Test
    @Transactional
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t09_deleteServer() {
        //정상
        int result = serverInfoService.deleteServerInfo(testId);
        assertThat(result).isEqualTo(1);
    }

    @DisplayName("서버 데이터 삭제 - 미존재 데이터")
    @Test
    @Transactional
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t10_deleteServerNotExistInfoTest() {
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> serverInfoService.deleteServerInfo(testId + 22));
    }
}