package com.park.monitoring.service;

import com.park.monitoring.config.error.ErrorCode;
import com.park.monitoring.config.error.Exception.BadRequestException;
import com.park.monitoring.config.error.Exception.BaseException;
import com.park.monitoring.config.error.Exception.DuplicateDataException;
import com.park.monitoring.config.error.Exception.NotFoundException;
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
            throw new BadRequestException(ErrorCode.INVALID_INPUT_VALUE);
        }
        return serverInfo;
    }

    public ServerInfo findServerInfoById(Integer id) {
        if (id == null) {
            throw new BadRequestException(ErrorCode.INVALID_INPUT_VALUE);
        }
        ServerInfo serverInfo = serverInfoMapper.selectServerInfoById(id);
        if (serverInfo == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }
        return serverInfo;
    }

    public int findServerIdByIp(String ip) {
        if (ip == null) {
            throw new BadRequestException(ErrorCode.INVALID_INPUT_VALUE);
        }
        Integer serverInfo = serverInfoMapper.findServerIdByIp(ip);
        if (serverInfo == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }
        return serverInfo;
    }

    public ServerInfo findServerInfoAtHistory(Integer id) {
        if (id == null) throw new BadRequestException(ErrorCode.INVALID_INPUT_VALUE);
        ServerInfo serverInfo = serverInfoMapper.selectServerInfoAtHistory(id);
        if (serverInfo == null) throw new NotFoundException(ErrorCode.NOT_FOUND);
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
            throw new BadRequestException(ErrorCode.INVALID_INPUT_VALUE);
        }

        if (serverInfoMapper.isIpExists(serverIp) == 1) {
            throw new DuplicateDataException(ErrorCode.DUPLICATED_ENTITY);
        }
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverOs(os)
                .serverHostname(hostname)
                .memoryTotal(totalMemory)
                .purpose(purpose)
                .serverIp(serverIp)
                .build();
        int result = serverInfoMapper.insertServerInfo(serverInfo);
        if (result < 1) throw new BaseException(ErrorCode.UNEXPECTED_ERROR);
        return serverIp;
    }


    @Transactional
    public int updateServerInfo(ServerInfo serverInfo) {
        if (serverInfo.getServerId() == null || serverInfo.getServerIp() == null) {
            throw new BadRequestException(ErrorCode.INVALID_INPUT_VALUE);
        }
        if(serverInfoMapper.selectServerInfoById(serverInfo.getServerId()) == null){
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }
        if (serverInfoMapper.isIpExists(serverInfo.getServerIp())!=0) {
            throw new DuplicateDataException(ErrorCode.DUPLICATED_ENTITY);
        }
        int result = serverInfoMapper.updateServerInfo(serverInfo);
        if (result < 1) {
            throw new BaseException(ErrorCode.UNEXPECTED_ERROR);
        }
        return result;
    }

    @Transactional
    public int deleteServerInfo(Integer id) {
        if (id == null) {
            throw new BadRequestException(ErrorCode.INVALID_INPUT_VALUE);
        }
        if (serverInfoMapper.selectServerInfoById(id) == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }
        int result = serverInfoMapper.deleteServerInfoById(id);
        if (result < 1) throw new BaseException(ErrorCode.UNEXPECTED_ERROR);
        else return result;
    }

    @Transactional
    public int deleteAll() {
        int result = serverInfoMapper.deleteAll();
        if (result > 0) return result;
        else {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }
    }
}
