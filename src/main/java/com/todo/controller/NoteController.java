package com.todo.controller;

import com.todo.dto.note.AddNoteDTO;
import com.todo.dto.note.GetNoteByPageDTO;
import com.todo.dto.note.NoteDTO;
import com.todo.dto.note.UpdateNoteDTO;
import com.todo.service.NoteService;
import com.todo.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Log
    @PostMapping("/add")
    public void add(@RequestBody @Valid AddNoteDTO addNoteDTO) {
        noteService.add(addNoteDTO);
    }

    @PostMapping("/getByPage")
    public NoteDTO getByPage(@RequestBody @Valid GetNoteByPageDTO getNoteByPageDTO) {
        return noteService.getByPage(getNoteByPageDTO);
    }

    @Log
    @PostMapping("/remove/{noteId}")
    public void removeById(@PathVariable("noteId") Long noteId) {
        noteService.deleteById(noteId);
    }

    @Log
    @PostMapping("/update")
    public void update(@RequestBody @Valid UpdateNoteDTO updateNoteDTO) {
        noteService.updateNote(updateNoteDTO);
    }

    @GetMapping("/{noteId}")
    public NoteDTO getById(@PathVariable("noteId") Long noteId) {
        return noteService.get(noteId);
    }

}
