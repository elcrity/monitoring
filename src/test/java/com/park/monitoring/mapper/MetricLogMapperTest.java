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

    @DisplayName("로그 조회 - 서버 id 전체")
    @Test
    void t00_readLog_All(){
        Long id = 1L;
        List<MetricLog> metricLogs = metricLogMapper.selectLogAllByServerId(id);

        assertThat(metricLogs).isNotNull();
        assertThat(metricLogs.size()).isEqualTo(2);
    }

    @DisplayName("로그 조회 - 서버 id 최신")
    @Test
    void t01_readLog_Recent(){
        Long id = 2L;
        MetricLog log = metricLogMapper.selectRecentLog(id);
        assertThat(log).isNotNull();
        assertThat(log.getCpuUsage()).isEqualTo(40.0);
    }

    @DisplayName("로그 등록")
    @Test
    void t02_insertLog(){
        MetricLog log = new MetricLog.Builder()
                .cpuUsage(44.4)
                .memoryUsage(25.5)
                .serverMetricFk(2L)
                .build();
        int result = metricLogMapper.insertLog(log);
        assertThat(result).isEqualTo(1);
    }

    @DisplayName("로그 삭제")
    @Test
    void t03_deleteLog(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneSecondBefore = now.minusMinutes(1);

        //2초 간격으로 데이터 삽입, 처음 삽입한 데이터 5개만 삭제
        int result = metricLogMapper.deleteLogBeforeTime(oneSecondBefore);
        assertThat(result).isEqualTo(5);
    }

}
