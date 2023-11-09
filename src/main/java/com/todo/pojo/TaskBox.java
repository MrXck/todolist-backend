package com.todo.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskBox {
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
