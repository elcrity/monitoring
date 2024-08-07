package com.park.monitoring.controller;

import com.park.monitoring.dto.DashBoardDto;
import com.park.monitoring.model.Disk;
import com.park.monitoring.model.MetricLog;
import com.park.monitoring.model.ServerInfo;
import com.park.monitoring.service.DiskService;
import com.park.monitoring.service.ServerInfoService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

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
        ServerInfo serverDto = serverInfoService.findServerInfoAtHistory(serverId);
//        @ControllerAdvice를 이용한 Exception처리
//        catch (IllegalArgumentException | NoSuchElementException e) {
//            throw e;
//        }
        //일반 Exception 처리
        // catch (IllegalArgumentException e) {
        //      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        // }
        return ResponseEntity.ok().body(serverDto);
    }

    @GetMapping("/regform")
    public ResponseEntity<ServerInfo> showRegistrationForm(Model model) {
        return ResponseEntity.ok().body(new ServerInfo());
    }

//    @PostMapping("/regserver")
//    public ResponseEntity<String> addServer(@RequestParam(required = false) String purpose) {
//        String serverIp;
//        serverIp = serverInfoService.addServerInfo(purpose);
//        int serverId = serverInfoService.findServerIdByIp(serverIp);
//        int result = diskService.insertDisk(serverId);
////      성공 유무 확인 못함
//        return ResponseEntity.ok().body("성공");
//    }
@PostMapping("/regserver")
public ResponseEntity<String> addServer(@RequestParam(required = false) String purpose) {
    String serverIp;
    serverIp = serverInfoService.addServerInfo(purpose);
    int serverId = serverInfoService.findServerIdByIp(serverIp);
    int result = diskService.insertDisk(serverId);
//      성공 유무 확인 못함
    return ResponseEntity.ok().body("성공");
}

    @PostMapping({"/regform/{serverId}", "/regform/"})
    public ResponseEntity<ServerInfo> editForm(@PathVariable(required = false) Integer serverId, Model model) {
        ServerInfo serverDto = serverInfoService.findServerInfoById(serverId);
        return ResponseEntity.ok().body(serverDto);
    }

    @PutMapping("/updateServer")
    public ResponseEntity<String> updateServer(@ModelAttribute ServerInfo serverInfo) {
        serverInfoService.updateServerInfo(serverInfo);
        return ResponseEntity.ok().body("성공");
    }

    @DeleteMapping({"/delete/{serverId}", "/delete/"})
    public ResponseEntity<String> deleteServer(@PathVariable(required = false) Integer serverId) {
        serverInfoService.deleteServerInfo(serverId);
            return ResponseEntity.ok().body("삭제 성공");


    }
}
