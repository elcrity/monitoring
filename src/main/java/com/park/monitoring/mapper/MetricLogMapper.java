package com.park.monitoring.mapper;

import com.park.monitoring.dto.LogHistoryDto;
import com.park.monitoring.model.MetricLog;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MetricLogMapper {

    List<MetricLog> selectAll();

    List<MetricLog> selectLogAllByServerId(int serverId);

    List<MetricLog> selectLogAllByLatest();

    LogHistoryDto selectLogHistory(int serverId);

    int insertLog(MetricLog metricLog);
//    int updateLog(MetricLog metricLog);
    int deleteLogBeforeTime(LocalDateTime timestamp);

}
