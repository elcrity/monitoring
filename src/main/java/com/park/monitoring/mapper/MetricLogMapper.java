package com.park.monitoring.mapper;

import com.park.monitoring.model.MetricLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mapper
public interface MetricLogMapper {
    List<MetricLog> selectLogAllByLatest();
    List<MetricLog> selectLogHistory(Map<String, Object> params);
    int insertLog(MetricLog metricLog);
    int deleteLogBeforeTime();

}
