package com.todo.dto.operationLog;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PageDTO {

    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;

    @NotNull(message = "pageNum不能为空")
    private Integer pageNum;

    private String keyword;
}
