package com.todo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.todo.dto.note.AddNoteDTO;
import com.todo.dto.note.GetNoteByPageDTO;
import com.todo.dto.note.NoteDTO;
import com.todo.dto.note.UpdateNoteDTO;
import com.todo.pojo.Note;

public interface NoteService extends IService<Note> {
    void add(AddNoteDTO addNoteDTO);

    NoteDTO getByPage(GetNoteByPageDTO getNoteByPageDTO);

    void deleteById(Long noteId);

    void updateNote(UpdateNoteDTO updateNoteDTO);

    NoteDTO get(Long noteId);
}