package com.park.monitoring.mapper;

import com.park.monitoring.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    List<User> showAllUsers();

    void insertUser(User user);

}
