package com.park.monitoring.controller;

import com.park.monitoring.dto.DetailDto;
import com.park.monitoring.model.Disk;
import com.park.monitoring.model.ServerInfo;
import com.park.monitoring.service.DiskService;
import com.park.monitoring.service.ServerInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class ServerInfoController {

    ServerInfoService serverInfoService;
    DiskService diskService;

    public ServerInfoController(ServerInfoService serverInfoService, DiskService diskService) {
        this.serverInfoService = serverInfoService;
        this.diskService = diskService;
    }

    @GetMapping
    public String index(Model model) {
        List<ServerInfo> serverinfoList = serverInfoService.findAllServerInfo();
        List<Disk> diskList = diskService.findAllDisks();
        model.addAttribute("serverInfoList", serverinfoList);
        model.addAttribute("diskList", diskList);
        return "dashboard";
    }

    @PostMapping("/detail/{serverId}")
    public String detail(Model model, @PathVariable Long serverId) {
        List<ServerInfo> serverinfoList = serverInfoService.findAllServerInfo();
        ServerInfo serverInfo = serverInfoService.findServerInfoById(serverId);
        List<Disk> diskList = diskService.findAllDisksByServerId(serverId);
        DetailDto detailDto = new DetailDto.Builder().ModelToDto(serverInfo, diskList);
        model.addAttribute("serverInfoList", serverinfoList);
        model.addAttribute("diskList", diskList);
        model.addAttribute("detailDto", detailDto);
        return "detail";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        model.addAttribute("exception", e.getMessage());
        return "error"; // Error view name (Thymeleaf template)
    }
}
