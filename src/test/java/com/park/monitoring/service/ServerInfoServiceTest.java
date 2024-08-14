package com.park.monitoring.service;

import com.park.monitoring.config.error.ErrorCode;
import com.park.monitoring.config.error.Exception.BadRequestException;
import com.park.monitoring.config.error.Exception.DataIntegrityException;
import com.park.monitoring.config.error.Exception.NotFoundException;
import com.park.monitoring.mapper.ServerInfoMapper;
import com.park.monitoring.model.ServerInfo;
import com.park.monitoring.util.ServerInfoUtil;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

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

    @Mock
    ServerInfoService serverInfoService;

    private MockedStatic<ServerInfoUtil> mockedStatic;

    @BeforeEach
    void init() {
        serverInfoService = new ServerInfoService(serverInfoMapper);
        mockedStatic = Mockito.mockStatic(ServerInfoUtil.class);
    }

    @AfterEach
    void tearDown() {
        mockedStatic.close();
    }
    @DisplayName("서버 데이터 조회")
    @Test
    void t01_testFindAll() {
        assertThat(serverInfoService.findAllServerInfo().size()).isGreaterThan(1);
    }

    @DisplayName("서버 데이터 조회 - 반환값 null")
    @Test
    @Sql("classpath:sql/testTable.sql")
    void t01_e01_testFindAll() {
        assertThatExceptionOfType(NotFoundException.class).isThrownBy(()
                -> serverInfoService.findAllServerInfo());
    }

    @DisplayName("서버 데이터 조회 - id")
    @Test
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t02_testFindById() {
        int id = 1;
        assertThat(serverInfoService.findServerInfoById(id)).isNotNull();
    }
    @DisplayName("서버 데이터 조회 - null")
    @Test
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t02_e01_testFindById_null() {
//        when(serverInfoService.findServerInfoById(null)).thenThrow(BadRequestException.class);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> serverInfoService.findServerInfoById(null))
                .withMessage(ErrorCode.INVALID_INPUT_VALUE.getMessage());
    }

    @DisplayName("서버 데이터 조회 - invalid id")
    @Test
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t02_e02_testFindById_invalid() {
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> serverInfoService.findServerInfoById(0))
                .withMessageContaining(ErrorCode.NOT_FOUND.getMessage());

    }


    @DisplayName("서버 데이터 등록")
    @Test
    @Transactional
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t04_testAddServer() {
        String purpose = "test";
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverOs("windows 11")
                .serverHostname("DESKTOP-61V7M8K")
                .memoryTotal(16440L)
                .purpose(purpose)
                .serverIp("192.168.2.66")
                .build();

        assertThat(serverInfoService.addServerInfo(serverInfo)).isEqualTo(1);
    }

    @DisplayName("서버 데이터 등록 - 필수값 null")
    @Test
    @Transactional
    void t04_e01_testAddServer_nullUnique() {
        String purpose = "test";
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverOs("windows 11")
                .serverHostname("DESKTOP-61V7M8K")
                .memoryTotal(16440L)
                .purpose(purpose)
                .serverIp(null)
                .build();

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> serverInfoService.addServerInfo(serverInfo))
                .withMessage(ErrorCode.INVALID_INPUT_VALUE.getMessage());
    }

    @DisplayName("서버 데이터 등록 - 중복값")
    @Test
    @Transactional
    void t04_e02_testAddServer_duplicate() {
        String purpose = "test";
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverOs("windows 11")
                .serverHostname("DESKTOP-61V7M8K")
                .memoryTotal(16440L)
                .purpose(purpose)
                .serverIp("192.168.1.1")
                .build();
        assertThatExceptionOfType(DataIntegrityException.class)
                .isThrownBy(() -> serverInfoService.addServerInfo(serverInfo))
                .withMessage(ErrorCode.DUPLICATED_ENTITY.getMessage());
    }

    @DisplayName("서버 데이터 수정")
    @Test
    @Transactional
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t05_testUpdateServerInfoTest() {
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
    void t05_e01_testUpdateServer_NotExist() {
        //없는 id값
        ServerInfo updateInfo = new ServerInfo.Builder()
                .serverId(testId + 521)
                .serverOs("테스트Os")
                .memoryTotal(11223L)
                .purpose("테스트용")
                .serverHostname("testServer")
                .serverIp("111.222.333.444")
                .build();

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> serverInfoService.updateServerInfo(updateInfo))
                .withMessage(ErrorCode.NOT_FOUND.getMessage());
    }

    @DisplayName("서버 데이터 수정 - 필수값 null")
    @Test
    @Transactional
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t05_e02_testUpdateServer_DIV() {
        //필수 값 null
        ServerInfo updateInfo = new ServerInfo.Builder()
                .serverId(testId)
                .serverOs("테스트Os")
                .memoryTotal(11223L)
                .purpose("테스트용")
                .serverHostname("testServer")
                .serverIp(null)
                .build();
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> serverInfoService.updateServerInfo(updateInfo))
                .withMessage(ErrorCode.INVALID_INPUT_VALUE.getMessage());
    }

    @DisplayName("서버 데이터 삭제")
    @Test
    @Transactional
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t06_deleteServer() {
        //정상
        assertThat(serverInfoService.deleteServerInfo(testId)).isEqualTo(1);
    }

    @DisplayName("서버 데이터 삭제 - 미존재 데이터")
    @Test
    @Transactional
//    @Sql({"classpath:testTable.sql","classpath:testServerData.sql"})
    void t06_e01_deleteServerNotExistInfoTest() {
        assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> serverInfoService.deleteServerInfo(testId + 522))
                .withMessage(ErrorCode.NOT_FOUND.getMessage());
    }

    @DisplayName("서버 데이터 전체 삭제")
    @Test
    @Transactional
    void t07_deleteAll() {
        assertThat(serverInfoService.deleteAll()).isGreaterThan(1);
    }

    @DisplayName("서버 데이터 전체 삭제 - noData")
    @Test
    @Transactional
    @Sql({"classpath:sql/testTable.sql"})
    void t07_e01_deleteAll_noData() {
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> serverInfoService.deleteAll())
                .withMessage(ErrorCode.NOT_FOUND.getMessage());
    }
}