package com.park.monitoring.controller;

import com.park.monitoring.model.Disk;
import com.park.monitoring.model.ServerInfo;
import com.park.monitoring.service.DiskService;
import com.park.monitoring.service.ServerInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class ServerInfoController {

    ServerInfoService serverInfoService;

    public ServerInfoController(ServerInfoService serverInfoService) {
        this.serverInfoService = serverInfoService;
    }

    @GetMapping
    public String index(Model model) {
        List<ServerInfo> serverinfoList = serverInfoService.findAllServerInfo();
        model.addAttribute("serverInfoList", serverinfoList);
        return "dashboard";
    }
}
