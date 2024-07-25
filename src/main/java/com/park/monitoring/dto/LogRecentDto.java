package com.park.monitoring.dto;

import com.park.monitoring.model.Disk;
import com.park.monitoring.model.DiskLog;
import com.park.monitoring.model.MetricLog;

import java.util.List;

public class LogRecentDto {
    List<Disk> disks;
    Double cpuUsage;
    Double memoryUsage;

    public LogRecentDto() {
    }

    public LogRecentDto(Builder builder){
        this.disks = builder.disks;
        this.cpuUsage = builder.cpuUsage;
        this.memoryUsage = builder.memoryUsage;
    }



    public static class Builder{
        private List<Disk> disks;
        private Double cpuUsage;
        private Double memoryUsage;

        public LogRecentDto ModelToDto(MetricLog metricLog, List<Disk> disks){
            this.disks = disks;
            this.cpuUsage = metricLog.getCpuUsage();
            this.memoryUsage = metricLog.getMemoryUsage();
            return new Builder()
                    .disks(this.disks)
                    .cpuUsage(this.cpuUsage)
                    .memoryUsage(this.memoryUsage)
                    .build();
        }

        public Builder disks(List<Disk> disks){
            this.disks = disks;
            return this;
        }

        public Builder cpuUsage(Double cpuUsage){
            this.cpuUsage = cpuUsage;
            return this;
        }

        public Builder memoryUsage(Double memoryUsage){
            this.memoryUsage = memoryUsage;
            return this;
        }

        public LogRecentDto build(){
            return new LogRecentDto(this);
        }
    }

    public List<Disk> getDisks() {
        return disks;
    }

    public void setDisks(List<Disk> disks) {
        this.disks = disks;
    }

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(Double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    @Override
    public String toString() {
        return "LogDto{" +
                "disks=" + disks +
                ", cpuUsage=" + cpuUsage +
                ", memoryUsage=" + memoryUsage +
                '}';
    }
}
