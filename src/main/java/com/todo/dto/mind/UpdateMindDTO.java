package com.todo.dto.mind;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateMindDTO {

    @NotNull(message = "id不能为空")
    private Long id;

    @NotEmpty(message = "标题不能为空")
    private String name;

    @NotEmpty(message = "内容不能为空")
    private String content;
}
