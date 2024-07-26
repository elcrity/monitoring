package com.park.monitoring.service;

import com.park.monitoring.mapper.MetricLogMapper;
import com.park.monitoring.model.MetricLog;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MetricLogService {

    MetricLogMapper metricLogMapper;

    public MetricLogService(MetricLogMapper metricLogMapper) {
        this.metricLogMapper = metricLogMapper;
    }

    public List<MetricLog> getMetricLogAll() {
        List<MetricLog> metricLogs = metricLogMapper.selectAll();
        if (metricLogs.isEmpty()) throw new NoSuchElementException("로그를 불러오지 못했습니다.");
        return metricLogs;
    }

    public List<MetricLog> getMetricLogAllByServerId(Long serverId) {
        if (serverId == null) {
            throw new IllegalArgumentException("서버의 id값이 null입니다.");
        }
        List<MetricLog> metricLogs = metricLogMapper.selectLogAllByServerId(serverId);
        if (metricLogs.isEmpty()) {
            throw new NoSuchElementException("해당하는 서버가 존재하지 않습니다");
        }
        return metricLogs;
    }

    public MetricLog getMetricLogRecent(Long serverId) {
        if (serverId == null) {
            throw new IllegalArgumentException("서버의 id값이 null입니다.");
        }
        MetricLog metricLog = metricLogMapper.selectRecentLog(serverId);
        if (metricLog == null) {
            throw new NoSuchElementException("해당하는 서버가 존재하지 않습니다");
        }
        return metricLog;
    }

    public int insertMetricLog(MetricLog metricLog) {
        if (metricLog.getMemoryUsage() == null || metricLog.getCpuUsage() == null) {
            throw new IllegalArgumentException("메모리, 혹은 cpu 점유율 필드가 누락되었습니다.");
        } else if (metricLog.getServerMetricFk() == null) {
            throw new DataIntegrityViolationException("필수 필드 누락");
        }
        int result = metricLogMapper.insertLog(metricLog);
        if (result == 0) throw new NoSuchElementException("로그 등록 실패");
        return result;
    }

    public int deleteMetricLogBeforeTime(LocalDateTime time) {
        if (time == null) throw new IllegalArgumentException("정확한 시간을 입력해주세요");
        int result = metricLogMapper.deleteLogBeforeTime(time);
        return result;
    }
}
