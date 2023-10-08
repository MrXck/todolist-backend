package com.todo.dto.note;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AddNoteDTO {

    @NotEmpty(message = "标题不能为空")
    private String title;

    @NotEmpty(message = "内容不能为空")
    private String detail;

}
