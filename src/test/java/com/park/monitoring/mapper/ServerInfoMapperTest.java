package com.park.monitoring.mapper;

import com.park.monitoring.model.ServerInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@ExtendWith(SpringExtension.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@ActiveProfiles("local")
@Sql({"classpath:testTable.sql", "classpath:testData.sql"})
public class ServerInfoMapperTest {
    private static final Logger log = LoggerFactory.getLogger(ServerInfoMapperTest.class);

    @Autowired
    private ServerInfoMapper serverInfoMapper;


    @Test
    void getAllServerInfo(){
        List<ServerInfo> servers = serverInfoMapper.getAllServerInfo();

        assertNotNull(servers);
        assertEquals(servers.size(), 10);
    }

    @Test
    void getServerInfo(){
        long serverId = 3L;
        ServerInfo server = serverInfoMapper.getServerInfoById(serverId);
        assertNotNull(server);
        assertThat(server.getServerHostname())
                .isEqualTo("server3.example.com");

        ServerInfo checkIdInfo = serverInfoMapper.getServerInfoById(serverId);
        assertNotNull(checkIdInfo);
        assertThat(checkIdInfo.getServerId()).isEqualTo(serverId);

        ServerInfo checkIpInfo = serverInfoMapper.getServerInfoByIp(checkIdInfo.getServerIp());
        assertNotNull(checkIpInfo);
        assertThat(checkIpInfo.getServerIp()).isEqualTo(checkIdInfo.getServerIp());
    }

    @Test
    void addServerInfo() {
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverOs("윈도우")
                .serverHostname("park1104")
                .memoryTotal(16000)
                .purpose("ftp 서버")
                .serverIp("192.168.2.60")
                .build();

        int result = serverInfoMapper.addServerInfo(serverInfo);
        assertEquals(1, result);
        assertEquals("park1104", serverInfoMapper.getServerInfoByIp(serverInfo.getServerIp()).getServerHostname());
    }

    @Test
    void updateServerInfo() {
        long serverId = 1L;
        ServerInfo updateInfo = new ServerInfo.Builder()
                .serverId(serverId)
                .serverOs("윈도우")
                .serverHostname("park1104")
                .memoryTotal(16000)
                .purpose("ftp 서버")
                .serverIp("192.168.2.60")
                .build();
        int result = serverInfoMapper.updateServerInfo(updateInfo);
        ServerInfo updatedInfo = serverInfoMapper.getServerInfoById(serverId);
        assertEquals(1,  result);

        assertEquals("윈도우", updatedInfo.getServerOs());
        assertEquals("park1104", updatedInfo.getServerHostname());
        assertEquals(16000, updatedInfo.getMemoryTotal());
        assertEquals("ftp 서버", updatedInfo.getPurpose());
        assertEquals("192.168.2.60", updatedInfo.getServerIp());
    }

    @Test
    void deleteServerInfo() {
        long serverId = 1L;
        ServerInfo serverInfo = serverInfoMapper.getServerInfoById(serverId);
        assertNotNull(serverInfo);
        int result = serverInfoMapper.deleteServerInfoById(serverId);
        assertEquals(1, result);
        assertNull(serverInfoMapper.getServerInfoById(serverId));
    }
}
