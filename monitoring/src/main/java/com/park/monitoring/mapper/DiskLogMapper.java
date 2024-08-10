package com.park.monitoring.mapper;

import com.park.monitoring.model.DiskLog;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface DiskLogMapper {
    List<DiskLog> selectAllDiskLogs();
    List<DiskLog> selectDiskLogsByFk(Long id);
    DiskLog selectDiskLogRecent(Long id);
    int insertDiskLog(DiskLog diskLog);
    int deleteDiskLogBeforeTime(LocalDateTime timestamp);
}
