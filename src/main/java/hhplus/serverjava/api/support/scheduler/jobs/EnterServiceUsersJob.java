package hhplus.serverjava.api.support.scheduler.jobs;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.api.support.scheduler.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.ApplicationContext;


import static hhplus.serverjava.api.support.response.BaseResponseStatus.SCHEDULER_ERROR;

@Slf4j
public class EnterServiceUsersJob implements Job {
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 서비스를 이용중인 유저가 100명 미만일 때 대기열 유저 입장
        try {
            SchedulerContext schedulerContext = context.getScheduler().getContext();

            ApplicationContext applicationContext = (ApplicationContext) schedulerContext.get("applicationContext");

            SchedulerService schedulerService = applicationContext.getBean(SchedulerService.class);

            schedulerService.enterServiceUser();

        } catch (SchedulerException e) {
            log.error("EnterServiceUsersJob Error");
            log.error(e.getMessage());
            throw new BaseException(SCHEDULER_ERROR);
        }
    }
}