package com.park.monitoring.controller;

import com.park.monitoring.model.Disk;
import com.park.monitoring.service.DiskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DiskController {

    private final DiskService diskService;

    public DiskController(DiskService diskService) {
        this.diskService = diskService;
    }

    @GetMapping("/diskInfo/{serverId}")
    public Disk getDiskInfoByServerId(@PathVariable Long serverId) {
        // 여기서 serverId를 사용하여 관련 디스크 정보를 조회합니다.
        // 실제로는 serverId에 따라 필터링할 수 있어야 하므로, 필요한 로직을 추가하세요.
        return diskService.findDiskById(serverId);
    }
}
