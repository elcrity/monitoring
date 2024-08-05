package com.park.monitoring.controller;

import com.park.monitoring.model.Disk;
import com.park.monitoring.model.ServerInfo;
import com.park.monitoring.service.DiskService;
import com.park.monitoring.service.ServerInfoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping
public class ServerInfoController {

    ServerInfoService serverInfoService;
    DiskService diskService;


    public ServerInfoController(ServerInfoService serverInfoService, DiskService diskService) {
        this.serverInfoService = serverInfoService;
        this.diskService = diskService;
    }

    @GetMapping
    public String dashboard(Model model) {
        List<ServerInfo> serverDtoList;
        List<Disk> diskList;
        serverDtoList = serverInfoService.findAllServerInfo();
        diskList = diskService.findAllDisks();
        if (serverDtoList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "등록된 서버가 없습니다.");
        }
        model.addAttribute("serverDtoList", serverDtoList);
        model.addAttribute("diskList", diskList);
        return "dashboard";
    }

    @PostMapping({"/history/{serverId}", "/history/"})
    public String detail(Model model, @PathVariable(required = false) Integer serverId) {
        ServerInfo serverDto;
        try {
            serverDto = serverInfoService.findServerInfoAtHistory(serverId);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
//        @ControllerAdvice를 이용한 Exception처리
//        catch (IllegalArgumentException | NoSuchElementException e) {
//            throw e;
//        }
        //일반 Exception 처리
        // catch (IllegalArgumentException e) {
        //      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        // }
        model.addAttribute("serverDto", serverDto);
        return "history";
    }

    @GetMapping("/regForm")
    public String showRegistrationForm(Model model) {
        model.addAttribute("serverDto", new ServerInfo());
        return "register";
    }

    @PostMapping("/regServer")
    public String addServer(@RequestParam String purpose) {
        String serverIp;
        try {
            serverIp = serverInfoService.addServerInfo(purpose);
            int serverId = serverInfoService.findServerInfoByIp(serverIp).getServerId();
            int result = diskService.insertDisk(serverId);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping({"/editForm/{serverId}", "/editForm/"})
    public String editForm(@PathVariable(required = false) Integer serverId, Model model) {
        ServerInfo serverDto;
        serverDto = serverInfoService.findServerInfoById(serverId);
        model.addAttribute("serverDto", serverDto);
        return "register";
    }

    @PutMapping("/updateServer")
    public String updateServer(@ModelAttribute ServerInfo serverInfo) {
        serverInfoService.updateServerInfo(serverInfo);
        return "redirect:/history/" + serverInfo.getServerId();
    }


    @DeleteMapping({"/delete/{serverId}", "/delete/"})
    public String deleteServer(@PathVariable(required = false) Integer serverId, RedirectAttributes redirectAttributes) {
        serverInfoService.deleteServerInfo(serverId);
        redirectAttributes.addFlashAttribute("message", "서버 삭제 성공.");
        return "redirect:/";
    }
//    @ExceptionHandler(IllegalArgumentException.class)
//    public String handleIllegalArgumentException(IllegalArgumentException e) {
//        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
//    }
}
