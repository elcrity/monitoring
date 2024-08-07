package com.park.monitoring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Controller
@RequestMapping("/")
public class homeController {
	
	private static final Logger log = LoggerFactory.getLogger(homeController.class);

    private final DataSource dataSource;

    @Autowired
    public homeController(DataSource dataSource){
        this.dataSource = dataSource;

    }

    @GetMapping
	public String start() {
		return "dashboard";
	}

    @GetMapping("/history/{serverId}")
    public String history(@PathVariable String serverId,Model model) {
        model.addAttribute("serverId", serverId);
        return "history";
    }

    @GetMapping("/data")
    public String data(Model model){
        try (Connection connection = dataSource.getConnection()){
            log.info("--------MariaDb 연결 성공--------");
        }catch (SQLException e){
            log.info("--------MariaDb 연결 실패--------");
        }
        return "Data";
    }

}
