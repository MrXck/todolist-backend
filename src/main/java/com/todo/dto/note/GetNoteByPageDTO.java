package com.todo.dto.note;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class GetNoteByPageDTO {

    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "不能小于1")
    private Integer pageSize;

    @NotNull(message = "页数不能为空")
    @Min(value = 1, message = "不能小于1")
    private Integer pageNum;

    private String keyword;
}
