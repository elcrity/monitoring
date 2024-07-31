package com.park.monitoring.controller;

import com.park.monitoring.model.ServerInfo;
import com.park.monitoring.service.DiskService;
import com.park.monitoring.service.ServerInfoService;
import com.park.monitoring.util.ConvertUtil;
import com.park.monitoring.util.ServerInfoUtil;
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
    public String dashboard(Model model, RedirectAttributes redirectAttributes) {
        List<ServerInfo> serverDtoList;
        try {
            serverDtoList = serverInfoService.findAllServerInfo();
            if (serverDtoList.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "등록된 서버가 없습니다.");
            }
            model.addAttribute("serverDtoList", serverDtoList);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return "dashboard";
    }

    @PostMapping({"/history/{serverId}", "/history/"})
    public String detail(Model model, @PathVariable(required = false) String serverId) {
        ServerInfo serverDto;
        try {
            Integer id = ConvertUtil.convertToInteger(serverId);
            serverDto = serverInfoService.findServerInfoAtHistory(id);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        model.addAttribute("serverDto", serverDto);
        return "history";
    }

    @GetMapping("/regForm")
    public String toReg() {
        return "register";
    }

    @PostMapping("/add/server")
    public String addServer(@RequestParam String purpose) {
        String os = ServerInfoUtil.getServerOs();
        String hostname = ServerInfoUtil.getServerHostname();
        long totalMemory = (long) ServerInfoUtil.getServerMemory().get("totalMemory");
        String serverIp = ServerInfoUtil.getServerIp(os);
        int result;
        try {
            result = serverInfoService.addServerInfo
                    (new ServerInfo.Builder()
                            .serverOs(os)
                            .serverHostname(hostname)
                            .memoryTotal(totalMemory)
                            .purpose(purpose)
                            .serverIp(serverIp)
                            .build());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return "redirect:/dashboard";
    }

    @DeleteMapping("/delete/{serverId}")
    public String deleteServer(@PathVariable Integer serverId, RedirectAttributes redirectAttributes) {
        try {
            serverInfoService.deleteServerInfo(serverId);
            redirectAttributes.addFlashAttribute("message", "서버 삭제 성공.");
            return "redirect:/dashboard";
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
//    @ExceptionHandler(IllegalArgumentException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
//        model.addAttribute("exception", e.getMessage());
//        return "error"; // Error view name (Thymeleaf template)
//    }
    }
}
