package com.todo.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Mind {
    private Long id;

    private String name;

    private String content;

    private Long userId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
