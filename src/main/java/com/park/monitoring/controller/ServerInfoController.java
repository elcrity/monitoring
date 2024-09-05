package com.park.monitoring.controller;

import com.park.monitoring.model.ServerInfo;
import com.park.monitoring.service.DiskService;
import com.park.monitoring.service.ServerInfoService;
import com.park.monitoring.util.ServerInfoUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Server Controller", description = "서버 입력")
public class ServerInfoController {

    private static final Logger log = LoggerFactory.getLogger(ServerInfoController.class);
    ServerInfoService serverInfoService;
    DiskService diskService;

    public ServerInfoController(ServerInfoService serverInfoService, DiskService diskService) {
        this.serverInfoService = serverInfoService;
        this.diskService = diskService;
    }

//    @GetMapping("/dashboard")
//    public ResponseEntity<List<DashBoardDto>> dashboard(HttpServletResponse response) {
//        List<ServerInfo> serverDtoList;
//        List<Disk> diskList;
//        serverDtoList = serverInfoService.findAllServerInfo();
//        diskList = diskService.findAllDisks();
//        List<DashBoardDto> dashBoardDtoList = new ArrayList<>();
//        for (ServerInfo serverDto : serverDtoList) {
//            List<Disk> serverDisks = new ArrayList<>();
//            for (Disk disk : diskList) {
//                if (disk.getDiskServerInfoFk().equals(serverDto.getServerId())) {
//                    serverDisks.add(disk);
//                }
//            }
//            DashBoardDto dashBoardDto = new DashBoardDto.Builder()
//                    .serverOs(serverDto.getServerOs())
//                    .serverHostname(serverDto.getServerHostname())
//                    .memoryTotal(serverDto.getMemoryTotal())
//                    .serverIp(serverDto.getServerIp())
//                    .purpose(serverDto.getPurpose())
//                    .serverId(serverDto.getServerId())
//                    .disks(serverDisks)
//                    .build();
//            dashBoardDtoList.add(dashBoardDto);
//        }
//        return ResponseEntity.ok().body(dashBoardDtoList);
//    }

//    @PostMapping({"/history/{serverId}", "/history/"})
//    public ResponseEntity<ServerInfo> detail(@PathVariable(required = false) Integer serverId) {
//        ServerInfo serverInfo = serverInfoService.findServerInfoById(serverId);
//        ServerInfo serverDto = new ServerInfo.Builder()
//                .serverId(serverInfo.getServerId())
//                .serverHostname(serverInfo.getServerHostname())
//                .serverIp(serverInfo.getServerIp())
//                .memoryTotal(serverInfo.getMemoryTotal())
//                .purpose(serverInfo.getPurpose())
//                .build();
//        return ResponseEntity.ok().body(serverDto);
//    }
//
//    @PostMapping("/regserver")
//        public ResponseEntity<Map<String, String>> addServer(@RequestBody(required = false) ServerInfo serverInfo) {
//        serverInfo.setServerOs(ServerInfoUtil.getServerOs());
//        Map<String, String> response = new HashMap<>();
//        serverInfoService.addServerInfo(serverInfo);
//        int serverId = serverInfoService.findServerIdByIp(serverInfo.getServerIp());
//        diskService.insertDisk(serverId);
//
//        response.put("message", serverInfo.getServerIp() + "서버가 등록되었습니다");
//        return ResponseEntity.ok(response);
//    }
//
//    @PutMapping("/server")
//    public ResponseEntity<Map<String,String>> updateServer(@RequestBody ServerInfo serverInfo) {
//        serverInfoService.updateServerInfo(serverInfo);
//        Map<String, String> response = new HashMap<>();
//        response.put("message", serverInfo.getServerIp() + " 서버가 수정됐습니다");
//        return ResponseEntity.ok().body(response);
//    }

    @DeleteMapping({"/delete/{serverId}", "/delete/"})
    public ResponseEntity<Map<String,String>> deleteServer(@PathVariable(required = false) Integer serverId) {
        serverInfoService.deleteServerInfo(serverId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "서버 엔티티를 삭제했습니다.");
        return ResponseEntity.ok().body(response);
    }
}
