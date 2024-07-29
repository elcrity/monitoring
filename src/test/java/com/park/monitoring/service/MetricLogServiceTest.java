package com.park.monitoring.service;

import com.park.monitoring.dto.LogHistoryDto;
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

    int id = 1;
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

    @DisplayName("로그 조회 - byId")
    @Test
    void t00_getLogs_byId(){
        List<MetricLog> metricLogs = metricLogService.getMetricLogAllByServerId(id);
        assertThat(metricLogs.size()).isGreaterThan(2);
    }

    @DisplayName("로그 조회 - 없는 id")
    @Test
    void t01_getLogAll_noId() {
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(()->metricLogService.getMetricLogAllByServerId(id+21));
    }

    @DisplayName("로그 조회 - id == null")
    @Test
    void t02_getLogAll_nullId() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->metricLogService.getMetricLogAllByServerId(null));

    }

    @DisplayName("로그 조회 - dashboard")
    @Test
    void t03_getRecentLogs() {
        List<MetricLog> metricLogs = metricLogService.getMetricLogByLatest();
        assertThat(metricLogs).isNotNull();
        assertThat(metricLogs.size()).isGreaterThan(0);
    }

    @DisplayName("로그 조회 - dashboard noData")
    @Test
    void t03_01_getRecentLogs_noData() {
        int result = metricLogService.deleteMetricLogBeforeTime(LocalDateTime.now().plusMinutes(10));
        assertThat(result).isGreaterThan(0);
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(()->metricLogService.getMetricLogByLatest());
    }

    @DisplayName("최근 로그 조회 - history")
    @Test
    void t04_getLog_history(){
        LogHistoryDto metricLog = metricLogService.getMetricLogAtHistory(id);
        assertThat(metricLog).isNotNull();
        assertThat(metricLog.getLogs())
                .contains("disk_usage1", "disk_total2");

    }

    @DisplayName("최근 로그 조회 - history exception")
    @Test
    void t04_01_getLog_history(){
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->metricLogService.getMetricLogAtHistory(null));
        int result = metricLogService.deleteMetricLogBeforeTime(LocalDateTime.now().plusMinutes(10));
        assertThat(result).isGreaterThan(0);
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(()->metricLogService.getMetricLogAtHistory(id));
    }

    @DisplayName("로그 등록")
    @Test
    void t05_addLog(){
        id = 3;
        MetricLog metricLog = new MetricLog.Builder()
                .cpuUsage(22.2)
                .memoryUsage(33.3)
                .serverMetricFk(id)
                .build();
        int result = metricLogService.insertMetricLog(metricLog);
        assertThat(result).isEqualTo(1);
    }

    @DisplayName("로그 등록 - 필드 누락")
    @Test
    void t06_addLog_omission(){
        id = 3;
        MetricLog metricLog = new MetricLog.Builder()
                .cpuUsage(22.2)
                .serverMetricFk(id)
                .build();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->metricLogService.insertMetricLog(metricLog));
    }
    @DisplayName("로그 등록 - fk null")
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
        assertThat(result).isGreaterThan(0);

    }

    @DisplayName("로그 삭제 - no Log")
    @Test
    void t08_removeLog_noBeforeTime(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMinuteBefore = now.minusMinutes(5);

        int result = metricLogService.deleteMetricLogBeforeTime(oneMinuteBefore);
        assertThat(result).isEqualTo(0);

    }

    @DisplayName("로그 삭제 - null")
    @Test
    void t08_removeLog_timeIsNull(){
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->metricLogService.deleteMetricLogBeforeTime(null));

    }
}
