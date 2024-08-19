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
@DisplayName("서버 서비스 테스트")
public class ServerInfoServiceTest {
    Logger log = LoggerFactory.getLogger(ServerInfoServiceTest.class);

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
    void t01_01testFindAll() {
        assertThat(serverInfoService.findAllServerInfo().size())
                .isGreaterThan(1);
    }

    @DisplayName("서버 데이터 조회 - 반환값 null")
    @Test
    @Sql("classpath:sql/testTable.sql")
    void t01_02testFindAll() {
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> serverInfoService.findAllServerInfo())
                .withMessage(ErrorCode.NOT_FOUND.getMessage());
    }

    @DisplayName("서버 데이터 조회 - id")
    @Test
    void t02_01testFindById() {
        assertThat(serverInfoService.findServerInfoById(1))
                .isNotNull();
    }
    @DisplayName("서버 데이터 조회 - null")
    @Test
    void t02_02testFindById_null() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> serverInfoService.findServerInfoById(null))
                .withMessage(ErrorCode.INVALID_INPUT_VALUE.getMessage());
    }

    @DisplayName("서버 데이터 조회 - invalid id")
    @Test
    void t02_03testFindById_invalid() {
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> serverInfoService.findServerInfoById(0))
                .withMessage(ErrorCode.NOT_FOUND.getMessage());

    }

    @DisplayName("id 확인")
    @Test
    void t03_01testFindByIp() {
        assertThat(serverInfoService.findServerIdByIp("192.168.1.1"))
                .isEqualTo(1);
    }

    @DisplayName("ip 확인 - null")
    @Test
    void t03_02testFindByIp_badRequest() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> serverInfoService.findServerIdByIp(null))
                .withMessage(ErrorCode.INVALID_INPUT_VALUE.getMessage());

    }
    @DisplayName("id 확인 - invalid")
    @Test
    void t03_03testFindByIp_notFound() {
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> serverInfoService.findServerIdByIp("192.168.999.999"))
                .withMessage(ErrorCode.NOT_FOUND.getMessage());

    }

    @DisplayName("서버 데이터 등록")
    @Test
    @Transactional
    void t04_01testAddServer() {
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverHostname("DESKTOP-61V7M8K")
                .memoryTotal(16440L)
                .purpose("test")
                .serverIp("192.168.2.66")
                .build();

        assertThat(serverInfoService.addServerInfo(serverInfo)).isEqualTo(1);
    }

    @DisplayName("서버 데이터 등록 - 필수값 null")
    @Test
    @Transactional
    void t04_02testAddServer_nullUnique() {
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverOs("windows 11")
                .serverHostname(null)
                .memoryTotal(16440L)
                .purpose("test")
                .serverIp(null)
                .build();

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> serverInfoService.addServerInfo(serverInfo))
                .withMessage(ErrorCode.INVALID_INPUT_VALUE.getMessage());
    }

    @DisplayName("서버 데이터 등록 - 중복값")
    @Test
    @Transactional
    void t04_03testAddServer_duplicate() {
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverOs("windows 11")
                .serverHostname("DESKTOP-61V7M8K")
                .memoryTotal(16440L)
                .purpose("test")
                .serverIp("192.168.1.1")//중복
                .build();
        assertThatExceptionOfType(DataIntegrityException.class)
                .isThrownBy(() -> serverInfoService.addServerInfo(serverInfo))
                .withMessage(ErrorCode.DUPLICATED_ENTITY.getMessage());
    }

    @DisplayName("서버 데이터 수정")
    @Test
    @Transactional
    void t05_01testUpdateServerInfoTest() {
        ServerInfo updateInfo = new ServerInfo.Builder()
                .serverId(1)
                .serverOs("테스트Os")
                .memoryTotal(11223L)
                .purpose("테스트용")
                .serverHostname("testServer")
                .serverIp("111.222.333.444")
                .build();
        int result = serverInfoService.updateServerInfo(updateInfo);

        assertThat(result).isEqualTo(1);
    }

    @DisplayName("서버 데이터 수정 - invalid Id")
    @Test
    @Transactional
    void t05_02testUpdateServer_invalidId() {
        //없는 id값
        ServerInfo updateInfo = new ServerInfo.Builder()
                .serverId(-1)
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
    void t05_03testUpdateServer_null() {
        //필수 값 null
        ServerInfo updateInfo = new ServerInfo.Builder()
                .serverId(null)
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
    void t06_01deleteServer() {
        //정상
        assertThat(serverInfoService.deleteServerInfo(1))
                .isEqualTo(1);
    }

    @DisplayName("서버 데이터 삭제 - 미존재 데이터")
    @Test
    @Transactional
    void t06_02deleteServerNotExistInfoTest() {
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> serverInfoService.deleteServerInfo(-1))
                .withMessage(ErrorCode.NOT_FOUND.getMessage());
    }

    @DisplayName("서버 데이터 전체 삭제")
    @Test
    @Transactional
    void t07_01deleteAll() {
        assertThat(serverInfoService.deleteAll())
                .isGreaterThan(1);
    }

    @DisplayName("서버 데이터 전체 삭제 - noData")
    @Test
    @Transactional
    @Sql({"classpath:sql/testTable.sql"})
    void t07_02deleteAll_noData() {
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> serverInfoService.deleteAll())
                .withMessage(ErrorCode.NOT_FOUND.getMessage());
    }
}