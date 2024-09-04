package com.park.monitoring.dto;

import com.park.monitoring.model.MetricLog;

import java.util.List;

public class ServerHistoryDto {
    private Integer serverId;
    private Long memoryTotal;
    private String serverIp;
    private MetricLog metricLog;

    // Default constructor
    public ServerHistoryDto() {
    }

    // Parameterized constructor
    public ServerHistoryDto(Integer serverId, Long memoryTotal, String serverIp, MetricLog metricLog) {
        this.serverId = serverId;
        this.memoryTotal = memoryTotal;
        this.serverIp = serverIp;
        this.metricLog = metricLog;
    }

    // Getters and Setters
    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public Long getMemoryTotal() {
        return memoryTotal;
    }

    public void setMemoryTotal(Long memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public MetricLog getMetricLogs() {
        return metricLog;
    }

    public void setMetricLogs(MetricLog metricLogs) {
        this.metricLog = metricLogs;
    }

    // Builder Pattern
    public static class Builder {
        private Integer serverId;
        private Long memoryTotal;
        private String serverIp;
        private MetricLog metricLog;

        public Builder serverId(Integer serverId) {
            this.serverId = serverId;
            return this;
        }

        public Builder memoryTotal(Long memoryTotal) {
            this.memoryTotal = memoryTotal;
            return this;
        }

        public Builder serverIp(String serverIp) {
            this.serverIp = serverIp;
            return this;
        }

        public Builder metricLogs(MetricLog metricLog) {
            this.metricLog = metricLog;
            return this;
        }

        public ServerHistoryDto build() {
            return new ServerHistoryDto(serverId, memoryTotal, serverIp, metricLog);
        }
    }

    @Override
    public String toString() {
        return "ServerHistoryDto{" +
                "serverId=" + serverId +
                ", memoryTotal=" + memoryTotal +
                ", serverIp='" + serverIp + '\'' +
                ", metricLog=" + metricLog +
                '}';
    }
}