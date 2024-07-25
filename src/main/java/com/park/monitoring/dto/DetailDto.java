package com.park.monitoring.dto;

import com.park.monitoring.model.Disk;
import com.park.monitoring.model.ServerInfo;

import java.util.ArrayList;
import java.util.List;

public class DetailDto {
    String ip;
    Double memoryTotal;
    List<Long> diskId;
    List<String> diskName;
    List<Long> diskTotal;



    public DetailDto(Builder builder){
        this.ip = builder.ip;
        this.memoryTotal = builder.memoryTotal;
        this.diskId = builder.diskId;
        this.diskName = builder.diskName;
        this.diskTotal = builder.diskTotal;
    }
    public static class Builder{
        private String ip;
        private Double memoryTotal;
        private List<Long> diskId;
        private List<String> diskName;
        private List<Long> diskTotal;

        public DetailDto ModelToDto(ServerInfo serverInfo, List<Disk> diskList){
            this.ip = serverInfo.getServerIp();
            this.memoryTotal = serverInfo.getMemoryTotal();
            this.diskId = new ArrayList<>();
            this.diskName = new ArrayList<>();
            this.diskTotal = new ArrayList<>();
            for(Disk disk : diskList){
                this.diskId.add(disk.getDiskId());
                this.diskName.add(disk.getDiskName());
                this.diskTotal.add(disk.getDiskTotal());
            }
            return new Builder()
                    .ip(this.ip)
                    .memoryTotal(this.memoryTotal)
                    .diskId(this.diskId)
                    .diskName(this.diskName)
                    .diskTotal(this.diskTotal)
                    .build();
        }

        public Builder ip(String ip){
            this.ip = ip;
            return this;
        }
        public Builder memoryTotal(Double memoryTotal){
            this.memoryTotal = memoryTotal;
            return this;
        }
        public Builder diskId(List<Long> diskId){
            this.diskId = diskId;
            return this;
        }
        public Builder diskName(List<String> diskName){
            this.diskName = diskName;
            return this;
        }
        public Builder diskTotal(List<Long> diskTotal){
            this.diskTotal = diskTotal;
            return this;
        }
        public DetailDto build(){
            return new DetailDto(this);
        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Double getMemoryTotal() {
        return memoryTotal;
    }

    public void setMemoryTotal(Double memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    public List<Long> getDiskId() {
        return diskId;
    }

    public void setDiskId(List<Long> diskId) {
        this.diskId = diskId;
    }

    public List<String> getDiskName() {
        return diskName;
    }

    public void setDiskName(List<String> diskName) {
        this.diskName = diskName;
    }

    public List<Long> getDiskTotal() {
        return diskTotal;
    }

    public void setDiskTotal(List<Long> diskTotal) {
        this.diskTotal = diskTotal;
    }

    @Override
    public String toString() {
        return "DetailDto{" +
                "ip='" + ip + '\'' +
                ", memoryTotal=" + memoryTotal +
                ", diskId=" + diskId +
                ", diskName=" + diskName +
                ", diskTotal=" + diskTotal +
                '}';
    }
}
