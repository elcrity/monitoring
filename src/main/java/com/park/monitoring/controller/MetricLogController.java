package com.park.monitoring.controller;

import com.park.monitoring.model.MetricLog;
import com.park.monitoring.service.MetricLogService;
import com.park.monitoring.service.ServerInfoService;
import com.park.monitoring.util.ServerInfoUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/log")
public class MetricLogController {

    private final ServerInfoService serverInfoService;
    MetricLogService metricLogService;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static ScheduledFuture<?> scheduledFuture;

    public MetricLogController(MetricLogService metricLogService, ServerInfoService serverInfoService) {
        this.metricLogService = metricLogService;
        this.serverInfoService = serverInfoService;
    }

    @PostMapping()
    ResponseEntity<List<MetricLog>> getDashboardLog() {
        List<MetricLog> metricLogList = metricLogService.findMetricLogByLatest();
        if (metricLogList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok().body(metricLogList);
    }


    @PostMapping({"/history/{serverId}", "/history"})
    ResponseEntity<List<MetricLog>> getServerLog(@PathVariable(required = false) Integer serverId) {
        // 서버 로그를 가져오기
        List<MetricLog> metricLogs = metricLogService.findMetricLogAtHistory(serverId);
        return ResponseEntity.ok().body(metricLogs);
    }

    @PostMapping("/start")
    public void startLogging() {
        String ip = ServerInfoUtil.getServerIp(ServerInfoUtil.getServerOs());
        Runnable task = () -> metricLogService.insertMetricLog(ip);

        try {
            if (scheduledFuture != null && !scheduledFuture.isDone()) {
                scheduledFuture.cancel(false);
            }
            scheduledFuture = scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);
        }catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/stop")
    void stopLogging() {
        if (scheduledFuture != null && !scheduledFuture.isDone()) {
            scheduledFuture.cancel(true); // true를 전달하여 현재 실행 중인 작업도 중단할 수 있음
        }
    }

//    @ExceptionHandler(IllegalArgumentException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String handleIllegalArgumentException(IllegalArgumentException e) {
//        return e.getMessage();
//    }
}
