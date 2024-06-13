package com.todo.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperationLog {

    private Long id;

    private String method;

    private String path;

    private String param;

    private String response;

    private Long userId;

    private Long consuming;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
