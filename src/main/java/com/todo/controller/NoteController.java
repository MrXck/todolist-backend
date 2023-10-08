package com.todo.controller;

import com.todo.dto.note.AddNoteDTO;
import com.todo.dto.note.GetNoteByPageDTO;
import com.todo.dto.note.NoteDTO;
import com.todo.dto.note.UpdateNoteDTO;
import com.todo.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping("/add")
    public String add(@RequestBody @Valid AddNoteDTO addNoteDTO) {
        noteService.add(addNoteDTO);
        return "";
    }

    @PostMapping("/getByPage")
    public NoteDTO getByPage(@RequestBody @Valid GetNoteByPageDTO getNoteByPageDTO) {
        return noteService.getByPage(getNoteByPageDTO);
    }

    @PostMapping("/remove/{noteId}")
    public String removeById(@PathVariable("noteId") Long noteId) {
        noteService.deleteById(noteId);
        return "";
    }

    @PostMapping("/update")
    public String update(@RequestBody @Valid UpdateNoteDTO updateNoteDTO) {
        noteService.updateNote(updateNoteDTO);
        return "";
    }

    @GetMapping("/{noteId}")
    public NoteDTO getById(@PathVariable("noteId") Long noteId) {
        return noteService.get(noteId);
    }

}
