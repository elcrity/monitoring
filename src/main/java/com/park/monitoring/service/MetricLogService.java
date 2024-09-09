package com.park.monitoring.service;

import com.park.monitoring.config.error.ErrorCode;
import com.park.monitoring.config.error.Exception.BadRequestException;
import com.park.monitoring.config.error.Exception.BaseException;
import com.park.monitoring.config.error.Exception.NoContentException;
import com.park.monitoring.config.error.Exception.NotFoundException;
import com.park.monitoring.dto.DiskInfo;
import com.park.monitoring.dto.LogInput;
import com.park.monitoring.mapper.MetricLogMapper;
import com.park.monitoring.mapper.ServerInfoMapper;
import com.park.monitoring.model.MetricLog;
import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MetricLogService {

    private static final Logger log = LoggerFactory.getLogger(MetricLogService.class);
    private final ServerInfoMapper serverInfoMapper;
    MetricLogMapper metricLogMapper;


    public MetricLogService(MetricLogMapper metricLogMapper, ServerInfoMapper serverInfoMapper) {
        this.metricLogMapper = metricLogMapper;
        this.serverInfoMapper = serverInfoMapper;
    }

    public List<MetricLog> findMetricLogByLatest() {
        List<MetricLog> metricLogs = metricLogMapper.selectLogAllByLatest();
        if (metricLogs.isEmpty()) {
            throw new NoContentException(ErrorCode.NO_CONTENT);
        }
        return metricLogs;
    }

    public List<MetricLog> findMetricLogAtHistory(Integer serverId, boolean isRepeat, LocalDateTime date) {
        if (serverId == null) throw new BadRequestException(ErrorCode.INVALID_INPUT_VALUE);
        if(date == null) date = LocalDateTime.now();
        System.out.println("date : " + date);
        LocalTime time = LocalTime.now().withSecond(0).withNano(0);
        Map<String, Object> params = new HashMap<>();
//        LocalTime currentTime = LocalTime.now();
//        System.out.println(currentTime);
        LocalDateTime startDate = date.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endDate = startDate.plusDays(1);
        if (isRepeat) {
//            date = date.with(time);
            // Todo : 시작시간은 수정된 시간으로 입력받기, 끝나는 시간은 해당 날짜 23:59까지
            startDate = date.withSecond(0);
//            endDate = date.plusMinutes(5).withSecond(0);
        }
        System.out.println(startDate);
        System.out.println(endDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 출력 결과
        String startDateStr = startDate.format(formatter);
        String endDateStr = endDate.format(formatter);
        params.put("serverId", serverId);
        params.put("startDate", startDateStr);
        params.put("endDate", endDateStr);

        List<MetricLog> metricLogs = metricLogMapper.selectLogHistory(params);
        return metricLogs;
    }


    @Transactional
    public int insertMetricLog(LogInput logInput, DiskInfo diskInfo) {
        if(logInput == null || diskInfo == null || logInput.getServerIp() == null) throw new BadRequestException(ErrorCode.INVALID_INPUT_VALUE);
        Integer serverId = serverInfoMapper.findServerIdByIp(logInput.getServerIp());
        if(serverId == null) throw new NotFoundException(ErrorCode.NOT_FOUND);
        double cpuUsage = Double.parseDouble(String.format("%.2f", logInput.getCpuUsage() * 100));
        double memoryUsage = Double.parseDouble(String.format("%.2f", ((double) logInput.getMemoryUsage() * 100)));

        MetricLog.Builder builder = new MetricLog.Builder()
                .cpuUsage(cpuUsage)
                .memoryUsage(memoryUsage)
                .serverMetricFk(serverId);

        for (int i = 0; i < diskInfo.getDiskName().size() && i < 4; i++) {
            switch (i) {
                case 0:
                    builder.diskName1(diskInfo.getDiskName().get(i))
                            .diskTotal1(diskInfo.getDiskTotalData().get(i))
                            .diskUsage1(diskInfo.getDiskUsageData().get(i));
                    break;
                case 1:
                    builder.diskName2(diskInfo.getDiskName().get(i))
                            .diskTotal2(diskInfo.getDiskTotalData().get(i))
                            .diskUsage2(diskInfo.getDiskUsageData().get(i));
                    break;
                case 2:
                    builder.diskName3(diskInfo.getDiskName().get(i))
                            .diskTotal3(diskInfo.getDiskTotalData().get(i))
                            .diskUsage3(diskInfo.getDiskUsageData().get(i));
                    break;
                case 3:
                    builder.diskName4(diskInfo.getDiskName().get(i))
                            .diskTotal4(diskInfo.getDiskTotalData().get(i))
                            .diskUsage4(diskInfo.getDiskUsageData().get(i));
                    break;
            }
        }
        MetricLog metricLog = builder.build();

        if (metricLog.getMemoryUsage() == null || metricLog.getCpuUsage() == null
                || metricLog.getDiskName1() == null || metricLog.getDiskTotal1() == null
                || metricLog.getDiskUsage1() == null) {
            throw new BadRequestException(ErrorCode.INVALID_INPUT_VALUE);
        }
        int result = metricLogMapper.insertLog(metricLog);
        if (result < 1) throw new BaseException(ErrorCode.UNEXPECTED_ERROR);
        return result;
    }


    @Transactional
    public int deleteMetricLogBeforeTime() {
        int result = metricLogMapper.deleteLogBeforeTime();
        if (result < 1) throw new BaseException(ErrorCode.UNEXPECTED_ERROR);
        return result;
    }
}
