package com.lovecyy.wallet.item.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lovecyy.wallet.item.model.pojo.TContract;
import com.lovecyy.wallet.item.service.TContractService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 合约状态扫描
 */
@Component
@RequiredArgsConstructor
public class ContractDeployTimeTask {

    private static final Logger log= LoggerFactory.getLogger(ContractDeployTimeTask.class);


    private final TContractService tContractService;

    /**
     * 15分钟1次把15分钟以前的合约状态为0的更新为失败
     */
    @Scheduled(cron = "0 0/15 * * * ?")
    public void contractStatusTask(){
        try {
            LocalDateTime localDateTime=LocalDateTime.now();
            LocalDateTime beforeTime = localDateTime.minusMinutes(15);
            log.info("开始更新超时合约状态为失败 起始时间[{}]",beforeTime);
            updateExpireTxHash(beforeTime);
            log.info("结束更新超时合约状态");
        } catch (Exception e) {
            log.error("更新超时合约状态失败",e);
        }


    }

    private void updateExpireTxHash(LocalDateTime expireTime) {
        //更新15分钟以前的以前的状态为0的都为2
        try {
            UpdateWrapper<TContract> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set(TContract.COL_STATUS,2);
            updateWrapper.eq(TContract.COL_STATUS,0);
            updateWrapper.lt(TContract.COL_GMT_CREATE,expireTime.toString());
            tContractService.update( updateWrapper);
        } catch (Exception e) {
           log.error("更新15分钟前合约状态异常 起始时间",e);
        }

    }
}
