package com.todo.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;

public class LocalTimeValidator implements ConstraintValidator<ValidLocalTime, LocalTime> {

    @Override
    public boolean isValid(LocalTime value, ConstraintValidatorContext context) {
        // 这里可以定义自定义的日期验证逻辑
        // 例如，如果要确保日期在当前日期之后，可以使用以下代码：
        // return value != null && value.isAfter(LocalDate.now());
        return value != null;
    }
}