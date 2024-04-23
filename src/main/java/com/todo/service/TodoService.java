package com.todo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.todo.dto.todo.*;
import com.todo.pojo.Todo;
import org.quartz.Scheduler;

public interface TodoService extends IService<Todo> {
    /**
     * 根据开始日期和结束日期获取待办事项
     * @param getTodoDTO 开始日期和结束日期
     * @return 该事件段内的所有待办事项
     */
    TodoDTO getByMonth(GetTodoDTO getTodoDTO);

    /**
     * 根据待办事项id修改该待办事项的设置
     * @param updateTodoDTO 待办事项设置
     */
    void updateTodoById(UpdateTodoDTO updateTodoDTO);

    /**
     * 新增待办事项
     * @param addTodoDTO 待办事项设置
     * @return 新增的待办事项的id
     */
    Long add(AddTodoDTO addTodoDTO);

    /**
     * 根据待办事项id删除待办事项
     * @param todoId 待办事项id
     */
    void deleteById(Long todoId);

    /**
     * 获取当前月份的待办事项数量
     * @return 数量
     */
    TodoDTO getThisMonthTodo();

    /**
     * 获取所有的待办事项数量
     * @return 数量
     */
    TodoDTO getAllTodo();

    /**
     * 获取今天的待办事项数量
     * @return 数量
     */
    TodoDTO getTodayTodo();

    /**
     * 获取超时未完成的待办事项数量
     * @return 数量
     */
    TodoDTO getDelayTodo();

    /**
     * 获取完成的待办事项数量
     * @return 数量
     */
    TodoDTO getDoneTodo();

    /**
     * 根据传来的参数进行批量生成待办事项
     * @param batchGenerateTodoDTO 待办事项配置以及生成参数
     */
    void batchGenerate(BatchGenerateTodoDTO batchGenerateTodoDTO);

    /**
     * 根据待办事项id设置开始时间
     * @param todoId 待办事项id
     */
    void startTodo(Long todoId);

    /**
     * 根据待办事项id设置结束时间
     * @param todoId 待办事项id
     */
    void endTodo(Long todoId);

    /**
     * 判断是否可以满足加入到定时任务的条件
     * @param todo 待办事项
     * @param userId 用户id
     * @return 是否可以加入
     */
    boolean canAddQuartz(Todo todo, Long userId);

    /**
     * 将待办事项添加到定时任务中
     * @param scheduler 定时器
     * @param todo 待办事项
     */
    void addQuartz(Scheduler scheduler, Todo todo, Long user);
}