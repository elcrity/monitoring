package com.park.monitoring.service;

import com.park.monitoring.mapper.MetricLogMapper;
import com.park.monitoring.model.MetricLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MetricLogService {

    private static final Logger log = LoggerFactory.getLogger(MetricLogService.class);
    MetricLogMapper metricLogMapper;

    public MetricLogService(MetricLogMapper metricLogMapper) {
        this.metricLogMapper = metricLogMapper;
    }

    public List<MetricLog> findMetricLogAll() {
        List<MetricLog> metricLogs = metricLogMapper.selectAll();
        if (metricLogs.isEmpty()) throw new NoSuchElementException("로그를 불러오지 못했습니다.");
        return metricLogs;
    }

    public List<MetricLog> findMetricLogAllByServerId(Integer serverId) {
        if (serverId == null) {
            throw new IllegalArgumentException("입력받은 서버의 id가 null입니다.");
        }
        List<MetricLog> metricLogs = metricLogMapper.selectLogAllByServerId(serverId);
        if (metricLogs.isEmpty()) {
            throw new NoSuchElementException("해당하는 로그가 존재하지 않습니다");
        }
        return metricLogs;
    }

    public List<MetricLog> findMetricLogByLatest() {
        List<MetricLog> metricLogs = metricLogMapper.selectLogAllByLatest();
        if(metricLogs == null){
            throw new NoSuchElementException("로그가 존재하지 않습니다");
        }
        return metricLogs;
    }

    public List<MetricLog> findMetricLogAtHistory(Integer serverId){
        if(serverId == null) throw new IllegalArgumentException("입력받은 서버의 id가 null입니다.");
        List<MetricLog> metricLog = metricLogMapper.selectLogHistory(serverId);
        if(metricLog.isEmpty()) throw new NoSuchElementException("없는 서버입니다");
        return metricLog;

    }

    @Transactional
    public int insertMetricLog(MetricLog metricLog) {
        if (metricLog.getMemoryUsage() == null || metricLog.getCpuUsage() == null
                || metricLog.getDiskName1() == null || metricLog.getDiskTotal1() == null
                || metricLog.getDiskUsage1() == null){
            throw new IllegalArgumentException("필수 값이 누락되었습니다.");
        } else if (metricLog.getServerId() == null) {
            throw new DataIntegrityViolationException("참조 키값이 null입니다.");
        }
        int result = metricLogMapper.insertLog(metricLog);
        if (result < 1) throw new RuntimeException("로그 등록 실패");
        return result;
    }

    @Transactional
    public int deleteMetricLogBeforeTime(LocalDateTime time) {
        if (time == null) throw new IllegalArgumentException("입력받은 시간이 비정상입니다.");
        int result = metricLogMapper.deleteLogBeforeTime(time);
        if(result <1) throw new RuntimeException("삭제 실패");
        return result;
    }
}
