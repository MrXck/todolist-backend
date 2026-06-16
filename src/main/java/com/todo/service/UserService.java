package com.todo.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.todo.dto.user.RegisterUserDTO;
import com.todo.dto.user.SendUrlDTO;
import com.todo.dto.user.UpdateUserDTO;
import com.todo.dto.user.UserDTO;
import com.todo.pojo.User;

public interface UserService extends IService<User> {

    UserDTO login(UserDTO userDTO);

    void updateUser(UpdateUserDTO updateUserDTO);

    void register(RegisterUserDTO registerUser);

    void sendEmail(String email);

    void bindEmail(String email, String code);

    void sendIos(SendUrlDTO sendUrlDTO);

    void bindIos(SendUrlDTO sendUrlDTO);

    void sendAndroid(SendUrlDTO sendUrlDTO);

    void bindAndroid(SendUrlDTO sendUrlDTO);
}
