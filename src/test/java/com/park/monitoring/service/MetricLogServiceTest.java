package com.park.monitoring.service;

import com.park.monitoring.config.error.ErrorCode;
import com.park.monitoring.config.error.Exception.BadRequestException;
import com.park.monitoring.config.error.Exception.BaseException;
import com.park.monitoring.config.error.Exception.NoContentException;
import com.park.monitoring.config.error.Exception.NotFoundException;
import com.park.monitoring.mapper.MetricLogMapper;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@Transactional(readOnly = true)
@ActiveProfiles("test")
@Sql({"classpath:sql/testTable.sql", "classpath:sql/testData.sql"})
@DisplayName("로그 서비스 테스트")
public class MetricLogServiceTest {
    Logger log = LoggerFactory.getLogger(ServerInfoServiceTest.class);

    @Autowired
    MetricLogMapper metricLogMapper;
    @Autowired
    ServerInfoMapper serverInfoMapper;

    MetricLogService metricLogService;

    @BeforeEach
    public void setUp() {
        metricLogService = new MetricLogService(metricLogMapper, serverInfoMapper);
    }

    @DisplayName("최근 로그 조회")
    @Test
    void t01_01getLogRecent_success() {
        assertThat(metricLogService.findMetricLogByLatest().size())
                .isGreaterThan(0);
    }

    @DisplayName("최근 로그 조회 - noData")
    @Test
    @Sql("classpath:sql/testTable.sql")
    void t01_02getLogRecent_noData() {
        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> metricLogService.findMetricLogByLatest())
                .withMessage(ErrorCode.NO_CONTENT.getMessage());
    }

    @DisplayName("로그 히스토리 조회")
    @Test
    void t02_01getLogHistory_success() {
        assertThat(metricLogService.findMetricLogAtHistory(1).size())
                .isGreaterThan(0);

    }

    @DisplayName("로그 히스토리 조회 - null Id")
    @Test
    void t02_02getLogHistory_nullId() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> metricLogService.findMetricLogAtHistory(null))
                .withMessage(ErrorCode.INVALID_INPUT_VALUE.getMessage());
    }

    @DisplayName("로그 히스토리 조회 - not existId")
    @Test
    void t02_03getLogHistory_e02_noServer() {
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> metricLogService.findMetricLogAtHistory(-1))
                .withMessage(ErrorCode.NOT_FOUND.getMessage());
    }

    @DisplayName("로그 등록")
    @Test
    @Transactional
    void t03_01addLog_success() {
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverId(1)
                .serverOs("Ubuntu 20.04")
                .serverHostname("server-hostname")
                .memoryTotal(16384L)
                .purpose("Development")
                .serverIp("192.168.1.100")
                .build();

        assertThat(metricLogService.insertMetricLog(serverInfo))
                .isEqualTo(1);
    }

    @DisplayName("로그 등록 - serverIp null")
    @Test
    void t03_02addLog_ipNull() {
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverId(1000)
                .serverOs("Ubuntu 20.04")
                .serverHostname("server-hostname")
                .memoryTotal(16384L)
                .purpose("Development")
                .serverIp(null)
                .build();

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> metricLogService.insertMetricLog(serverInfo))
                .withMessage(ErrorCode.INVALID_INPUT_VALUE.getMessage());
    }

    @DisplayName("로그 등록 - notFoundServer")
    @Test
    void t03_03addLog_notFound() {
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverId(999)
                .serverOs("Ubuntu 20.04")
                .serverHostname("server-hostname")
                .memoryTotal(16384L)
                .purpose("Development")
                .serverIp("192.168.1.100")
                .build();

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> metricLogService.insertMetricLog(null))
                .withMessage(ErrorCode.NOT_FOUND.getMessage());
    }

    @DisplayName("로그 삭제")
    @Test
    void t04_01removeLog() {
        int result = metricLogService.deleteMetricLogBeforeTime();
        assertThat(result).isGreaterThan(0);
    }

    @DisplayName("로그 삭제 - no Log")
    @Test
    @Sql("classpath:sql/testTable.sql")
    void t04_02removeLog_noBeforeTime() {
        assertThatExceptionOfType(BaseException.class)
                .isThrownBy(() -> metricLogService.deleteMetricLogBeforeTime())
                .withMessage(ErrorCode.UNEXPECTED_ERROR.getMessage());
    }
}
