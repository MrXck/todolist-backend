package com.todo.dto.timeThing;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.todo.pojo.TimeThing;
import lombok.Data;

import java.util.List;

@Data
public class TimeThingDTO {

    private Page<TimeThing> page;

    private TimeThing timeThing;

    private List<TimeThing> timeThings;

}