package com.park.monitoring.controller;

import com.park.monitoring.model.MetricLog;
import com.park.monitoring.service.MetricLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/log")
public class MetricLogController {

    MetricLogService metricLogService;

    public MetricLogController(MetricLogService metricLogService) {
        this.metricLogService = metricLogService;
    }

    @PostMapping()
    ResponseEntity<List<MetricLog>> getDashboardLog() {
        try {
            List<MetricLog> metricLogList = metricLogService.findMetricLogByLatest();
            if(metricLogList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok().body(metricLogList);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/{serverId}")
    ResponseEntity<List<MetricLog>> getServerLog(@PathVariable Integer serverId){
        if (serverId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<MetricLog> metricLogs;
        try {
            // 서버 로그를 가져오려 시도
            metricLogs = metricLogService.findMetricLogAtHistory(serverId);
        } catch (NoSuchElementException e) {
            // 로그가 없을 경우 (서버 ID가 유효하지 않을 때)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().body(metricLogs);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        return e.getMessage();
    }
}
