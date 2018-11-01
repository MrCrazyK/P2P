package com.lanou.timer;

import com.lanou.service.loan.IncomeRecordService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ClassName : TimerManager
 * PackageName : com.lanou.timer
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/10/5 15:59
 * @Version : 1.0
 */
public class TimerManager {
    private Logger logger = LogManager.getLogger(TimerManager.class);

    @Autowired
    private IncomeRecordService incomeRecordService;

    @Scheduled(cron = "0/5 * * * * *")
    public void generateIncomePlan() {
        logger.info("--------------生成收益计划开始-------------");

        incomeRecordService.generateIncomePlan();

        logger.info("--------------生成收益计划结束-------------");
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void generateIncomeBack() {
        logger.info("--------------收益返还开始-------------");

        incomeRecordService.generateIncomeBack();

        logger.info("--------------收益返还结束-------------");
    }
}
