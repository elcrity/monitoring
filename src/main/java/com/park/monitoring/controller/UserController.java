package com.park.monitoring.controller;

import com.park.monitoring.model.User;
import com.park.monitoring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

//    private final Logger log = LoggerFactory.getLogger(UserController.class);
//    private final UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping
//    public String showUsers(Model model) {
//        List<User> users = userService.getUserList();
//        model.addAttribute("users", users);
//        return "board";
//    }
//
//    @GetMapping("/signForm")
//    public String signForm(Model model) {
//        model.addAttribute("user", new User());
//        return "Form";
//    }
//
//    @PostMapping("/create")
//    public String userSign(@ModelAttribute("user") User user){
//        log.debug("-----유저 컨트롤러/user/create----- {}",user);
//        userService.signUser(user);
//        return "redirect:/user";
//    }
}
