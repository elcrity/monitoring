package com.park.monitoring.dto;

import java.time.LocalDateTime;

public class HistoryRequest {
    private Integer serverId;
    private boolean isRepeat;
    private LocalDateTime date; // 날짜 필드 추가

    // Getters and setters
    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public boolean getIsRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean isRepeat) {
        this.isRepeat = isRepeat;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "HistoryRequest{" +
                "serverId=" + serverId +
                ", isRepeat=" + isRepeat +
                ", date=" + date +
                '}';
    }
}
