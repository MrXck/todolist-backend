package com.todo.controller;

import com.todo.dto.user.RegisterUserDTO;
import com.todo.dto.user.UpdateUserDTO;
import com.todo.dto.user.UserDTO;
import com.todo.service.UserService;
import com.todo.utils.NoAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @NoAuthorization
    public UserDTO login(@RequestBody @Valid UserDTO userDTO) {
        return userService.login(userDTO);
    }

    @PostMapping("/update")
    public void update(@RequestBody @Valid UpdateUserDTO updateUserDTO) {
        userService.updateUser(updateUserDTO);
    }

    @PostMapping("/register")
    @NoAuthorization
    public void register(@RequestBody @Valid RegisterUserDTO registerUser) {
        userService.register(registerUser);
    }

    @GetMapping("/sendEmail/{email}")
    public void sendEmail(@PathVariable("email") String email) {
        userService.sendEmail(email);
    }

    @GetMapping("/bindEmail/{email}/{code}")
    public void bindEmail(@PathVariable("email") String email, @PathVariable("code") String code) {
        userService.bindEmail(email, code);
    }
}
