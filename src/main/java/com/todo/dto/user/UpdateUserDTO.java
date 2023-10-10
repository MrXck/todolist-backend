package com.todo.dto.user;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateUserDTO {

    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotEmpty(message = "密码不能为空")
    private String password;

    private String email;

}
