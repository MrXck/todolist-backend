package com.todo.dto.user;

import com.todo.pojo.User;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserDTO {

    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotEmpty(message = "密码不能为空")
    private String password;

    private String token;

    private User user;
}
