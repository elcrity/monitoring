package com.park.monitoring.service;

import com.park.monitoring.dto.DiskInfo;
import com.park.monitoring.mapper.MetricLogMapper;
import com.park.monitoring.mapper.ServerInfoMapper;
import com.park.monitoring.model.MetricLog;
import com.park.monitoring.model.ServerInfo;
import com.park.monitoring.util.ServerInfoUtil;
import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MetricLogService {

    private static final Logger log = LoggerFactory.getLogger(MetricLogService.class);
    private final ServerInfoMapper serverInfoMapper;
    MetricLogMapper metricLogMapper;

    private final OperatingSystemMXBean osBean;

    public MetricLogService(MetricLogMapper metricLogMapper, ServerInfoMapper serverInfoMapper) {
        this.metricLogMapper = metricLogMapper;
        this.serverInfoMapper = serverInfoMapper;

        OperatingSystemMXBean tempOsBean = null;
        while (tempOsBean == null) {
            tempOsBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        }
        this.osBean = tempOsBean;
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
        if (metricLogs.isEmpty()||metricLogs == null) {
            throw new NoSuchElementException("해당하는 로그가 존재하지 않습니다");
        }
        return metricLogs;
    }

    public List<MetricLog> findMetricLogByLatest() {
        List<MetricLog> metricLogs = metricLogMapper.selectLogAllByLatest();
        if (metricLogs.isEmpty()) {
            throw new NoSuchElementException("로그가 존재하지 않습니다");
        }
        return metricLogs;
    }

    public List<MetricLog> findMetricLogAtHistory(Integer serverId) {
        if (serverId == null) throw new IllegalArgumentException("입력받은 서버의 id가 null입니다.");
        List<MetricLog> metricLogs = metricLogMapper.selectLogHistory(serverId);
        if (metricLogs.isEmpty() ||metricLogs == null) throw new NoSuchElementException("없는 서버입니다");
        return metricLogs;

    }

    @Transactional
    public int insertMetricLog(String serverIp) {
        if(serverIp == null) throw new IllegalArgumentException("참조할 값이 null입니다.");
        Integer serverInfo = serverInfoMapper.findServerIdByIp(serverIp);
        if(serverInfo == null) throw new NoSuchElementException("해당하는 서버가 없습니다.");

        double cpuUsage = Double.parseDouble(String.format("%.2f", osBean.getCpuLoad() * 100));
        long sysFreeMemory = ServerInfoUtil.getFreeMemory(osBean);
        long sysTotalMemory = ServerInfoUtil.getTotalMemory(osBean);
        double memoryUsage = ServerInfoUtil.getUsageMemoryP(sysTotalMemory, sysFreeMemory);
        DiskInfo diskInfo = ServerInfoUtil.getDiskInfo();

        MetricLog.Builder builder = new MetricLog.Builder()
                .cpuUsage(cpuUsage)
                .memoryUsage(memoryUsage)
                .serverMetricFk(serverInfo);

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
        Integer serverId = serverInfoMapper.findServerIdByIp(serverIp);
        if(serverId == null) throw new NoSuchElementException("해당되는 데이터가 없습니다.");
        return serverId;
    }
}
