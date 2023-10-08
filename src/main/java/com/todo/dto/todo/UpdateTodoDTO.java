package com.todo.dto.todo;

import com.todo.valid.ValidLocalDate;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class UpdateTodoDTO {

    @NotNull(message = "id不能为空")
    private Long id;

    @NotEmpty(message = "标题不能为空")
    private String title;

    private String detail;

    @NotNull(message = "是否完成不能为空")
    private Boolean isDone;

    @NotNull(message = "重要程度不能为空")
    @Min(value = 1, message = "不能小于1")
    @Max(value = 5, message = "不能大于5")
    private Integer priority;

    @ValidLocalDate(message = "开始时间不能为空")
    private LocalDate startTime;

    @ValidLocalDate(message = "结束时间不能为空")
    private LocalDate endTime;
}
