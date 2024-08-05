package com.park.monitoring.mapper;

import com.park.monitoring.model.MetricLog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@Transactional(readOnly = true)
@ActiveProfiles("test")
@Sql({"classpath:sql/testTable.sql", "classpath:sql/testData.sql"})
public class MetricLogMapperTest {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MetricLogMapper metricLogMapper;

    @DisplayName("로그 조회 - 전체")
    @Test
    void t00_readLog_all(){
        List<MetricLog> metricLogs = metricLogMapper.selectAll();
        metricLogs.forEach(metricLog -> {
            assertThat(metricLog).isNotNull();
            assertThat(metricLog.getLogId()).isNotNull();
            assertThat(metricLog.getCpuUsage()).isNotNull();
            assertThat(metricLog.getMemoryUsage()).isNotNull();
            assertThat(metricLog.getServerId()).isNotNull();
            assertThat(metricLog.getCreatedDate()).isNotNull();
        });
    }

    @DisplayName("로그 조회 - 서버 id 전체")
    @Test
    void t01_readLog_byId(){
        int id = 1;
        List<MetricLog> metricLogs = metricLogMapper.selectLogAllByServerId(id);
        assertThat(metricLogs).isNotNull();
        assertThat(metricLogs.size()).isGreaterThan(2);
    }

    @DisplayName("로그 조회 - 최신 로그")
    @Test
    void t02_readLogAll_latest(){
        List<MetricLog> metricLogs = metricLogMapper.selectLogAllByLatest();
        System.out.println("================= : " + metricLogs);
        metricLogs.forEach(metricLog -> {
            assertThat(metricLog).isNotNull();
            assertThat(metricLog.getLogId()).isNotNull();
            assertThat(metricLog.getCpuUsage()).isNotNull();
            assertThat(metricLog.getMemoryUsage()).isNotNull();
            assertThat(metricLog.getServerId()).isNotNull();
            assertThat(metricLog.getCreatedDate()).isNotNull();
//            assertThat(metricLog.getDiskUsage1()).isNotNull();
//            assertThat(metricLog.getDiskTotal1()).isNotNull();

        });
    }

    @DisplayName("로그 조회 - history")
    @Test
    void t03_readLog_history(){
        int id = 1;
        List<MetricLog> logs = metricLogMapper.selectLogHistory(id);
        assertThat(logs).isNotNull();
        assertThat(log).isNotNull();
        assertThat(logs.size()).isGreaterThan(2);
        MetricLog log = logs.get(0);

        assertThat(log.getCpuUsage()).isNotNull(); // Replace with expected value if necessary
        assertThat(log.getMemoryUsage()).isNotNull(); // Replace with expected value if necessary
        assertThat(log.getServerId()).isNotNull(); // Replace with expected value if necessary
        assertThat(log.getCreatedDate()).isNotNull(); // Replace with expected value if necessary
    }

    @DisplayName("로그 등록")
    @Test
    void t04_insertLog(){
        MetricLog log = new MetricLog.Builder()
                .cpuUsage(55.5) // CPU 사용률
                .memoryUsage(30.0) // 메모리 사용률
                .serverMetricFk(3) // 서버 메트릭 외래 키
                .diskUsage1(75.0) // 디스크 사용량 1
                .diskUsage2(60.0) // 디스크 사용량 2
                .diskUsage3(45.0) // 디스크 사용량 3
                .diskUsage4(80.0) // 디스크 사용량 4
                .diskTotal1(100000.0) // 디스크 총 용량 1
                .diskTotal2(200000.0) // 디스크 총 용량 2
                .diskTotal3(300000.0) // 디스크 총 용량 3
                .diskTotal4(400000.0) // 디스크 총 용량 4
                .diskName1("Disk A") // 디스크 이름 1
                .diskName2("Disk B") // 디스크 이름 2
                .diskName3("Disk C") // 디스크 이름 3
                .diskName4("Disk D") // 디스크 이름 4
                .build();
        int result = metricLogMapper.insertLog(log);
        assertThat(result).isEqualTo(1);
    }

    @DisplayName("로그 삭제")
    @Test
    void t05_deleteLog(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneSecondBefore = now.minusMinutes(1);

        //2초 간격으로 데이터 삽입, 처음 삽입한 데이터 5개만 삭제
        int result = metricLogMapper.deleteLogBeforeTime();
        assertThat(result).isGreaterThan(5);
    }

}
