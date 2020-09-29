package com.lovecyy.wallet.item.task;

import com.lovecyy.wallet.item.service.TWalletService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 块扫描任务
 * @author Yakir
 */
@Component
@RequiredArgsConstructor
public class BlockScanTask {


    private static final Logger log= LoggerFactory.getLogger(BlockScanTask.class);



    private final TWalletService tWalletService;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void blockTask(){
        log.info("扫块监听开始");
        try {
            tWalletService.monitorEthBlock();
        } catch (IOException e) {
            log.error("扫块监听出现异常",e);
        }
        log.info("扫块监听结束");
    }
}
