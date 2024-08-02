package com.park.monitoring;

import com.sun.management.OperatingSystemMXBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.lang.management.ManagementFactory;

@SpringBootApplication
public class MonitoringApplication {
	public static void main(String[] args) {
		
		SpringApplication.run(MonitoringApplication.class, args);
	}
}
