package com.park.monitoring.controller;

import com.park.monitoring.dto.LogRecentDto;
import com.park.monitoring.model.Disk;
import com.park.monitoring.model.DiskLog;
import com.park.monitoring.model.MetricLog;
import com.park.monitoring.service.DiskLogService;
import com.park.monitoring.service.DiskService;
import com.park.monitoring.service.MetricLogService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/log")
public class MetricLogController {

    private final DiskLogService diskLogService;
    private final DiskService diskService;
    MetricLogService metricLogService;

    public MetricLogController(MetricLogService metricLogService, DiskLogService diskLogService, DiskService diskService) {
        this.metricLogService = metricLogService;
        this.diskLogService = diskLogService;
        this.diskService = diskService;
    }

    @PostMapping("/{id}")
    List<MetricLog> getMetricLog(@PathVariable Long id) {
        List<MetricLog> metricLogList = metricLogService.getMetricLogAllByServerId(id);
        return metricLogList;
    }

    //Todo : 실시간 로그, 각 항목에 로그를 전부 출력, 데이터 + created_date가져와야됨
    @PostMapping("/{serverId}")
    LogRecentDto getRecentLog(@PathVariable Long serverId) {
        MetricLog metricLog = metricLogService.getMetricLogRecent(serverId);
        List<Disk> disks = diskService.findAllDisksByServerId(serverId);
        LogRecentDto logRecentDto = new LogRecentDto.Builder()
                .ModelToDto(metricLog,disks);
        return logRecentDto;
    }

    @PostMapping("/detail/{ServerId}")
    List<LogDto> getServerLog(@PathVariable Long serverId){
        //cpu, memory, disk{diskusage1, diskusage2}
    }


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        return e.getMessage();
    }
}
