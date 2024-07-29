package com.park.monitoring.controller;

import com.park.monitoring.dto.DetailDto;
import com.park.monitoring.dto.ServerInfoWithDiskDto;
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
        List<ServerInfoWithDiskDto> serverDtoList = serverInfoService.findServerInfoWithDisk();
        model.addAttribute("serverDtoList", serverDtoList);
        return "dashboard";
    }

    @PostMapping("/detail/{serverId}")
    public String detail(Model model, @PathVariable Integer serverId) {
        ServerInfoWithDiskDto serverDtoList = serverInfoService.findServerInfoAtHistory(serverId);
        model.addAttribute("serverDtoList", serverDtoList);
        return "detail";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        model.addAttribute("exception", e.getMessage());
        return "error"; // Error view name (Thymeleaf template)
    }
}
