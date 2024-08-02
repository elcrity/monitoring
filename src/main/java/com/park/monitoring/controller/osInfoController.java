package com.park.monitoring.controller;

import com.park.monitoring.CPUStats;
import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


@RequestMapping("/os")
@Controller
public class osInfoController {
    private static final Logger log = LoggerFactory.getLogger(osInfoController.class);
    private CPUStats cpuStats = null;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static ScheduledFuture<?> scheduledFuture;

    @GetMapping()
    public String infoTest() {
        scheduledFuture = scheduler.scheduleAtFixedRate(this::executeTasks, 0, 5, TimeUnit.SECONDS);
        return "os";
    }

    private void executeTasks() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            log.info("---------------------------");
            getDiskUsage();
            getMemory();
            getServerIp();

            if (os.contains("windows")) {
                getCPUProcess();
            } else if (os.contains("linux")) {
                if (cpuStats == null) {
                    BufferedReader reader = new BufferedReader(new FileReader("/proc/stat"));
                    String startLine = reader.readLine();
                    String[] fields = startLine.split("\\s+");
                    cpuStats = new CPUStats(fields);
                    Thread.sleep(10);
                }
                cpuStats = cpuUsageLinux(cpuStats);
            }
        } catch (IOException | InterruptedException | RuntimeException e) {
            log.error("Exception in task execution: {}", e.toString());
        }
    }

    public static void getDiskUsage() {
        //linux df -h 사용시, 장치명, 할당 용량, 사용 용량, 사용 가능 용량, 사용률, 마운트포인트(디렉토리) 표시
        File[] roots = File.listRoots();

        for (File root : roots) {
            try {
                root = new File("/");
                long diskTotal = root.getTotalSpace();
                //getFreeSpace와 비슷하지만 JVM이 사용 가능한 용량을 byte단위로
//            long diskUsable = root.getUsableSpace();
                //File이 속한 파티션의 사용 가능 용량을 byte 단위로
                long diskFree = root.getFreeSpace();
                long diskUsed = diskTotal - diskFree;
                String diskUsedRate = String.format("%.2f", ((double) diskUsed / diskTotal) * 100);
                String diskName = root.getName();

                log.info("Disk Name : {}", diskName);
                log.info("Disk Total : {}GB", diskTotal / (1024 * 1024 * 1024));
                log.info("Disk Free : {}GB", diskFree / (1024 * 1024 * 1024));
                log.info("Disk Usage : {}%", (diskUsedRate));
            } catch (Exception e) {
                log.error("DISK Usage 에러 {}", e.toString());
            }
        }
    }

    private void getServerIp() {
        String hostAddr = "";
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("linux")) {
            try {
                Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces();
                while (niEnum.hasMoreElements()) {
                    NetworkInterface ni = niEnum.nextElement();
                    if(ni.getDisplayName().contains("virbr")) continue;
                    Enumeration<InetAddress> kk = ni.getInetAddresses();
                    while (kk.hasMoreElements()) {
                        InetAddress inetAddress = kk.nextElement();
                        if (!inetAddress.isLoopbackAddress() &&
                                !inetAddress.isLinkLocalAddress() &&
                                inetAddress.isSiteLocalAddress()) {
                            hostAddr = inetAddress.getHostAddress();
                            log.info("IP hostaddress : {}", inetAddress.getHostAddress());

                        }

                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
        } else {
            InetAddress local = null;
            try {
                local = InetAddress.getLocalHost();
                log.info("WindowIP hostaddress : {}", local.getHostAddress());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
//        try {
//            Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces();
//            while (niEnum.hasMoreElements()) {
//                NetworkInterface ni = niEnum.nextElement();
//
//                Enumeration<InetAddress> kk= ni.getInetAddresses();
//                while (kk.hasMoreElements()) {
//                    InetAddress inetAddress = kk.nextElement();
//                    if (!inetAddress.isLoopbackAddress() &&
//                            !inetAddress.isLinkLocalAddress() &&
//                            inetAddress.isSiteLocalAddress()) {
//                        hostAddr = inetAddress.getHostAddress();
//                        log.info("IP hostaddress : {}", inetAddress.getHostAddress());
//
//                    }
//
//                }
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }

    }

    public static CPUStats cpuUsageLinux(CPUStats inputCpuStats) {
        BufferedReader reader = null;
        CPUStats cpuStats = new CPUStats();
        CPUStats prevCpuStats = new CPUStats();
        prevCpuStats.move(inputCpuStats);

        try {
            String newLine;
            reader = new BufferedReader(new FileReader("/proc/stat"));

            // 첫 번째 줄을 읽어서 CPU 정보를 가져옴
            if ((newLine = reader.readLine()) != null) {
                String[] fields = newLine.split("\\s+");
                // 필요한 값 추출 (
                cpuStats = new CPUStats(fields);

                // 이전 값 업데이트
                long cpuTime = cpuStats.getTotalTime();
                long prevCpuTime = prevCpuStats.getTotalTime();
                // 이전 CPU 사용 시간과 비교
                long cpuTimeDiff = cpuTime - prevCpuTime;
                long idleTimeDiff = cpuStats.getIdle() - prevCpuStats.getIdle();
                // CPU 사용률 계산
                // (총 사용시간 변화량 - 유휴 시간 변화량)/총 사용시간 변화량
                double cpuUsage = ((double) (cpuTimeDiff - idleTimeDiff) / (cpuTimeDiff));
                String percentCpuUsage = String.format("%.2f", cpuUsage * 100);
                log.info("Used CPU Time : {}", cpuTimeDiff);
                log.info("Idle CPU Time : {}", idleTimeDiff);
                log.info("CPU Usage%: {}%", percentCpuUsage);
            }
        } catch (IOException e) {
            log.error("CPU Usage Linux I/O에러 {}", e.toString());
        } finally {
            // BufferedReader 닫기
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("CPU Usage Linux finally I/O에러 {}", e.toString());
                }
            }
        }
        return cpuStats;
    }


    public static void getCPUProcess() {
        BufferedReader buffReader = null;
        Process p = null;
        String hostname;
        try {
            p = Runtime.getRuntime().exec("hostname");
            buffReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            hostname = buffReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        System.out.println("=================osBean" + osBean);
        String cpuArch = osBean.getArch();
        String cpuName = osBean.getName();
        double ProcessLoad = osBean.getProcessCpuLoad();
        String cpuVersion = osBean.getVersion();
        int availableProcessors = osBean.getAvailableProcessors();
        String cpuUsage = String.format("%.2f", osBean.getCpuLoad() * 100);
        log.info("Cpu Info : {}.{}.{}.{}", cpuName, cpuVersion, cpuArch, hostname);
        log.info("Process Load : {}% ", String.format("%.2f", ProcessLoad * 100));
        log.info("Cpu Usage : {}%", cpuUsage);
    }

    public static void getMemory() {
//        System 메모리
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long sysFreeMemory = (osBean.getFreeMemorySize());
        long sysTotalMemory = (osBean.getTotalMemorySize());
        long sysUsedMemory = sysTotalMemory - sysFreeMemory;
        String sysSharedMemory = String.format("%.2f", ((double) sysUsedMemory / sysTotalMemory) * 100);

        log.info("System Free Memory : {}MB ", sysFreeMemory / (1024 * 1024));
        log.info("System Total Memory : {}MB ", sysTotalMemory / (1024 * 1024));
        log.info("System Used Memory : {}% ", sysSharedMemory);

        //Runtime 메모리
//        long totalMemory = Runtime.getRuntime().totalMemory();
//        long totalMemoryMB = totalMemory / (1024 * 1024);
//        long usedMemory = totalMemory - Runtime.getRuntime().freeMemory();
//        long usedMemoryMB = usedMemory / (1024 * 1024);
//        long maxMemory = Runtime.getRuntime().maxMemory();

//        log.info("---Runtime totalMemoryMB---- {}MB ", totalMemory/ (1024 * 1024));
//        log.info("---Runtime usedMemoryMB---- {}MB ", usedMemory/ (1024 * 1024));
//        log.info("---Runtime maxMemory()---- {}MB ", maxMemory/ (1024 * 1024));
//        log.info("---memoryBean.getObjectName();---- {} ", memoryBean.getObjectName());
        //System 메모리

    }


}
