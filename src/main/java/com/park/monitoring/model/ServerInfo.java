package com.park.monitoring.model;

import lombok.*;

@Data
@Builder
public class ServerInfo {
    long server_id;
    String server_os;
    String server_hostname;
    double memory_total;
    String purpose;
    String server_ip;
}
