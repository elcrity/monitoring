package com.park.monitoring.mapper;

import com.park.monitoring.model.DiskLog;
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
public class DiskLogMapperTest {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    DiskLogMapper  diskLogMapper;

    @DisplayName("디스크 로그 조회 - 전체")
    @Test
    void t00_getAllDiskLogs(){
        List<DiskLog> diskLogList = diskLogMapper.selectAllDiskLogs();
        assertThat(diskLogList).isNotNull();
        assertThat(diskLogList.size()).isGreaterThan(2);
    }

    @DisplayName("디스크 로그 조회 - id")
    @Test
    void t01_getDiskLogById(){
        Long diskId = 2L;
        List<DiskLog> diskLogList = diskLogMapper.selectDiskLogsByFk(diskId);
        assertThat(diskLogList).isNotNull();
        assertThat(diskLogList.size()).isGreaterThan(1);
    }

    @DisplayName("디스크 로그 조회 - 최근")
    @Test
    void t02_getDiskLogRecent(){
        Long diskId = 2L;
        DiskLog diskLog = diskLogMapper.selectDiskLogRecent(diskId);
        assertThat(diskLog).isNotNull();
        assertThat(diskLog.getDiskUsage()).isEqualTo(11.7);
    }

    @DisplayName("디스크 로그 등록")
    @Test
    void t03_insertDiskLog(){
        DiskLog diskLog = new DiskLog.Builder()
                .diskDiskLogFk(3L)
                .diskUsage(99.9)
                .build();
        int result = diskLogMapper.insertDiskLog(diskLog);
        assertThat(result).isEqualTo(1);
        List<Double> diskUsages = diskLogMapper.selectDiskLogsByFk(3L)
                .stream()
                .map(DiskLog::getDiskUsage)
                .toList();
        assertThat(diskUsages).contains(99.9);
    }

    @DisplayName("디스크 로그 삭제")
    @Test
    void t04_deleteDiskLog(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beforeMinute = now.minusMinutes(1);
        int result = diskLogMapper.deleteDiskLogBeforeTime(beforeMinute);
        assertThat(result).isGreaterThan(0);
    }
}
