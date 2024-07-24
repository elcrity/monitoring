package com.park.monitoring.controller;

import com.park.monitoring.model.MetricLog;
import com.park.monitoring.service.MetricLogService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/metric")
public class MetricLogController {

    MetricLogService metricLogService;

    public MetricLogController(MetricLogService metricLogService) {
        this.metricLogService = metricLogService;
    }

    @PostMapping("/{id}")
    List<MetricLog> getMetricLog(@PathVariable Long id) {
        List<MetricLog> metricLogList = metricLogService.getMetricLogAllByServerId(id);
        return metricLogList;
    }
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        return e.getMessage();
    }
}
