package com.todo.dto.todo;

import com.todo.valid.ValidLocalDate;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GetTodoDTO {

    @ValidLocalDate(message = "开始时间不能为空")
    private LocalDate startTime;

    @ValidLocalDate(message = "结束时间不能为空")
    private LocalDate endTime;

}
