//package com.park.monitoring;
//
//public class CPUStats {
//    // user : 유저 모드에서 사용한 시간
//    // nice : 유저모드에서 우선수위가 낮은 프로세스에 사용한 시간
//    // system : 시스템에서 사용한 시간
//    // idle : cpu유휴 시간
//    // iowait : 입출력 종료까지 대기한 시간
//    // irq : 하드웨어 인터럽트에 사용한 시간
//    // softirq : 소프트웨어 인터럽트에 사용한 시간
//    private long user;
//    private long nice;
//    private long system;
//    private long idle;
//    private long iowait;
//    private long irq;
//    private long softirq;
//
//
//    public CPUStats(long user, long nice, long system, long idle,
//                    long iowait, long irq, long softirq) {
//        this.user = user;
//        this.nice = nice;
//        this.system = system;
//        this.idle = idle;
//        this.iowait = iowait;
//        this.irq = irq;
//        this.softirq = softirq;
//    }
//
//    public CPUStats(String[] fields) {
//        this.user = Long.parseLong(fields[1]);
//        this.nice = Long.parseLong(fields[2]);
//        this.system = Long.parseLong(fields[3]);
//        this.idle = Long.parseLong(fields[4]);
//        this.iowait = Long.parseLong(fields[5]);
//        this.irq = Long.parseLong(fields[6]);
//        this.softirq = Long.parseLong(fields[7]);
//    }
//
//    public CPUStats() {
//    }
//
//    public long getUser() {
//        return user;
//    }
//
//    public void setUser(long user) {
//        this.user = user;
//    }
//
//    public long getNice() {
//        return nice;
//    }
//
//    public void setNice(long nice) {
//        this.nice = nice;
//    }
//
//    public long getSystem() {
//        return system;
//    }
//
//    public void setSystem(long system) {
//        this.system = system;
//    }
//
//    public long getIdle() {
//        return idle;
//    }
//
//    public void setIdle(long idle) {
//        this.idle = idle;
//    }
//
//    public long getIowait() {
//        return iowait;
//    }
//
//    public void setIowait(long iowait) {
//        this.iowait = iowait;
//    }
//
//    public long getIrq() {
//        return irq;
//    }
//
//    public void setIrq(long irq) {
//        this.irq = irq;
//    }
//
//    public long getSoftirq() {
//        return softirq;
//    }
//
//    public void setSoftirq(long softirq) {
//        this.softirq = softirq;
//    }
//
//    @Override
//    public String toString() {
//        return "com.park.monitoring.CPUStats{" +
//                "user=" + user +
//                ", nice=" + nice +
//                ", system=" + system +
//                ", idle=" + idle +
//                ", iowait=" + iowait +
//                ", irq=" + irq +
//                ", softirq=" + softirq +
//                '}';
//    }
//
//    public void move(CPUStats cpuStats) {
//        this.user = cpuStats.getUser();
//        this.nice = cpuStats.getNice();
//        this.system = cpuStats.getSystem();
//        this.idle = cpuStats.getIdle();
//        this.iowait = cpuStats.getIowait();
//        this.irq = cpuStats.getIrq();
//        this.softirq = cpuStats.getSoftirq();
//    }
//
//    public long getTotalTime() {
//        return user + nice + system + idle + iowait + irq + softirq;
//    }
//}
