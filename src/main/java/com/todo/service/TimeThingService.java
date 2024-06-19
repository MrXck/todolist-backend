package com.todo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.todo.dto.timeThing.*;
import com.todo.pojo.TimeThing;

public interface TimeThingService extends IService<TimeThing> {

    TimeThingDTO getByPage(GetTimeThingByPageDTO dto);

    void insert(InsertDTO dto);

    void deleteById(Long id);

    void edit(UpdateDTO dto);

    TimeThingDTO getByDate(GetTimeThingByDateDTO dto);
}