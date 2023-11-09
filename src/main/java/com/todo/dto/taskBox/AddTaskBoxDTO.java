package com.todo.dto.taskBox;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AddTaskBoxDTO {
    @NotEmpty(message = "名称不能为空")
    private String name;
}
