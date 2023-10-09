package com.todo.dto.mind;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AddMindDTO {

    @NotEmpty(message = "标题不能为空")
    private String name;

    @NotEmpty(message = "内容不能为空")
    private String content;

}
