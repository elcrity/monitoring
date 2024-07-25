package com.park.monitoring.service;

import com.park.monitoring.mapper.DiskLogMapper;
import com.park.monitoring.model.DiskLog;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DiskLogService {

    DiskLogMapper diskLogMapper;

    public DiskLogService(DiskLogMapper diskLogMapper) {
        this.diskLogMapper = diskLogMapper;
    }

    public List<DiskLog> getAllDiskLogs() {
        return diskLogMapper.selectAllDiskLogs();
    }

    public List<DiskLog> getDiskLogsById(Long diskId) {
        if(diskId == null) throw new NoSuchElementException("찾으려 하는 디스크 id를 확인해 주세요");
        List<DiskLog> diskLogs = diskLogMapper.selectDiskLogsByFk(diskId);
        if(diskLogs.isEmpty()) throw new NoSuchElementException("찾으려 하는 로그가 없습니다. id를 확인해주세요");
        return diskLogs;
    }

    public DiskLog getDiskLogRecent(Long diskId) {
        if(diskId == null) throw new NoSuchElementException("찾으려 하는 디스크 id를 확인해 주세요");
        DiskLog diskLog = diskLogMapper.selectDiskLogRecent(diskId);
        if(diskLog == null) throw new NoSuchElementException("찾으려 하는 로그가 없습니다. id를 확인해주세요");
        return diskLog;
    }

    public int insertDiskLog(DiskLog diskLog) {
        if(diskLog.getDiskDiskLogFk() == null || diskLog.getDiskUsage() == null)
            throw new IllegalArgumentException("필수 요소 누락, 입력값 확인 필요");
        int result = diskLogMapper.insertDiskLog(diskLog);
        if(result==0) throw new NoSuchElementException("로그 등록에 실패 했습니다.");
        return result;
    }

    public int deleteDiskLogBeforeTime(LocalDateTime time) {
        if(time == null) throw new IllegalArgumentException("입력된 시간 확인 필요");
        int result = diskLogMapper.deleteDiskLogBeforeTime(time);
        if(result==0) throw new NoSuchElementException("삭제된 로그가 없습니다. 삭제에 실패했습니다.");
        return result;
    }

}
