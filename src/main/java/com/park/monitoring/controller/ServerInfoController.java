package com.park.monitoring.controller;

import com.park.monitoring.dto.DashBoardDto;
import com.park.monitoring.model.Disk;
import com.park.monitoring.model.ServerInfo;
import com.park.monitoring.service.DiskService;
import com.park.monitoring.service.ServerInfoService;
import com.park.monitoring.util.ServerInfoUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Todo : 컨트롤러 테스트
//Todo : 예외 처리하기,
//Todo : view단에서 데이터 출력, 각 컨트롤러 작동 작성하기
@RestController
@RequestMapping("/api")
public class ServerInfoController {

    private static final Logger log = LoggerFactory.getLogger(ServerInfoController.class);
    ServerInfoService serverInfoService;
    DiskService diskService;

    public ServerInfoController(ServerInfoService serverInfoService, DiskService diskService) {
        this.serverInfoService = serverInfoService;
        this.diskService = diskService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<List<DashBoardDto>> dashboard(HttpServletResponse response) {
        List<ServerInfo> serverDtoList;
        List<Disk> diskList;
        serverDtoList = serverInfoService.findAllServerInfo();
        diskList = diskService.findAllDisks();
        List<DashBoardDto> dashBoardDtoList = new ArrayList<>();
        for (ServerInfo serverDto : serverDtoList) {
            List<Disk> serverDisks = new ArrayList<>();
            for (Disk disk : diskList) {
                if (disk.getDiskServerInfoFk().equals(serverDto.getServerId())) {
                    serverDisks.add(disk);
                }
            }
            DashBoardDto dashBoardDto = new DashBoardDto.Builder()
                    .serverOs(serverDto.getServerOs())
                    .serverHostname(serverDto.getServerHostname())
                    .memoryTotal(serverDto.getMemoryTotal())
                    .serverIp(serverDto.getServerIp())
                    .purpose(serverDto.getPurpose())
                    .serverId(serverDto.getServerId())
                    .disks(serverDisks)
                    .build();
            dashBoardDtoList.add(dashBoardDto);
        }
        return ResponseEntity.ok().body(dashBoardDtoList);
    }

    @PostMapping({"/history/{serverId}", "/history/"})
    public ResponseEntity<ServerInfo> detail(@PathVariable(required = false) Integer serverId) {
        ServerInfo serverInfo = serverInfoService.findServerInfoById(serverId);
        ServerInfo serverDto = new ServerInfo.Builder()
                .serverHostname(serverInfo.getServerHostname())
                .serverIp(serverInfo.getServerIp())
                .memoryTotal(serverInfo.getMemoryTotal())
                .build();
        return ResponseEntity.ok().body(serverDto);
    }

//    @GetMapping("/regform")
//    public ResponseEntity<ServerInfo> showRegistrationForm(Model model) {
//        return ResponseEntity.ok().body(new ServerInfo());
//    }

    @PostMapping("/regserver")
        public ResponseEntity<Map<String, String>> addServer(@RequestBody(required = false) ServerInfo serverInfo) {
        String purpose = "test";
        serverInfo.setServerOs(ServerInfoUtil.getServerOs());
        ServerInfo testServerInfo = new ServerInfo.Builder()
                .serverOs("window")
                .serverHostname("Test Host")
                .memoryTotal(16440L)
                .purpose("test")
                .serverIp("192.168.2.2")
                .build();
        if(serverInfo == null){
            serverInfo = testServerInfo;
        }
        System.out.println("controller : " + serverInfo);
        Map<String, String> response = new HashMap<>();
        int serverResult = serverInfoService.addServerInfo(serverInfo);
        int serverId = serverInfoService.findServerIdByIp(serverInfo.getServerIp());
        int diskResult = diskService.insertDisk(serverId);

        response.put("message", "등록되었습니다");
        return ResponseEntity.ok(response);


    }
//
//    @PostMapping({"/regform/{serverId}", "/regform/"})
//    public ResponseEntity<ServerInfo> editForm(@PathVariable(required = false) Integer serverId) {
//        ServerInfo serverDto = serverInfoService.findServerInfoById(serverId);
//        return ResponseEntity.ok().body(serverDto);
//    }

    @PutMapping("/updateServer")
    public ResponseEntity<Map<String,String>> updateServer(@ModelAttribute ServerInfo serverInfo) {
        serverInfoService.updateServerInfo(serverInfo);
        Map<String, String> response = new HashMap<>();
        response.put("message", serverInfo.getServerIp() + " 서버가 수정됐습니다");
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping({"/delete/{serverId}", "/delete/"})
    public ResponseEntity<Map<String,String>> deleteServer(@PathVariable(required = false) Integer serverId) {
        serverInfoService.deleteServerInfo(serverId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "서버 엔티티를 삭제했습니다.");
        return ResponseEntity.ok().body(response);


    }
}
