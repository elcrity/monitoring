package com.park.monitoring.util;

import com.sun.management.OperatingSystemMXBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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

    public static Map<String, Object> getServerMemory() {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        long sysFreeMemory = (osBean.getFreeMemorySize());
        long sysTotalMemory = (osBean.getTotalMemorySize());
        long sysUsedMemory = sysTotalMemory - sysFreeMemory;
        double usedMemoryPercentage = Double.parseDouble(String.format("%.2f", ((double) sysUsedMemory / sysTotalMemory) * 100));

        Map<String, Object> memoryInfoMap = new HashMap<>();
        addMemoryInfo(memoryInfoMap, "freeMemory", sysFreeMemory);
        addMemoryInfo(memoryInfoMap, "totalMemory", sysTotalMemory);
        addMemoryInfo(memoryInfoMap, "usedMemoryPercentage", usedMemoryPercentage);
        return memoryInfoMap;
    }

    private static void addMemoryInfo(Map<String, Object> map, String key, Object value) {
        if (value instanceof Long) {
            map.put(key, value);
        } else if (value instanceof Double) {
            try {
                map.put(key, value);
            } catch (NumberFormatException e) {
                System.err.println("포맷이 맞지 않음 " + key + ": " + value);
            }
        }
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
}
