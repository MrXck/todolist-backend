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
import com.todo.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private MailUtils mailUtils;

    @Autowired
    private CacheUtils<String, String> cacheUtils;

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
    @Transactional(rollbackFor = Exception.class)
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
        updateWrapper.set(User::getEnableEmail, updateUserDTO.getEnableEmail());
        updateWrapper.set(User::getUpdateTime, LocalDateTime.now());

        this.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
        user.setEnableEmail(Constant.DISABLE_EMAIL);
        user.setUpdateTime(LocalDateTime.now());
        user.setCreateTime(LocalDateTime.now());
        this.save(user);
    }

    @Override
    public void sendEmail(String email) {
        if (!EmailValidatorUtils.isValid(email)) {
            throw new APIException(Constant.CHECK_EMAIL_ERROR);
        }

        if (cacheUtils.get(UserThreadLocal.get().toString()) != null || cacheUtils.get(email) != null) {
            throw new APIException(Constant.SEND_EMAIL_CODE_ERROR);
        }

        String string = ValidateCodeUtils.generateValidateCode4String(6);
        cacheUtils.put(UserThreadLocal.get().toString(), string, 300, TimeUnit.SECONDS);
        cacheUtils.put(email, string, 300, TimeUnit.SECONDS);
        mailUtils.sendMail(email, "邮箱绑定验证码", string);
    }

    @Override
    public void bindEmail(String email, String code) {
        String string = cacheUtils.get(email);

        if (string == null || !string.equals(code)) {
            throw new APIException(Constant.CHECK_EMAIL_CODE_ERROR);
        }

        cacheUtils.remove(email);

        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, UserThreadLocal.get());
        updateWrapper.set(User::getEmail, email);
        this.update(updateWrapper);
    }
}
