package com.todo.dto.note;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.todo.pojo.Note;
import lombok.Data;

@Data
public class NoteDTO {

    private IPage<Note> page;

    private Note note;
}
