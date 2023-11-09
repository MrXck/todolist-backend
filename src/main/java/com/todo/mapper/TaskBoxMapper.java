package com.todo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.todo.pojo.TaskBox;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskBoxMapper extends BaseMapper<TaskBox> {
}