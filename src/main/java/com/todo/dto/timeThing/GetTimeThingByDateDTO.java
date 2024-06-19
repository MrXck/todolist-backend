package com.todo.dto.timeThing;

import com.todo.valid.ValidLocalDate;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GetTimeThingByDateDTO {

    @ValidLocalDate(message = "时间不能为空")
    private LocalDate thingDate;
}
