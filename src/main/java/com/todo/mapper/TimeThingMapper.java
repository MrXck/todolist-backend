package com.todo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.todo.pojo.TimeThing;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TimeThingMapper extends BaseMapper<TimeThing> {
}