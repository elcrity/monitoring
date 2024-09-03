package com.park.monitoring.dto;

public class ServerMetricDto {
    private Integer serverId;
    private String serverHostname;
    private String serverIp;
    private Double cpuUsage;
    private Double memoryUsage;
    private Double diskUsage1;
    private Double diskUsage2;
    private Double diskUsage3;
    private Double diskUsage4;

    // Getters and Setters
    public Integer getServerId() { return serverId; }
    public void setServerId(Integer serverId) { this.serverId = serverId; }

    public String getServerHostname() { return serverHostname; }
    public void setServerHostname(String serverHostname) { this.serverHostname = serverHostname; }

    public String getServerIp() { return serverIp; }
    public void setServerIp(String serverIp) { this.serverIp = serverIp; }

    public Double getCpuUsage() { return cpuUsage; }
    public void setCpuUsage(Double cpuUsage) { this.cpuUsage = cpuUsage; }

    public Double getMemoryUsage() { return memoryUsage; }
    public void setMemoryUsage(Double memoryUsage) { this.memoryUsage = memoryUsage; }

    public Double getDiskUsage1() { return diskUsage1; }
    public void setDiskUsage1(Double diskUsage1) { this.diskUsage1 = diskUsage1; }

    public Double getDiskUsage2() { return diskUsage2; }
    public void setDiskUsage2(Double diskUsage2) { this.diskUsage2 = diskUsage2; }

    public Double getDiskUsage3() { return diskUsage3; }
    public void setDiskUsage3(Double diskUsage3) { this.diskUsage3 = diskUsage3; }

    public Double getDiskUsage4() { return diskUsage4; }
    public void setDiskUsage4(Double diskUsage4) { this.diskUsage4 = diskUsage4; }

    // Builder Class
    public static class Builder {
        private Integer serverId;
        private String serverHostname;
        private String serverIp;
        private Double cpuUsage;
        private Double memoryUsage;
        private Double diskUsage1;
        private Double diskUsage2;
        private Double diskUsage3;
        private Double diskUsage4;

        public Builder serverId(Integer serverId) {
            this.serverId = serverId;
            return this;
        }

        public Builder serverHostname(String serverHostname) {
            this.serverHostname = serverHostname;
            return this;
        }

        public Builder serverIp(String serverIp) {
            this.serverIp = serverIp;
            return this;
        }

        public Builder cpuUsage(Double cpuUsage) {
            this.cpuUsage = cpuUsage;
            return this;
        }

        public Builder memoryUsage(Double memoryUsage) {
            this.memoryUsage = memoryUsage;
            return this;
        }

        public Builder diskUsage1(Double diskUsage1) {
            this.diskUsage1 = diskUsage1;
            return this;
        }

        public Builder diskUsage2(Double diskUsage2) {
            this.diskUsage2 = diskUsage2;
            return this;
        }

        public Builder diskUsage3(Double diskUsage3) {
            this.diskUsage3 = diskUsage3;
            return this;
        }

        public Builder diskUsage4(Double diskUsage4) {
            this.diskUsage4 = diskUsage4;
            return this;
        }

        public ServerMetricDto build() {
            ServerMetricDto dto = new ServerMetricDto();
            dto.serverId = this.serverId;
            dto.serverHostname = this.serverHostname;
            dto.serverIp = this.serverIp;
            dto.cpuUsage = this.cpuUsage;
            dto.memoryUsage = this.memoryUsage;
            dto.diskUsage1 = this.diskUsage1;
            dto.diskUsage2 = this.diskUsage2;
            dto.diskUsage3 = this.diskUsage3;
            dto.diskUsage4 = this.diskUsage4;
            return dto;
        }
    }

    @Override
    public String toString() {
        return "ServerMetricDto{" +
                "serverId=" + serverId +
                ", serverHostname='" + serverHostname + '\'' +
                ", serverIp='" + serverIp + '\'' +
                ", cpuUsage=" + cpuUsage +
                ", memoryUsage=" + memoryUsage +
                ", diskUsage1=" + diskUsage1 +
                ", diskUsage2=" + diskUsage2 +
                ", diskUsage3=" + diskUsage3 +
                ", diskUsage4=" + diskUsage4 +
                '}';
    }
}