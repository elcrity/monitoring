package com.park.monitoring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.park.monitoring.config.error.ErrorCode;
import com.park.monitoring.config.error.Exception.DataIntegrityException;
import com.park.monitoring.config.error.Exception.NotFoundException;
import com.park.monitoring.dto.HistoryRequest;
import com.park.monitoring.dto.ServerHistoryDto;
import com.park.monitoring.dto.ServerMetricDto;
import com.park.monitoring.model.MetricLog;
import com.park.monitoring.model.ServerInfo;
import com.park.monitoring.service.MetricLogService;
import com.park.monitoring.service.ServerInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class ThymeleafController {

    private static final Logger log = LoggerFactory.getLogger(ThymeleafController.class);
    ServerInfoService serverInfoService;
    MetricLogService metricLogService;

    ThymeleafController(ServerInfoService serverInfoService, MetricLogService metricLogService) {
        this.serverInfoService = serverInfoService;
        this.metricLogService = metricLogService;
    }

    @GetMapping
    public String index(Model model) {
//        List<ServerInfo> serverInfoList = serverInfoService.findAllServerInfo();
//        model.addAttribute("servers", serverInfoList);
        return "index";
    }

    @GetMapping("getServer")
    public String server(Model model) {
        // 서버 정보와 메트릭 로그를 가져옵니다.
        List<ServerInfo> serverInfoList = serverInfoService.findAllServerInfo();
        List<MetricLog> latestLog = metricLogService.findMetricLogByLatest();
        // 메트릭 로그를 서버 ID를 기준으로 맵으로 변환합니다.
        Map<Integer, MetricLog> metricMap = latestLog.stream()
                .collect(Collectors.toMap(MetricLog::getServerId, metric -> metric));
        // ServerMetricDto 리스트를 생성합니다.
        List<ServerMetricDto> serverMetricDtos = new ArrayList<>();
        for (ServerInfo server : serverInfoList) {
            MetricLog metric = metricMap.get(server.getServerId());
            // ServerMetricDto를 빌더를 사용하여 생성합니다.
            ServerMetricDto dto = new ServerMetricDto.Builder()
                    .serverId(server.getServerId())
                    .serverHostname(server.getServerHostname())
                    .serverIp(server.getServerIp())
                    .cpuUsage(metric != null ? metric.getCpuUsage() : null)
                    .memoryUsage(metric != null ? metric.getMemoryUsage() : null)
                    .diskUsage1(metric != null ? metric.getDiskUsage1() : null)
                    .diskUsage2(metric != null ? metric.getDiskUsage2() : null)
                    .diskUsage3(metric != null ? metric.getDiskUsage3() : null)
                    .diskUsage4(metric != null ? metric.getDiskUsage4() : null)
                    .build();
            serverMetricDtos.add(dto);
        }
        // DTO 리스트를 모델에 추가합니다.
        model.addAttribute("serverMetrics", serverMetricDtos);
        // Thymeleaf 템플릿을 반환합니다.
        return "index";
    }

    @GetMapping("getLogs/{serverId}")
    public String getLog(Model model,
                               @PathVariable(required = false) Integer serverId,
                               @RequestParam(defaultValue = "false") boolean isRepeat) {
        ServerInfo serverInfo = serverInfoService.findServerInfoById(serverId);
        List<MetricLog> logHistory = metricLogService.findMetricLogByLatest();

        MetricLog filteredLog = new MetricLog();
        for (MetricLog log : logHistory) {
            if (serverInfo.getServerId().equals(log.getServerId())) {
                filteredLog = log; // Return the matching MetricLog
            }
        }

        ServerHistoryDto serverHistoryDto = new ServerHistoryDto.Builder()
                .serverId(serverInfo.getServerId())
                .serverIp(serverInfo.getServerIp())
                .memoryTotal(serverInfo.getMemoryTotal())
                .metricLogs(filteredLog)
                .build();
        model.addAttribute("serverHistory", serverHistoryDto);

        return "index :: #historyContainer";
    }

    @PostMapping("getHistory")
    @ResponseBody
    public List<MetricLog> getHistory(@RequestBody HistoryRequest request) {
        Integer serverId = request.getServerId();
        boolean isRepeat = request.isRepeat();
        LocalDateTime date = request.getDate();
        System.out.println(date);
        List<MetricLog> logHistory = metricLogService.findMetricLogAtHistory(serverId,isRepeat,date);
        return logHistory;
    }
}
