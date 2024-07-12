package com.park.monitoring.service;

import com.park.monitoring.mapper.UserMapper;
import com.park.monitoring.model.User;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.util.List;

@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper){
        this.userMapper=userMapper;
    }

    public List<User> getUserList(){
        return userMapper.showAllUsers();
    }

    public void signUser(User user){
        userMapper.insertUser(user);
    }
}
