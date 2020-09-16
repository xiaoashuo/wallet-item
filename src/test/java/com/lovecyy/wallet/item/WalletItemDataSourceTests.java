package com.lovecyy.wallet.item;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lovecyy.wallet.item.model.pojo.TContract;
import com.lovecyy.wallet.item.service.TConfigService;
import com.lovecyy.wallet.item.service.TContractService;
import com.lovecyy.wallet.item.service.TFeedbackService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.time.LocalDateTime;

@SpringBootTest
public class WalletItemDataSourceTests {

     @Autowired
     private TFeedbackService tFeedbackService;
     @Autowired
     private  TContractService tContractService;
     @Autowired
     private TConfigService tConfigService;

     @Test
     public void testConfig(){
//          BigInteger currentBlockNumber = tConfigService.getLastBlockNumber();
//          System.out.println(currentBlockNumber);
          boolean b = tConfigService.updateBlockNumber(BigInteger.TEN);
          System.out.println(b);
     }

     @Test
     public void context(){
//          TFeedback entity=TFeedback.builder().content("不能用")
//                  .gmtCreate(new Date())
//                  .gmtModified(new Date())
//                  .uid(1).username("zhangsan")
//                  .build();
//          boolean save = tFeedbackService.save(entity);
//          System.out.println(save);
          System.out.println("啊啊");
          LocalDateTime localDateTime=LocalDateTime.now();
          LocalDateTime beforeTime = localDateTime.minusMinutes(15);
//          QueryWrapper<TContract> queryWrapper=new QueryWrapper<>();
//          queryWrapper.eq(TContract.COL_STATUS,0);
//          queryWrapper.le(TContract.COL_GMT_CREATE,localDateTime.toString());
//          queryWrapper.ge(TContract.COL_GMT_CREATE,beforeTime.toString());
//          List<TContract> list = tContractService.list(queryWrapper);
//          System.out.println(list);
//          List<TContract> list1 = tContractService.list();
//          System.out.println(list1);
          try {
               UpdateWrapper<TContract> updateWrapper = new UpdateWrapper<>();
               updateWrapper.set(TContract.COL_STATUS,1);
               updateWrapper.eq(TContract.COL_STATUS,0);
               updateWrapper.lt(TContract.COL_GMT_CREATE,localDateTime.toString());
               tContractService.update( updateWrapper);
          } catch (Exception e) {

          }
     }

}
