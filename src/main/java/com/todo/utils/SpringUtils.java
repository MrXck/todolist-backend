package com.todo.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static void set(ApplicationContext applicationContext){
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass){
        return context.getBean(beanClass);
    }

    public static <T> T getBean(String beanName){
        return (T) context.getBean(beanName);
    }

    public static <T> T getBean(String beanName, Class<T> beanClass){
        return (T) context.getBean(beanName, beanClass);
    }
}
