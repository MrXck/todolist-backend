package com.todo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todo.dto.user.RegisterUserDTO;
import com.todo.dto.user.UpdateUserDTO;
import com.todo.dto.user.UserDTO;
import com.todo.exception.APIException;
import com.todo.mapper.UserMapper;
import com.todo.pojo.User;
import com.todo.service.UserService;
import com.todo.utils.Constant;
import com.todo.utils.JwtUtils;
import com.todo.utils.MD5Utils;
import com.todo.utils.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public UserDTO login(UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        queryWrapper.eq(User::getPassword, MD5Utils.md5(password));

        User user = this.getOne(queryWrapper);

        if (user == null) {
            throw new APIException(Constant.LOGIN_ERROR);
        }

        user.setLastLoginTime(LocalDateTime.now());
        this.updateById(user);

        Map<String, Object> claims = new HashMap<>(16);
        claims.put("userId", user.getId());
        String token = jwtUtils.createToken(claims, 720);
        user.setPassword(null);
        userDTO.setToken(token);
        userDTO.setPassword("");
        userDTO.setUser(user);
        return userDTO;
    }

    @Override
    public void updateUser(UpdateUserDTO updateUserDTO) {
        String username = updateUserDTO.getUsername();
        String password = updateUserDTO.getPassword();

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        queryWrapper.ne(User::getId, UserThreadLocal.get());
        User user = this.getOne(queryWrapper);
        if (user != null) {
            throw new APIException(Constant.USERNAME_ALREADY_ERROR);
        }

        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, UserThreadLocal.get());
        updateWrapper.set(User::getUsername, username);
        updateWrapper.set(User::getPassword, MD5Utils.md5(password));
        updateWrapper.set(User::getUpdateTime, LocalDateTime.now());

        this.update(updateWrapper);
    }

    @Override
    public void register(RegisterUserDTO registerUser) {
        String username = registerUser.getUsername();
        String password = registerUser.getPassword();

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = this.getOne(queryWrapper);

        if (user != null) {
            throw new APIException(Constant.USERNAME_ALREADY_ERROR);
        }

        user = new User();
        user.setUsername(username);
        user.setPassword(MD5Utils.md5(password));
        user.setUpdateTime(LocalDateTime.now());
        user.setCreateTime(LocalDateTime.now());
        this.save(user);
    }
}
