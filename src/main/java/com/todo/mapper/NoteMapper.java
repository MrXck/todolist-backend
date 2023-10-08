package com.todo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.todo.pojo.Note;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoteMapper extends BaseMapper<Note> {
}