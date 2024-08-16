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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@Transactional(readOnly = true)
@ActiveProfiles("test")
@Sql({"classpath:sql/testTable.sql", "classpath:sql/testData.sql"})
@DisplayName("로그 매퍼 테스트")
public class MetricLogMapperTest {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MetricLogMapper metricLogMapper;

    @DisplayName("로그 조회 ")
    @Test
    void t01_readLoLatest(){
        assertThat(metricLogMapper.selectLogAllByLatest()).isNotNull();
    }

    @DisplayName("로그 조회 - history")
    @Test
    void t02_readLog_history(){
        assertThat(metricLogMapper.selectLogHistory(1).size()).isGreaterThan(1);
    }

    @DisplayName("로그 등록")
    @Test
    void t03_insertLog(){
        MetricLog log = new MetricLog.Builder()
                .cpuUsage(55.5) // CPU 사용률
                .memoryUsage(30.0) // 메모리 사용률
                .serverMetricFk(3) // 서버 메트릭 외래 키
                .diskUsage1(75.0) // 디스크 사용량 1
                .diskUsage2(60.0) // 디스크 사용량 2
                .diskUsage3(45.0) // 디스크 사용량 3
                .diskTotal1(100000.0) // 디스크 총 용량 1
                .diskTotal2(200000.0) // 디스크 총 용량 2
                .diskTotal3(300000.0) // 디스크 총 용량 3
                .diskName1("Disk A") // 디스크 이름 1
                .diskName2("Disk B") // 디스크 이름 2
                .diskName3("Disk C") // 디스크 이름 3
                .build();
        assertThat(metricLogMapper.insertLog(log)).isEqualTo(1);
    }

    @DisplayName("로그 삭제")
    @Test
    void t04_deleteLog(){
        //로그 데이터는 현재 시간, 시간-1분, -2분,-3분 등록
        assertThat(metricLogMapper.deleteLogBeforeTime()).isGreaterThan(1);
    }
}
