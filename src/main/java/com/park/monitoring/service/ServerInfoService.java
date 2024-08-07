package com.park.monitoring.service;

import com.park.monitoring.mapper.ServerInfoMapper;
import com.park.monitoring.model.ServerInfo;
import com.park.monitoring.util.ServerInfoUtil;
import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServerInfoService {
    Logger log = LoggerFactory.getLogger(this.getClass());

    ServerInfoMapper serverInfoMapper;
    OperatingSystemMXBean osBean;

    public ServerInfoService(ServerInfoMapper serverInfoMapper) {

        this.serverInfoMapper = serverInfoMapper;

        OperatingSystemMXBean tempOsBean = null;
        while (tempOsBean == null) {
            tempOsBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        }
        this.osBean = tempOsBean;
    }


    public List<ServerInfo> findAllServerInfo() {
        List<ServerInfo> serverInfo = serverInfoMapper.selectAllServerInfo();
        if (serverInfo == null) {
            throw new IllegalArgumentException("서버 정보를 가져올 수 없습니다.");
        }
        return serverInfo;
    }

    public ServerInfo findServerInfoById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("입력된 Id가 null입니다.");
        }
        ServerInfo serverInfo = serverInfoMapper.selectServerInfoById(id);
        if (serverInfo == null) {
            throw new NoSuchElementException("service, 해당 ip로 가져온 데이터 없습니다.");
        }
        return serverInfo;
    }

    public int findServerIdByIp(String ip) {
        if (ip == null) {
            throw new IllegalArgumentException("id를 조회하기 위해 입력된 Ip가 null입니다.");
        }
        Integer serverInfo = serverInfoMapper.findServerIdByIp(ip);
        if (serverInfo == null) {
            throw new NoSuchElementException("해당 ip로 조회된 서버 id 없음. - " + this.getClass());
        }
        return serverInfo;
    }

    public ServerInfo findServerInfoAtHistory(Integer id) {
        if (id == null) throw new IllegalArgumentException("입력받은 값이 null입니다.");
        ServerInfo serverInfo = serverInfoMapper.selectServerInfoAtHistory(id);
        if (serverInfo == null) throw new NoSuchElementException("해당되는 데이터가 없습니다.");
        return serverInfo;
    }

    @Transactional
    public String addServerInfo(String purpose) {
        String os = ServerInfoUtil.getServerOs();
        String hostname = ServerInfoUtil.getServerHostname();
        Long totalMemory = (long) (ServerInfoUtil.getTotalMemory(osBean) / Math.pow(1000, 2));
        String serverIp = ServerInfoUtil.getServerIp(os);
        if (serverIp == null
                || hostname == null
                || os == null
                || totalMemory == null) {
            throw new IllegalArgumentException("등록을 위한 필수 값이 null입니다.");
        }

        if (serverInfoMapper.isIpExists(serverIp) == 1) {
            throw new DataIntegrityViolationException("해당 ip는 이미 등록되어 있습니다.");
        }
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverOs(os)
                .serverHostname(hostname)
                .memoryTotal(totalMemory)
                .purpose(purpose)
                .serverIp(serverIp)
                .build();
        int result = serverInfoMapper.insertServerInfo(serverInfo);
        if (result < 1) throw new RuntimeException("데이터 등록 실패");
        return serverIp;
    }


    @Transactional
    public int updateServerInfo(ServerInfo serverInfo) {
        if (serverInfo.getServerId() == null || serverInfo.getServerIp() == null) {
            throw new IllegalArgumentException("입력받은 값이 비정상입니다.");
        }
        if (serverInfoMapper.selectServerInfoById(serverInfo.getServerId()) == null) {
            throw new NoSuchElementException("수정할 Server를 찾을수 없습니다.");
        }
        System.out.println("Service : " + serverInfo);
        int result = serverInfoMapper.updateServerInfo(serverInfo);
        if (result < 1) {
            throw new RuntimeException("수정에 실패했습니다.");
        }
        return result;
    }

    @Transactional
    public int deleteServerInfo(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("입력된 Id가 null입니다.");
        }
        if (serverInfoMapper.selectServerInfoById(id) == null) {
            throw new NoSuchElementException("존재하지 않는 서버입니다.");
        }
        int result = serverInfoMapper.deleteServerInfoById(id);
        if (result < 1) throw new RuntimeException("데이터 삭제 실패");
        else return result;
    }

    @Transactional
    public int deleteAll() {
        int result = serverInfoMapper.deleteAll();
        if (result > 0) return result;
        else {
            throw new NoSuchElementException("삭제할 데이터가 존재하지 않습니다.");
        }
    }
}
