package com.park.monitoring.service;

import com.park.monitoring.mapper.ServerInfoMapper;
import com.park.monitoring.model.ServerInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@ActiveProfiles("local")
@Sql({"classpath:testTable.sql", "classpath:testData.sql"})
public class ServerInfoServiceTest {
    Logger log = LoggerFactory.getLogger(ServerInfoServiceTest.class);

    @Autowired
    ServerInfoMapper serverInfoMapper;

    ServerInfoService serverInfoService;

    @BeforeEach
    void init() {
        serverInfoService = new ServerInfoService(serverInfoMapper);
    }


//    public ServerInfoServiceTest(ServerInfoService serverInfoService) {
//        this.serverInfoService = serverInfoService;
//    }

    @Test
    void findAllServerInfo() {
        List<ServerInfo> serverInfo = serverInfoService.findAllServerInfo();
        assertEquals(10, serverInfo.size());

    }

    @Test
    void findServerInfoByIdTest() {
        ServerInfo serverInfo = serverInfoService.findServerInfoById(1L);
        assertEquals("Linux", serverInfo.getServerOs());

        // 예외 테스트
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            serverInfoService.findServerInfoById(-1L);
        });
        assertEquals("service, 해당 ip로 가져온 데이터 없음.", exception.getMessage());
    }

    @Test
    void findServerInfoByIpNullTest() {
        ServerInfo serverInfo = serverInfoService.findServerInfoByIp("192.168.1.1");
        assertEquals("Linux", serverInfo.getServerOs());

        // 예외 테스트
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> {
            serverInfoService.findServerInfoByIp(null);
        });
        assertEquals("입력된 IP가 null입니다.", exception1.getMessage());
    }

    @Test
    void regServerInfoTest() {
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverOs("windwo")
                .serverHostname("test")
                .memoryTotal(8012)
                .purpose("서버")
                .serverIp("192.168.1.11")
                .build();
        assertEquals(1, serverInfoService.addServerInfo(serverInfo));

        serverInfo.setServerIp(null);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {

            serverInfoService.addServerInfo(serverInfo);

        });
        assertEquals("데이터의 필수 값이 null입니다.", exception.getMessage());
    }

    @Test
    void addServerInfoWithDuplicateIp() {
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverOs("windows")
                .serverHostname("test")
                .memoryTotal(8012)
                .purpose("서버")
                .serverIp("192.168.1.1") // 중복 IP 설정
                .build();

        // 중복 IP로 인해 DataIntegrityViolationException 발생

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            serverInfoService.addServerInfo(serverInfo);
        });
        //(conn=4057) Duplicate entry '192.168.1.1' for key 'server_ip'>
        assertTrue(exception.getMessage().contains("Duplicate entry"));
    }


    @Test
    void updateServerInfoTest() {
        //정상 값
        Long testId = 1L;
        ServerInfo updateInfo = new ServerInfo.Builder()
                .serverId(testId)
                .serverOs("테스트Os")
                .memoryTotal(11223)
                .purpose("테스트용")
                .serverHostname("testServer")
                .serverIp("111.222.333.444")
                .build();

        int result = serverInfoService.updateServerInfo(updateInfo);
        assertEquals(1, result);
        assertEquals("111.222.333.444", serverInfoService.findServerInfoById(testId).getServerIp());
        assertEquals("테스트Os", serverInfoService.findServerInfoById(testId).getServerOs());
    }

    @Test
    void updateServerNotExistInfoTest() {
        //없는 id값
        Long testId = 22L;
        ServerInfo updateInfo = new ServerInfo.Builder()
                .serverId(testId)
                .serverOs("테스트Os")
                .memoryTotal(11223)
                .purpose("테스트용")
                .serverHostname("testServer")
                .serverIp("111.222.333.444")
                .build();

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            serverInfoService.updateServerInfo(updateInfo);
        });
        assertEquals("존재하지 않는 서버 ID입니다.", exception.getMessage());
    }

    @Test
    void updateServerDIVInfoTest() {
        //필수 값 null
        Long testId = 1L;
        ServerInfo updateInfo = new ServerInfo.Builder()
                .serverId(testId)
                .serverOs("테스트Os")
                .memoryTotal(11223)
                .purpose("테스트용")
                .serverHostname("testServer")
                .serverIp(null)
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            serverInfoService.updateServerInfo(updateInfo);
        });
        assertTrue(exception.getMessage().contains("Column 'server_ip' cannot be null"));
    }


    @Test
    void deleteServerInfoTest() {
        Long testId = 1L;
        //정상
        int result = serverInfoService.deleteServerInfoById(testId);
        assertEquals(1, result);
    }

    @Test
    void deleteServerNotExistInfoTest() {
        Long testId = 11L;
        //정상
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            int result = serverInfoService.deleteServerInfoById(testId);
            assertEquals(0, result);
        });
        assertEquals("존재하지 않는 id. 삭제 실패", exception.getMessage());
    }
}
