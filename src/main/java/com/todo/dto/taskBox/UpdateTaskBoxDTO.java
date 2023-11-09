package com.todo.dto.taskBox;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateTaskBoxDTO {

    @NotNull(message = "id不能为空")
    private Long id;

    @NotEmpty(message = "名称不能为空")
    private String name;

}
