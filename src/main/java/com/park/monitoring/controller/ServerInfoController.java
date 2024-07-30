package com.park.monitoring.controller;

import com.park.monitoring.dto.ServerInfoWithDiskDto;
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
    public String index(Model model, RedirectAttributes redirectAttributes) {
        List<ServerInfoWithDiskDto> serverDtoList;
        try {
            serverDtoList = serverInfoService.findServerInfoWithDisk();
            model.addAttribute("serverDtoList", serverDtoList);
        }catch(NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "요청한 데이터를 찾을 수 없습니다.", e);
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error", "예상하지 못한 에러 발생.");
            return "redirect:/error";
        }
        return "dashboard";
    }

    @PostMapping("/history/{serverId}")
    public String detail(Model model, @PathVariable Integer serverId) {
        ServerInfoWithDiskDto serverDtoList;
        try {
            serverDtoList = serverInfoService.findServerInfoAtHistory(serverId);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "입력된 id값 이상 : ", e);
        }catch(NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "요청한 데이터를 찾을 수 없습니다.", e);
        }
        model.addAttribute("serverDtoList", serverDtoList);
        return "history";
    }

    @DeleteMapping("/delete/{serverId}")
    public String deleteServer(@PathVariable Integer serverId, RedirectAttributes redirectAttributes) {
        try {
            serverInfoService.deleteServerInfo(serverId);
            redirectAttributes.addFlashAttribute("message", "서버 삭제 성공.");
            return "redirect:/dashboard"; // Redirect to a list or another page
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "유효하지 않은 서버 아이디.");
            return "redirect:/error"; // Redirect to an error page or similar
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "해당하는 서버 없음.");
            return "redirect:/error"; // Redirect to an error page or similar
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "예상하지 못한 에러 발생.");
            return "redirect:/error"; // Redirect to an error page or similar
        }
    }



    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        model.addAttribute("exception", e.getMessage());
        return "error"; // Error view name (Thymeleaf template)
    }
}
