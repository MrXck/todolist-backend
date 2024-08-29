package com.todo.dto.operationLog;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.todo.pojo.OperationLog;
import lombok.Data;

import java.util.List;

@Data
public class OperationLogDTO {

    private Page<OperationLog> page;

    private List<OperationLogVO> list;
}
