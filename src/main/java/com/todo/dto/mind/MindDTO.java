package com.todo.dto.mind;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.todo.pojo.Mind;
import lombok.Data;

@Data
public class MindDTO {
    private IPage<Mind> page;

    private Mind mind;
}
