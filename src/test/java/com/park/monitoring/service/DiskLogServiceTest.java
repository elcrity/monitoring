package com.park.monitoring.service;

import com.park.monitoring.mapper.DiskLogMapper;
import com.park.monitoring.model.DiskLog;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@Transactional(readOnly = true)
@ActiveProfiles("test")
@Sql({"classpath:sql/testTable.sql", "classpath:sql/testData.sql"})
@DisplayName("디스크 로그 서비스 테스트")
public class DiskLogServiceTest {

    @Autowired
    DiskLogMapper diskLogMapper;

    DiskLogService diskLogService;
    @Autowired
    private ServerInfoService serverInfoService;

    @BeforeEach
    public void setUp() {
        diskLogService = new DiskLogService(diskLogMapper);
    }

    @DisplayName("로그 조회 - 전체")
    @Test
    void t00_getAllDiskLogs() {
        List<DiskLog> diskLogs = diskLogService.getAllDiskLogs();
        assertThat(diskLogs.size()).isGreaterThan(0);
    }

    @DisplayName("로그 조회 - fk")
    @Test
    void t01_getDiskLog_ByFk() {
        Long diskId = 2L;
        List<DiskLog> diskLogList = diskLogService.getDiskLogsById(diskId);
        List<Long> diskLogIds = diskLogList.stream()
                .map(DiskLog::getDiskDiskLogFk)
                .toList();
        assertThat(diskLogList.get(0).getDiskUsage()).isEqualTo(11.2);
        assertThat(diskLogIds).containsAll(Collections.singleton(diskId));
    }

    @DisplayName("로그 조회 - fk 없음")
    @Test
    void t02_getDiskLog_NullFk() {
        Long diskId = null;
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> diskLogService.getDiskLogsById(diskId));
    }

    @DisplayName("로그 조회 - 로그 없음")
    @Test
    void t03_getDiskLog_NoFk() {
        Long diskId = 22L;
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> diskLogService.getDiskLogsById(diskId));
    }

    @DisplayName("로그 조회 - recent")
    @Test
    void t04_getDiskLog_Recent() {
        Long diskId = 2L;
        DiskLog diskLog = diskLogService.getDiskLogRecent(diskId);
        assertThat(diskLog.getDiskUsage()).isEqualTo(11.7);
    }

    @DisplayName("로그 조회 - recent notExistId")
    @Test
    void t05_getDiskLog_NotExistId() {
        Long diskId = 22L;
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(()-> diskLogService.getDiskLogsById(diskId));
    }

    @DisplayName("로그 조회 - recent nullId")
    @Test
    void t06_getDiskLog_NullId() {
        Long diskId = null;
        assertThatExceptionOfType(NoSuchElementException.class)
        .isThrownBy(()-> diskLogService.getDiskLogsById(diskId));
    }

    @DisplayName("로그 등록")
    @Test
    void t07_insertDiskLog() {
        DiskLog diskLog = new DiskLog.Builder()
                .diskUsage(88.8)
                .diskDiskLogFk(2L)
                .build();
        int result = diskLogService.insertDiskLog(diskLog);
        assertThat(result).isEqualTo(1);
        assertThat(
                diskLogService.getDiskLogsById(2L)
                        .stream()
                        .map(DiskLog::getDiskDiskLogFk)
                        .toList())
                .containsAll(Collections.singleton(2L));
    }

    @DisplayName("로그 등록 - nullFk, nullUsage")
    @Test
    void t08_insertDiskLog_NoFk() {
        DiskLog diskLog = new DiskLog.Builder()
                .diskUsage(88.8)
                .diskDiskLogFk(null)
                .build();
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->diskLogService.insertDiskLog(diskLog));

        diskLog.setDiskUsage(null);
        diskLog.setDiskDiskLogFk(3L);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->diskLogService.insertDiskLog(diskLog));
    }

    @DisplayName("로그 삭제")
    @Test
    void t09_deleteDiskLog() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMinutes = now.minusMinutes(1);
        int result = diskLogService.deleteDiskLogBeforeTime(oneMinutes);
        assertThat(result).isGreaterThan(1);
    }

    @DisplayName("로그 삭제 - nullTime")
    @Test
    void t10_deleteDiskLog_NullTime() {
        LocalDateTime now = null;
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->diskLogService.deleteDiskLogBeforeTime(now));
    }

    @DisplayName("로그 삭제 - 삭제된 로그 없음")
    @Test
    void t11_deleteDiskLog_noDellte() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMinutes = now.minusMinutes(3);
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(()->diskLogService.deleteDiskLogBeforeTime(oneMinutes));
    }
}
