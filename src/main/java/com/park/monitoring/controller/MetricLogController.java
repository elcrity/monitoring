package com.park.monitoring.controller;

import com.park.monitoring.dto.LogRequest;
import com.park.monitoring.model.MetricLog;
import com.park.monitoring.service.MetricLogService;
import com.park.monitoring.service.ServerInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

@RestController
@RequestMapping("/log")
@Tag(name = "Log Controller", description = "로그 입력")
public class MetricLogController {

    MetricLogService metricLogService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static ScheduledFuture<?> scheduledFuture;

    public MetricLogController(MetricLogService metricLogService, ServerInfoService serverInfoService) {
        this.metricLogService = metricLogService;
    }

//    @PostMapping()
//    ResponseEntity<List<MetricLog>> getDashboardLog() {
//        List<MetricLog> metricLogList = metricLogService.findMetricLogByLatest();
//        return ResponseEntity.ok().body(metricLogList);
//    }

//    @PostMapping({"/history/{serverId}", "/history/"})
//    ResponseEntity<List<MetricLog>> getServerLog(@PathVariable(required = false) Integer serverId, @RequestParam(defaultValue = "false") boolean isRepeat) {
//        // 서버 로그를 가져오기
//        List<MetricLog> metricLogs = metricLogService.findMetricLogAtHistory(serverId, isRepeat);
//        return ResponseEntity.ok().body(metricLogs);
//    }

    //    /insert/${serverIp} -> body : JSON{Log log}->
//    @PostMapping("/insert")
//    public ResponseEntity<Map<String, String>> startLogging(@RequestBody LogRequest logRequest) {
////        String ip = ServerInfoUtil.getServerIp(ServerInfoUtil.getServerOs());
//        Map<String, String> response = new HashMap<>();
//        metricLogService.insertMetricLog(logRequest.getLogInput(), logRequest.getDiskInfo());
//        response.put("message", logRequest.getLogInput().getServerIp() + " 로그 등록 성공");
//
//        return ResponseEntity.ok().body(response);
//    }
//
//    @GetMapping("/stop")
//    ResponseEntity<Map<String, String>> stopLogging() {
//        Map<String, String> response = new HashMap<>();
//        if (scheduledFuture != null && !scheduledFuture.isDone()) {
//            scheduledFuture.cancel(true); // true를 전달하여 현재 실행 중인 작업도 중단할 수 있음
//            response.put("message", "로그 정지");
//            return ResponseEntity.ok().body(response);
//        }
//        response.put("message", "로그 정지 오류");
//        return ResponseEntity.ok().body(response);
//    }
}
