package com.park.monitoring.controller;

import com.park.monitoring.model.MetricLog;
import com.park.monitoring.model.ServerInfo;
import com.park.monitoring.service.MetricLogService;
import com.park.monitoring.service.ServerInfoService;
import com.park.monitoring.util.ServerInfoUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        return ResponseEntity.ok().body(metricLogList);
    }

    @PostMapping({"/history/{serverId}", "/history/"})
    ResponseEntity<List<MetricLog>> getServerLog(@PathVariable(required = false) Integer serverId) {
        // 서버 로그를 가져오기
        List<MetricLog> metricLogs = metricLogService.findMetricLogAtHistory(serverId);
        return ResponseEntity.ok().body(metricLogs);
    }

    @GetMapping("/start")
    public ResponseEntity<Map<String,String>> startLogging() {
        String ip = ServerInfoUtil.getServerIp(ServerInfoUtil.getServerOs());
        int serverId = serverInfoService.findServerIdByIp(ip);
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverId(serverId)
                .serverIp(ip)
                .build();
        Map<String,String> response = new HashMap<>();
        Runnable task = () -> metricLogService.insertMetricLog(serverInfo);

        try {
            if (scheduledFuture != null && !scheduledFuture.isDone()) {
                scheduledFuture.cancel(false);
            }
            scheduledFuture = scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);
            response.put("message", "로그 시작");
            return ResponseEntity.ok().body(response);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/stop")
    ResponseEntity<Map<String,String>> stopLogging() {
        Map<String,String> response = new HashMap<>();
        if (scheduledFuture != null && !scheduledFuture.isDone()) {
            scheduledFuture.cancel(true); // true를 전달하여 현재 실행 중인 작업도 중단할 수 있음
            response.put("message", "로그 정지");
            return ResponseEntity.ok().body(response);
        }
        response.put("message", "로그 정지 오류");
        return ResponseEntity.ok().body(response);
    }
}
