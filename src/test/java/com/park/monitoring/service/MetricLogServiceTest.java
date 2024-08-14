package com.park.monitoring.service;

import com.park.monitoring.config.error.ErrorCode;
import com.park.monitoring.config.error.Exception.BadRequestException;
import com.park.monitoring.config.error.Exception.BaseException;
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

    int id = 1;
    @Autowired
    MetricLogMapper metricLogMapper;
    @Autowired
    ServerInfoMapper serverInfoMapper;

    MetricLogService metricLogService;

    private ServerInfoService serverInfoService;

    @BeforeEach
    public void setUp() {
        metricLogService = new MetricLogService(metricLogMapper,serverInfoMapper);
    }

    @DisplayName("최근 로그 조회")
    @Test
    void t01_getLogRecent(){
        assertThat(metricLogService.findMetricLogByLatest().size())
                .isGreaterThan(1);
    }

    @DisplayName("최근 로그 조회 - noData")
    @Test
    @Sql("classpath:sql/testTable.sql")
    void t01_e01_getLogRecent_noData(){
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(()->metricLogService.findMetricLogByLatest())
                .withMessage(ErrorCode.NOT_FOUND.getMessage());
    }

    @DisplayName("히스토리 로그 조회")
    @Test
    void t02_getLog_history(){
        assertThat(metricLogService.findMetricLogAtHistory(id).size())
                .isGreaterThan(1);

    }

    @DisplayName("히스토리 로그 조회 - null Id")
    @Test
    void t02_e01_getLogHistory_nullId(){
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(()->metricLogService.findMetricLogAtHistory(null))
                .withMessage(ErrorCode.INVALID_INPUT_VALUE.getMessage());
    }
    @DisplayName("히스토리 로그 조회 - not exist server")
    @Test
    void t02_e02_getLog_history_noServer(){
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(()->metricLogService.findMetricLogAtHistory(id+522))
                .withMessage(ErrorCode.NOT_FOUND.getMessage());
    }

    @DisplayName("로그 등록")
    @Test
    @Transactional
    void t03_addLog(){
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


    @DisplayName("로그 등록 - ip null")
    @Test
    void t03_e01_addLog_ipNull(){
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverId(1000)
                .serverOs("Ubuntu 20.04")
                .serverHostname("server-hostname")
                .memoryTotal(16384L)
                .purpose("Development")
                .serverIp(null)
                .build();

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(()->metricLogService.insertMetricLog(serverInfo))
                .withMessage(ErrorCode.INVALID_INPUT_VALUE.getMessage());
    }

    @DisplayName("로그 등록 - notFoundServer")
    @Test
    void t03_e02_addLog_notFound(){
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverId(1000)
                .serverOs("Ubuntu 20.04")
                .serverHostname("server-hostname")
                .memoryTotal(16384L)
                .purpose("Development")
                .serverIp("192.168.1.100")
                .build();

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(()->metricLogService.insertMetricLog(null))
                .withMessage(ErrorCode.NOT_FOUND.getMessage());
    }

    @DisplayName("로그 삭제")
    @Test
    void t04_removeLog(){
        int result = metricLogService.deleteMetricLogBeforeTime();
        assertThat(result).isGreaterThan(0);
    }

    @DisplayName("로그 삭제 - no Log")
    @Test
    @Sql("classpath:sql/testTable.sql")
    void t04_e01_removeLog_noBeforeTime(){
        assertThatExceptionOfType(BaseException.class)
                .isThrownBy(()->metricLogService.deleteMetricLogBeforeTime())
                .withMessage(ErrorCode.UNEXPECTED_ERROR.getMessage());
    }
}
