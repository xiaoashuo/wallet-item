package com.lovecyy.wallet.item.task;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lovecyy.wallet.item.common.utils.Web3JUtil;
import com.lovecyy.wallet.item.common.utils.Web3JUtilWrapper;
import com.lovecyy.wallet.item.model.pojo.TContract;
import com.lovecyy.wallet.item.service.TContractService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 合约状态扫描
 */
@Component
@RequiredArgsConstructor
public class ContractStatusTask {

    private static final Logger log= LoggerFactory.getLogger(ContractStatusTask.class);


    private final TContractService tContractService;

    private final Web3JUtilWrapper web3JUtilWrapper;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void contractStatusTask(){
        LocalDateTime localDateTime=LocalDateTime.now();
        LocalDateTime beforeTime = localDateTime.minusMinutes(15);
        QueryWrapper<TContract> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(TContract.COL_STATUS,0);
        queryWrapper.le(TContract.COL_GMT_CREATE,localDateTime.toString());
        queryWrapper.ge(TContract.COL_GMT_CREATE,beforeTime.toString());
        List<TContract> list = tContractService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)){
            return;
        }
        List<TContract> updateContracts=new ArrayList<>();
        for (TContract tContract : list) {
            String txHash = tContract.getTxHash();
            TransactionReceipt transactionReceiptByHash = web3JUtilWrapper.getTransactionReceiptByHash(txHash);
            if (transactionReceiptByHash.isStatusOK()){
                tContract.setAddress(transactionReceiptByHash.getContractAddress());
                tContract.setStatus(1);
                tContract.setGmtModified(new Date());
            }else{
                tContract.setStatus(2);
                tContract.setGmtModified(new Date());
            }
            updateContracts.add(tContract);
        }
        if (!CollectionUtils.isEmpty(updateContracts)){
            tContractService.saveOrUpdateBatch(updateContracts);
        }


    }



}
