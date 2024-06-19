package com.todo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todo.dto.timeThing.*;
import com.todo.mapper.TimeThingMapper;
import com.todo.pojo.TimeThing;
import com.todo.service.TimeThingService;
import com.todo.utils.UserThreadLocal;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TimeThingServiceImpl extends ServiceImpl<TimeThingMapper, TimeThing> implements TimeThingService {

    @Override
    public TimeThingDTO getByPage(GetTimeThingByPageDTO dto) {
        Page<TimeThing> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        String keyword = dto.getKeyword();
        LambdaQueryWrapper<TimeThing> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(TimeThing::getUpdateTime);
        queryWrapper.orderByDesc(TimeThing::getCreateTime);
        TimeThingDTO timeThingDTO = new TimeThingDTO();
        timeThingDTO.setPage(this.page(page, queryWrapper));
        return timeThingDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(InsertDTO dto) {
        TimeThing timeThing = new TimeThing();
        BeanUtils.copyProperties(dto, timeThing);
        timeThing.setUserId(UserThreadLocal.get());
        timeThing.setCreateTime(LocalDateTime.now());
        timeThing.setUpdateTime(LocalDateTime.now());
        this.save(timeThing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        LambdaQueryWrapper<TimeThing> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TimeThing::getId, id);
        queryWrapper.eq(TimeThing::getUserId, UserThreadLocal.get());
        this.remove(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(UpdateDTO dto) {
        LambdaUpdateWrapper<TimeThing> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(TimeThing::getUserId, UserThreadLocal.get());
        updateWrapper.eq(TimeThing::getId, dto.getId());
        updateWrapper.set(TimeThing::getUpdateTime, LocalDateTime.now());
        updateWrapper.set(TimeThing::getTitle, dto.getTitle());
        updateWrapper.set(TimeThing::getContent, dto.getContent());
        updateWrapper.set(TimeThing::getThingTime, dto.getThingTime());
        updateWrapper.set(TimeThing::getStartTime, dto.getStartTime());
        updateWrapper.set(TimeThing::getEndTime, dto.getEndTime());
        this.update(updateWrapper);
    }

    @Override
    public TimeThingDTO getByDate(GetTimeThingByDateDTO dto) {
        LambdaQueryWrapper<TimeThing> queryWrapper = new LambdaQueryWrapper<>();
        LocalDate thingDate = dto.getThingDate();
        queryWrapper.ge(TimeThing::getThingTime, thingDate);
        LocalDate localDate = thingDate.plusDays(1);
        queryWrapper.lt(TimeThing::getThingTime, localDate);
        queryWrapper.orderByDesc(TimeThing::getEndTime);
        TimeThingDTO timeThingDTO = new TimeThingDTO();
        timeThingDTO.setTimeThings(this.list(queryWrapper));
        return timeThingDTO;
    }

}