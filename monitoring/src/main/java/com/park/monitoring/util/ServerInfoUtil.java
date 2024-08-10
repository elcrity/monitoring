package com.park.monitoring.util;

import com.park.monitoring.dto.DiskInfo;
import com.sun.management.OperatingSystemMXBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class ServerInfoUtil {


    public static String getServerOs() {
        return System.getProperty("os.name").toLowerCase();
    }

    public static String getServerHostname() {
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
        return hostname;
    }

    public static long getFreeMemory(OperatingSystemMXBean osBean){
        return osBean.getFreeMemorySize();
    }

    public static long getTotalMemory(OperatingSystemMXBean osBean){
        return osBean.getTotalMemorySize();
    }

    public static double getUsageMemoryP(long totalMemory, long freeMemory){
        return Double.parseDouble(String.format("%.2f", ((double) (totalMemory - freeMemory) / totalMemory) * 100));
    }


    public static String getServerIp(String os) {
        String hostAddr = "";
        if (os.contains("linux")) {
            try {
                Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces();
                while (niEnum.hasMoreElements()) {
                    NetworkInterface ni = niEnum.nextElement();
                    if (ni.getDisplayName().contains("virbr")) continue;
                    Enumeration<InetAddress> kk = ni.getInetAddresses();
                    while (kk.hasMoreElements()) {
                        InetAddress inetAddress = kk.nextElement();
                        if (!inetAddress.isLoopbackAddress() &&
                                !inetAddress.isLinkLocalAddress() &&
                                inetAddress.isSiteLocalAddress()) {
                            hostAddr = inetAddress.getHostAddress();
                            return inetAddress.getHostAddress();
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
                return local.getHostAddress();
            } catch (UnknownHostException e) {
                throw new RuntimeException("예상치 못한 에러 : ", e);
            }
        }
        return "";
    }

    public static DiskInfo getDiskInfo() {
        List<File> diskData = Arrays.asList(File.listRoots());
        List<Double> diskTotalData = new ArrayList<>();
        List<Double> diskUsageData = new ArrayList<>();
        List<String> diskName = new ArrayList<>();

        for (File disk : diskData) {
            double diskTotal = disk.getTotalSpace();
            double usagePercentage = ((double) (diskTotal - disk.getFreeSpace()) / diskTotal) * 100;

            diskTotalData.add(diskTotal / Math.pow(1024.0, 2));  // MB 단위로 변환
            diskUsageData.add(Double.valueOf(String.format("%.2f", usagePercentage)));
            diskName.add(disk.getAbsolutePath());
        }

        return new DiskInfo(diskTotalData, diskUsageData, diskName);
    }
}