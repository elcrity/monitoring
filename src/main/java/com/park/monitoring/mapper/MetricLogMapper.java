package com.park.monitoring.mapper;

import com.park.monitoring.model.MetricLog;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MetricLogMapper {

    List<MetricLog> selectLogAllByServerId(Long serverId);

    MetricLog selectRecentLog(Long serverId);

    int insertLog(MetricLog metricLog);
//    int updateLog(MetricLog metricLog);
    int deleteLogBeforeTime(LocalDateTime timestamp);

}
