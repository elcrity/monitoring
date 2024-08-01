package com.park.monitoring.service;

import com.park.monitoring.mapper.MetricLogMapper;
import com.park.monitoring.mapper.ServerInfoMapper;
import com.park.monitoring.model.MetricLog;
import org.apache.catalina.Server;
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
    @Autowired
    ServerInfoMapper serverInfoMapper;

    MetricLogService metricLogService;

    @Autowired
    private ServerInfoService serverInfoService;

    @BeforeEach
    public void setUp() {

        metricLogService = new MetricLogService(metricLogMapper,serverInfoMapper);
    }

    @DisplayName("로그 조회 - 전체")
    @Test
    void t00_getLog_All(){
        List<MetricLog> metricLogs = metricLogService.findMetricLogAll();
        assertThat(metricLogs).isNotNull();
        assertThat(metricLogs.size()).isGreaterThan(0);
    }

    @DisplayName("로그 조회 - byId")
    @Test
    void t00_getLogs_byId(){
        List<MetricLog> metricLogs = metricLogService.findMetricLogAllByServerId(id);
        assertThat(metricLogs.size()).isGreaterThan(2);
    }

    @DisplayName("로그 조회 - 없는 id")
    @Test
    void t01_getLogAll_noId() {
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(()->metricLogService.findMetricLogAllByServerId(id+21));
    }

    @DisplayName("로그 조회 - id == null")
    @Test
    void t02_getLogAll_nullId() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->metricLogService.findMetricLogAllByServerId(null));

    }

    @DisplayName("로그 조회 - Latest")
    @Test
    void t03_getRecentLogs_Latest() {
        List<MetricLog> metricLogs = metricLogService.findMetricLogByLatest();
        System.out.println("================= : " + metricLogs);
        assertThat(metricLogs).isNotNull();
        assertThat(metricLogs.size()).isGreaterThan(0);
        for (MetricLog log : metricLogs) {
            assertThat(log.getLogId()).isNotNull();
            assertThat(log.getCpuUsage()).isNotNull();
            assertThat(log.getMemoryUsage()).isNotNull();
            assertThat(log.getServerId()).isNotNull();
            assertThat(log.getCreatedDate()).isNotNull();
        }
    }

    //Todo : delete log 시간 기준 변경으로 고장난 테스트 2개 고치기
    @DisplayName("로그 조회 - dashboard noData")
    @Sql("classpath:sql/testTable.sql")
    @Test
    void t03_01_getRecentLogs_noData() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(()->metricLogService.findMetricLogByLatest());
    }

    @DisplayName("최근 로그 조회 - history")
    @Test
    void t04_getLog_history(){
        List<MetricLog> metricLog = metricLogService.findMetricLogAtHistory(id);
        System.out.println("============================= : " + metricLog);
        assertThat(metricLog).isNotNull();

    }

    @DisplayName("최근 로그 조회 - history exception")
    @Test
    void t04_01_getLog_history(){
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->metricLogService.findMetricLogAtHistory(null));
        int result = metricLogService.deleteMetricLogBeforeTime();
        assertThat(result).isGreaterThan(0);
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(()->metricLogService.findMetricLogAtHistory(id+22));
    }

    @DisplayName("로그 등록")
    @Test
    void t05_addLog(){
        int result = metricLogService.insertMetricLog("192.168.1.1");
        assertThat(result).isEqualTo(1);
    }


    @DisplayName("로그 등록 - fk null")
    @Test
    void t06_addLog_fkNull(){
        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(()->metricLogService.insertMetricLog(null));
    }

    @DisplayName("로그 삭제")
    @Test
    void t07_removeLog(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMinuteBefore = now.minusMinutes(1);

        int result = metricLogService.deleteMetricLogBeforeTime();
        assertThat(result).isGreaterThan(0);
    }

    @DisplayName("로그 삭제 - no Log")
    @Test
    void t08_removeLog_noBeforeTime(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMinuteBefore = now.minusMinutes(5);

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(()->metricLogService.deleteMetricLogBeforeTime());

    }
}
