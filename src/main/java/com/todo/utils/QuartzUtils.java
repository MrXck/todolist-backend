package com.todo.utils;

import cn.hutool.core.date.DateUtil;
import com.todo.exception.APIException;
import com.todo.task.Schedule;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.util.*;

public class QuartzUtils {

    /**
     * Maven 依赖
     *         <dependency>
     *             <groupId>org.springframework.boot</groupId>
     *             <artifactId>spring-boot-starter-quartz</artifactId>
     *         </dependency>
     */

    /**
     * 创建定时任务使用cron表达式 定时任务创建之后默认启动状态
     *
     * @param scheduler 调度器
     * @param schedule  定时任务信息类
     * @param taskClassPath 定时任务类全限定名
     */
    public static void createScheduleJobWithCron(Scheduler scheduler, Schedule schedule, String cron, String taskClassPath) {
        try {
            //获取到定时任务的执行类  必须是类的绝对路径名称
            //定时任务类需要是job类的具体实现 QuartzJobBean是job的抽象类。
            // Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName("com.demo.task.AutomaticTask");
            Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(taskClassPath);
            // 构建定时任务信息 使用id作为任务唯一值，在执行时可以拿到。
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(schedule.getId().toString())
                    .withDescription(schedule.getName()).build();
            // 设置定时任务执行方式
            CronScheduleBuilder scheduleBuilder = null;
            try {
                scheduleBuilder = CronScheduleBuilder
                        .cronSchedule(cron);
            } catch (Exception e) {
                e.printStackTrace();
                throw new APIException("cron表达式配置错误");
            }
            //.withMisfireHandlingInstructionDoNothing();
            // 构建触发器trigger
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(schedule.getId().toString())
                    .startAt(schedule.getStartDate())
                    .endAt(schedule.getEndDate())
                    .withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (ClassNotFoundException e) {
            System.out.println("定时任务类路径出错：请输入类的绝对路径");
        } catch (SchedulerException e) {
            System.out.println("创建定时任务出错：" + e.getMessage());
        }
    }

    /**
     * 创建定时任务使用cron表达式 定时任务创建之后默认启动状态
     *
     * @param scheduler 调度器
     * @param schedule  定时任务信息类
     * @param taskClassPath 定时任务类全限定名
     */
    public static void createScheduleJobWithDateTime(Scheduler scheduler, Schedule schedule, String taskClassPath) {
        try {
            //获取到定时任务的执行类  必须是类的绝对路径名称
            //定时任务类需要是job类的具体实现 QuartzJobBean是job的抽象类。
            // Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName("com.demo.task.AutomaticTask");
            Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(taskClassPath);
            // 构建定时任务信息 使用id作为任务唯一值，在执行时可以拿到。
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(schedule.getId().toString())
                    .withDescription(schedule.getName()).build();
            //.withMisfireHandlingInstructionDoNothing();
            // 构建触发器trigger
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(schedule.getId().toString())
                    .startAt(schedule.getStartDate())
                    .endAt(schedule.getEndDate())
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (ClassNotFoundException e) {
            System.out.println("定时任务类路径出错：请输入类的绝对路径");
        } catch (SchedulerException e) {
            System.out.println("创建定时任务出错：" + e.getMessage());
        }
    }

    /**
     * 根据任务Id暂停定时任务
     *
     * @param scheduler 调度器
     * @param jobId     定时任务名称
     * @throws SchedulerException
     */
    public static void pauseScheduleJob(Scheduler scheduler, String jobId) {
        JobKey jobKey = JobKey.jobKey(jobId);
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            System.out.println("暂停定时任务出错：" + e.getMessage());
        }
    }

    /**
     * 根据任务Id恢复定时任务
     *
     * @param scheduler 调度器
     * @param jobId     定时任务id
     * @throws SchedulerException
     */
    public static void resumeScheduleJob(Scheduler scheduler, String jobId) {
        JobKey jobKey = JobKey.jobKey(jobId);
        try {
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            System.out.println("启动定时任务出错：" + e.getMessage());
        }
    }


    /**
     * 根据任务id立即运行一次定时任务
     *
     * @param scheduler 调度器
     * @param jobId     定时任务id
     * @throws SchedulerException
     */
    public static void runOnce(Scheduler scheduler, String jobId) {
        JobKey jobKey = JobKey.jobKey(jobId);
        try {
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            System.out.println("运行定时任务出错：" + e.getMessage());
        }
    }

    /**
     * 更新定时任务
     *
     * @param scheduler 调度器
     * @param schedule  定时任务信息类
     * @throws SchedulerException
     */
    public static void updateScheduleJob(Scheduler scheduler, Schedule schedule, String cron) {
        try {
            //获取到对应任务的触发器
            TriggerKey triggerKey = TriggerKey.triggerKey(schedule.getId().toString());
            //设置定时任务执行方式
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            //重新构建任务的触发器trigger
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            //重置对应的job
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            System.out.println("更新定时任务出错：" + e.getMessage());
        }
    }

    /**
     * 根据定时任务id从调度器当中删除定时任务
     *
     * @param scheduler 调度器
     * @param jobId     定时任务名称
     */
    public static void deleteScheduleJob(Scheduler scheduler, String jobId) {
        JobKey jobKey = JobKey.jobKey(jobId);
        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            System.out.println("删除定时任务出错：" + e.getMessage());
        }
    }

    public static List<Map<String, String>> getAllJobs(Scheduler scheduler) {
        List<Map<String, String>> jobList = new ArrayList<>();
        try {
            List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
            for (String groupName : triggerGroupNames) {
                //组装group的匹配，为了模糊获取所有的triggerKey或者jobKey
                GroupMatcher groupMatcher = GroupMatcher.groupEquals(groupName);
                //获取所有的triggerKey
                Set<TriggerKey> triggerKeySet = scheduler.getTriggerKeys(groupMatcher);
                for (TriggerKey triggerKey : triggerKeySet) {
                    //通过triggerKey在scheduler中获取trigger对象
                    CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                    //获取trigger拥有的Job
                    JobKey jobKey = trigger.getJobKey();
                    JobDetailImpl jobDetail = (JobDetailImpl) scheduler.getJobDetail(jobKey);
                    //组装需要显示的数据
                    Map<String, String> jobMap = new HashMap<>();
                    //分组名称
                    jobMap.put("groupName", groupName);
                    //定时任务名称
                    jobMap.put("jobDetailName", jobDetail.getName());
                    //cron表达式
                    String cronExpression = trigger.getCronExpression();
                    jobMap.put("jobCronExpression", cronExpression);
                    //时区
                    jobMap.put("timeZone", trigger.getTimeZone().getID());
                    //下次运行时间
                    CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
                    cronTriggerImpl.setCronExpression(cronExpression);
                    List<Date> dates = TriggerUtils.computeFireTimes(cronTriggerImpl, null, 20);
                    jobMap.put("nextRunDateTime", DateUtil.format(dates.get(0), "yyyy-MM-dd HH:mm:ss"));
                    jobList.add(jobMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobList;
    }
}