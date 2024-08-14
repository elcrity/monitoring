package com.park.monitoring.mapper;

import com.park.monitoring.model.MetricLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MetricLogMapper {

    List<MetricLog> selectLogAllByLatest();

    List<MetricLog> selectLogHistory(int serverId);

    int insertLog(MetricLog metricLog);
//    int updateLog(MetricLog metricLog);
    int deleteLogBeforeTime();


}
