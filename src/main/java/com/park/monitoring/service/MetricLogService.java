package com.park.monitoring.service;

import com.park.monitoring.mapper.MetricLogMapper;
import com.park.monitoring.mapper.ServerInfoMapper;
import com.park.monitoring.model.MetricLog;
import com.park.monitoring.model.ServerInfo;
import com.park.monitoring.util.ServerInfoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MetricLogService {

    private static final Logger log = LoggerFactory.getLogger(MetricLogService.class);
    private final ServerInfoMapper serverInfoMapper;
    MetricLogMapper metricLogMapper;

    public MetricLogService(MetricLogMapper metricLogMapper, ServerInfoMapper serverInfoMapper) {
        this.metricLogMapper = metricLogMapper;
        this.serverInfoMapper = serverInfoMapper;
    }

    public List<MetricLog> findMetricLogAll() {
        List<MetricLog> metricLogs = metricLogMapper.selectAll();
        if (metricLogs.isEmpty()) throw new NoSuchElementException("로그를 불러오지 못했습니다.");
        return metricLogs;
    }

    public List<MetricLog> findMetricLogAllByServerId(Integer serverId) {
        if (serverId == null) {
            throw new IllegalArgumentException("입력받은 서버의 id가 null입니다.");
        }
        List<MetricLog> metricLogs = metricLogMapper.selectLogAllByServerId(serverId);
        if (metricLogs.isEmpty()) {
            throw new NoSuchElementException("해당하는 로그가 존재하지 않습니다");
        }
        return metricLogs;
    }

    public List<MetricLog> findMetricLogByLatest() {
        List<MetricLog> metricLogs = metricLogMapper.selectLogAllByLatest();
        if (metricLogs == null) {
            throw new NoSuchElementException("로그가 존재하지 않습니다");
        }
        return metricLogs;
    }

    public List<MetricLog> findMetricLogAtHistory(Integer serverId) {
        if (serverId == null) throw new IllegalArgumentException("입력받은 서버의 id가 null입니다.");
        List<MetricLog> metricLog = metricLogMapper.selectLogHistory(serverId);
        if (metricLog.isEmpty()) throw new NoSuchElementException("없는 서버입니다");
        return metricLog;

    }

    @Transactional
    public int insertMetricLog(String serverIp) {
        if(serverIp == null) throw new DataIntegrityViolationException("참조할 서버키값이 null입니다.");
        ServerInfo serverInfo = serverInfoMapper.findServerInfoByIp(serverIp);
        double cpuUsage = (Double.parseDouble(String.format("%.2f", ServerInfoUtil.getCPUProcess().getCpuLoad()*100)));
        double memoryUsage = (double) ServerInfoUtil.getServerMemory().get("usedMemoryPercentage");
        List<File> diskData = ServerInfoUtil.getDiskUsage();
        List<Double> diskTotalData = new ArrayList<>();
        List<Double> diskUsageData = new ArrayList<>();
        List<String> diskName = new ArrayList<>();

        for (File disk : diskData) {
            double diskTotal = disk.getTotalSpace();
            double usagePercentage = ((double) (diskTotal - disk.getFreeSpace()) / diskTotal) * 100;

            diskTotalData.add(diskTotal/Math.pow(1024.0, 2));
            diskUsageData.add(Double.valueOf(String.format("%.2f", usagePercentage)));
            diskName.add(disk.getAbsolutePath());
        }


        MetricLog.Builder builder = new MetricLog.Builder()
                .cpuUsage(cpuUsage)
                .memoryUsage(memoryUsage)
                .serverMetricFk(serverInfo.getServerId());

        for (int i = 0; i < diskName.size() && i < 4; i++) {
            switch (i) {
                case 0:
                    builder.diskName1(diskName.get(i))
                            .diskTotal1(diskTotalData.get(i))
                            .diskUsage1(diskUsageData.get(i));
                    break;
                case 1:
                    builder.diskName2(diskName.get(i))
                            .diskTotal2(diskTotalData.get(i))
                            .diskUsage2(diskUsageData.get(i));
                    break;
                case 2:
                    builder.diskName3(diskName.get(i))
                            .diskTotal3(diskTotalData.get(i))
                            .diskUsage3(diskUsageData.get(i));
                    break;
                case 3:
                    builder.diskName4(diskName.get(i))
                            .diskTotal4(diskTotalData.get(i))
                            .diskUsage4(diskUsageData.get(i));
                    break;
            }
        }
        MetricLog metricLog = builder.build();


        if (metricLog.getMemoryUsage() == null || metricLog.getCpuUsage() == null
                || metricLog.getDiskName1() == null || metricLog.getDiskTotal1() == null
                || metricLog.getDiskUsage1() == null) {
            throw new IllegalArgumentException("필수 값이 누락되었습니다.");
        }
        int result = metricLogMapper.insertLog(metricLog);
        if (result < 1) throw new RuntimeException("로그 등록 실패");
        return result;
    }


    @Transactional
    public int deleteMetricLogBeforeTime() {
        int result = metricLogMapper.deleteLogBeforeTime();
        if (result < 1) throw new RuntimeException("로그 삭제에 실패했습니다.");
        return result;
    }

    public int findMetricLogByIp(String serverIp){
        if(serverIp == null) throw new IllegalArgumentException("입력받은 ip가 Null입니다.");
        ServerInfo server = serverInfoMapper.findServerInfoByIp(serverIp);
        if(server == null) throw new NoSuchElementException("해당되는 데이터가 없습니다.");
        return server.getServerId();
    }
}
