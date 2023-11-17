package com.todo.dto.todo;

import com.todo.valid.ValidLocalDate;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class BatchGenerateTodoDTO {

    @NotEmpty(message = "事项名称不能为空")
    private String title;

    private String detail;

    @ValidLocalDate(message = "开始时间不能为空")
    private LocalDate startTime;

    @ValidLocalDate(message = "结束时间不能为空")
    private LocalDate endTime;

    @NotNull(message = "生成周期不能为空")
    private Integer generateType;

    private List<Integer> generateDateList;

    private Long taskBoxId;

    @NotNull(message = "重要程度不能为空")
    @Min(value = 1, message = "不能小于1")
    @Max(value = 5, message = "不能大于5")
    private Integer priority;

    @NotNull(message = "持续时间不能为空")
    @Min(value = 1, message = "不能小于1")
    private Integer duration;
}
