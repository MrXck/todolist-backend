package com.todo.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Note {

    private Long id;
    private String title;
    private String detail;
    private Long userId;
    private String chart;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
