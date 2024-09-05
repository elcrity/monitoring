package com.park.monitoring.service;

import com.park.monitoring.config.error.ErrorCode;
import com.park.monitoring.config.error.Exception.BadRequestException;
import com.park.monitoring.config.error.Exception.BaseException;
import com.park.monitoring.config.error.Exception.NoContentException;
import com.park.monitoring.config.error.Exception.NotFoundException;
import com.park.monitoring.dto.DiskInfo;
import com.park.monitoring.dto.LogInput;
import com.park.monitoring.mapper.MetricLogMapper;
import com.park.monitoring.mapper.ServerInfoMapper;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

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
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDate = date.format(formatter);
        assertThat(metricLogService.findMetricLogAtHistory(1, false, LocalDateTime.now()).size())
                .isGreaterThan(0);

    }

    @DisplayName("로그 히스토리 조회 - null Id")
    @Test
    void t02_02getLogHistory_nullId() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> metricLogService.findMetricLogAtHistory(null, false, LocalDateTime.now()))
                .withMessage(ErrorCode.INVALID_INPUT_VALUE.getMessage());
    }

    @DisplayName("로그 등록")
    @Test
    @Transactional
    void t03_01addLog_success() {
        LogInput logInput = new LogInput();
        logInput.setServerIp("192.168.1.1");
        logInput.setCpuUsage(0.25);  // 25%의 CPU 사용률
        logInput.setMemoryUsage(0.65);  // 65%의 메모리 사용률

        // DiskInfo 객체 생성
        List<Double> diskTotalData = Arrays.asList(512312.0, 256773.0);
        List<Double> diskUsageData = Arrays.asList(20.2, 15.3);
        List<String> diskName = Arrays.asList("C:/", "D:/");

        DiskInfo diskInfo = new DiskInfo(diskTotalData, diskUsageData, diskName);

        assertThat(metricLogService.insertMetricLog(logInput, diskInfo))
                .isEqualTo(1);
    }

    @DisplayName("로그 등록 - serverIp null")
    @Test
    void t03_02addLog_ipNull() {
        LogInput logInput = new LogInput();
        logInput.setServerIp("192.168.1.100");
        logInput.setCpuUsage(0.25);  // 25%의 CPU 사용률
        logInput.setMemoryUsage(0.65);  // 65%의 메모리 사용률

        // DiskInfo 객체 생성
        List<Double> diskTotalData = Arrays.asList(512312.0, 256773.0);
        List<Double> diskUsageData = Arrays.asList(20.2, 15.3);
        List<String> diskName = Arrays.asList("C:/", "D:/");

        DiskInfo diskInfo = new DiskInfo(diskTotalData, diskUsageData, diskName);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> metricLogService.insertMetricLog(null, null))
                .withMessage(ErrorCode.INVALID_INPUT_VALUE.getMessage());
    }

    @DisplayName("로그 등록 - notFoundServer")
    @Test
    void t03_03addLog_notFound() {
        LogInput logInput = new LogInput();
        logInput.setServerIp("192.168.1.100");
        logInput.setCpuUsage(0.25);  // 25%의 CPU 사용률
        logInput.setMemoryUsage(0.65);  // 65%의 메모리 사용률

        // DiskInfo 객체 생성
        List<Double> diskTotalData = Arrays.asList(512312.0, 256773.0);
        List<Double> diskUsageData = Arrays.asList(20.2, 15.3);
        List<String> diskName = Arrays.asList("C:/", "D:/");

        DiskInfo diskInfo = new DiskInfo(diskTotalData, diskUsageData, diskName);

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> metricLogService.insertMetricLog(logInput, diskInfo))
                .withMessage(ErrorCode.NOT_FOUND.getMessage());
    }

    @DisplayName("로그 삭제")
    @Test
    void t04_01removeLog() {
        assertThat(metricLogService.deleteMetricLogBeforeTime())
                .isGreaterThan(0);
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
