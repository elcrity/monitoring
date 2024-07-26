package com.park.monitoring.service;

import com.park.monitoring.mapper.MetricLogMapper;
import com.park.monitoring.mapper.ServerInfoMapper;
import com.park.monitoring.model.MetricLog;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@Transactional(readOnly = true)
@ActiveProfiles("test")
@Sql({"classpath:sql/testTable.sql", "classpath:sql/testData.sql"})
public class MetricLogServiceTest {
    Logger log = LoggerFactory.getLogger(ServerInfoServiceTest.class);

    @Autowired
    MetricLogMapper metricLogMapper;

    MetricLogService metricLogService;

    @BeforeEach
    public void setUp() {
        metricLogService = new MetricLogService(metricLogMapper);
    }

    @DisplayName("로그 조회 - 전체")
    @Test
    void t00_getLog_All(){
        List<MetricLog> metricLogs = metricLogService.getMetricLogAll();
        assertThat(metricLogs).isNotNull();
        assertThat(metricLogs.size()).isGreaterThan(0);
    }

    @DisplayName("로그 조회 - id전체")
    @Test
    void t00_getLogs_byId(){
        Long id = 2L;
        List<MetricLog> metricLogs = metricLogService.getMetricLogAllByServerId(id);
        assertThat(metricLogs.size()).isEqualTo(2);
    }

    @DisplayName("로그 조회 - 없는 id")
    @Test
    void t01_getLogAll_noId() {
        Long id = 22L;
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(()->metricLogService.getMetricLogAllByServerId(id));
    }

    @DisplayName("로그 조회 - id == null")
    @Test
    void t02_getLogAll_nullId() {
        Long id = null;
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->metricLogService.getMetricLogAllByServerId(id));

    }

    @DisplayName("최근 로그 조회")
    @Test
    void t03_getLog_recent(){
        Long id = 2L;
        MetricLog metricLog = metricLogService.getMetricLogRecent(id);
        assertThat(metricLog.getCpuUsage()).isEqualTo(35.0);
    }

    @DisplayName("최근 로그 조회 - null id")
    @Test
    void t04_getLog_recentNull(){
        Long id = null;
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->metricLogService.getMetricLogRecent(id));
    }

    @DisplayName("최근 로그 조회 - 없는 id")
    @Test
    void t04_getLog_recentNoId(){
        Long id = 22L;
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(()->metricLogService.getMetricLogRecent(id));
    }

    @DisplayName("로그 등록")
    @Test
    void t05_addLog(){
        MetricLog metricLog = new MetricLog.Builder()
                .cpuUsage(22.2)
                .memoryUsage(33.3)
                .serverMetricFk(3L)
                .build();
        int result = metricLogService.insertMetricLog(metricLog);
        assertThat(result).isEqualTo(1);
    }

    @DisplayName("로그 등록 - 필드 누락")
    @Test
    void t06_addLog_omission(){
        MetricLog metricLog = new MetricLog.Builder()
                .cpuUsage(22.2)
                .serverMetricFk(3L)
                .build();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->metricLogService.insertMetricLog(metricLog));
    }
    @DisplayName("로그 등록 - 필드 누락")
    @Test
    void t06_addLog_null(){
        MetricLog metricLog = new MetricLog.Builder()
                .cpuUsage(22.2)
                .memoryUsage(33.3)
                .serverMetricFk(null)
                .build();

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(()->metricLogService.insertMetricLog(metricLog));
    }

    @DisplayName("로그 삭제")
    @Test
    void t07_removeLog(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMinuteBefore = now.minusMinutes(1);

        int result = metricLogService.deleteMetricLogBeforeTime(oneMinuteBefore);
        assertThat(result).isEqualTo(5);

    }

    @DisplayName("로그 삭제")
    @Test
    void t08_removeLog_noBeforeTime(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMinuteBefore = now.minusMinutes(5);

        int result = metricLogService.deleteMetricLogBeforeTime(oneMinuteBefore);
        assertThat(result).isEqualTo(0);

    }

    @DisplayName("로그 삭제")
    @Test
    void t08_removeLog_timeIsNull(){
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->metricLogService.deleteMetricLogBeforeTime(null));

    }
}
