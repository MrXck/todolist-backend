package com.todo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todo.dto.note.AddNoteDTO;
import com.todo.dto.note.GetNoteByPageDTO;
import com.todo.dto.note.NoteDTO;
import com.todo.dto.note.UpdateNoteDTO;
import com.todo.exception.APIException;
import com.todo.pojo.Note;
import com.todo.mapper.NoteMapper;
import com.todo.service.NoteService;
import com.todo.utils.Constant;
import com.todo.utils.UserThreadLocal;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements NoteService {

    @Override
    public void add(AddNoteDTO addNoteDTO) {
        Note note = new Note();
        note.setTitle(addNoteDTO.getTitle());
        note.setDetail(addNoteDTO.getDetail());
        note.setUserId(UserThreadLocal.get());
        note.setCreateTime(LocalDateTime.now());
        note.setUpdateTime(LocalDateTime.now());
        this.save(note);
    }

    @Override
    public NoteDTO getByPage(GetNoteByPageDTO getNoteByPageDTO) {
        IPage<Note> page = new Page<>(getNoteByPageDTO.getPageNum(), getNoteByPageDTO.getPageSize());
        String keyword = getNoteByPageDTO.getKeyword();

        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Note::getUserId, UserThreadLocal.get());
        queryWrapper.like(keyword != null && !"".equals(keyword), Note::getTitle, keyword);
        queryWrapper.select(Note.class, i -> !"detail".equals(i.getColumn()));
        queryWrapper.orderByDesc(Note::getCreateTime);

        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setPage(this.page(page, queryWrapper));
        return noteDTO;
    }

    @Override
    public void deleteById(Long noteId) {
        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Note::getUserId, UserThreadLocal.get());
        queryWrapper.eq(Note::getId, noteId);

        this.remove(queryWrapper);
    }

    @Override
    public void updateNote(UpdateNoteDTO updateNoteDTO) {
        LambdaUpdateWrapper<Note> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Note::getUserId, UserThreadLocal.get());
        updateWrapper.eq(Note::getId, updateNoteDTO.getId());
        updateWrapper.set(Note::getTitle, updateNoteDTO.getTitle());
        updateWrapper.set(Note::getDetail, updateNoteDTO.getDetail());
        updateWrapper.set(Note::getUpdateTime, LocalDateTime.now());

        this.update(updateWrapper);
    }

    @Override
    public NoteDTO get(Long noteId) {

        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Note::getUserId, UserThreadLocal.get());
        queryWrapper.eq(Note::getId, noteId);
        Note note = this.getOne(queryWrapper);
        if (note == null) {
            throw new APIException(Constant.NOT_FOUND_NOTE_ERROR);
        }

        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setNote(note);
        return noteDTO;
    }
}