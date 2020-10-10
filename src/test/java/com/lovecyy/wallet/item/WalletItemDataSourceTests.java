package com.lovecyy.wallet.item;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lovecyy.wallet.item.model.dto.TransactionDTO;
import com.lovecyy.wallet.item.model.pojo.TContract;
import com.lovecyy.wallet.item.model.qo.TransactionQO;
import com.lovecyy.wallet.item.service.*;
import com.lovecyy.wallet.item.task.ContractStatusTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class WalletItemDataSourceTests {

     @Autowired
     private TFeedbackService tFeedbackService;
     @Autowired
     private  TContractService tContractService;
     @Autowired
     private TConfigService tConfigService;

     @Autowired
     private TWalletService tWalletService;
     @Autowired
     private TTransactionService tTransactionService;

     @Autowired
     private TUserRelationContractService tUserRelationContractService;

//     @Test
//     public void listContract(){
//          QrCodeUtil.generate("https://hutool.cn/", 300, 300, FileUtil.file("d:/qrcode.jpg"));
//
//          List<TContract> tContracts = tContractService.listByUserId(1);
//          System.out.println(tContracts);
//     }



     @Autowired
     private TUsersService tUsersService;

     @Autowired
     private ContractStatusTask contractStatusTask;

     @Test
     public void testDeploy(){
          contractStatusTask.contractStatusTask();
     }

     @Test
     public void testConfig(){
          TransactionQO transactionQO = new TransactionQO();
          transactionQO.setAddress("0x292c6ddb3675ceb0e4d2a0d82e3e7115cfe44efb");
          transactionQO.setOptionType(0);
          transactionQO.setPageNum(0);
          transactionQO.setPageSize(10);
          transactionQO.setTransactionType(1);
          TransactionDTO transactionDTO = tTransactionService.listByTypeAndPage(transactionQO);
          System.out.println(transactionDTO);
//          TUsers build = TUsers.builder().username("username").password("password").build();
//
//          int insert = tUsersService.getBaseMapper().insert(build);
//          System.out.println(build);
//          TTransaction tTransaction = TTransaction.builder().tradingHash("txHash")
//                  .fromAddress("fromAddress")
//                  .toAddress(null)
//                  .amount(BigDecimal.TEN)
//                  .status(1)
//                  .type(3)
//                  .blockNumber(BigInteger.TEN)
//                  .contractAddress("contractAddress").gasUse(BigInteger.TEN).gasPrice(BigInteger.TEN)
//                  .gmtCreate(new Date()).gmtModified(new Date()).build();
//          tTransactionService.save(tTransaction);

//          boolean existsContractHash = tContractService.isExistsContractHash("01xb0958a298380de120130f14d9261a828ed30fe5cfba2f6f27e8abd84f8dc2104");
//          System.out.println(existsContractHash);
//          boolean b = tContractService.updateContractStatus(2, "0xb0958a298380de120130f14d9261a828ed30fe5cfba2f6f27e8abd84f8dc2104");
//          System.out.println(b);
//          TUserRelationContract build = TUserRelationContract.builder()
//                  .uid(2).walletAddress("123").contractAddress("456")
//                  .build();
//          tUserRelationContractService.save(build);
//
//          boolean existsRelation = tUserRelationContractService.isExistsRelation(build);
//          System.out.println(existsRelation);
//          boolean b = tWalletService.validateAddressInWallet("121","0x146d512168d6d0602c19ff63257cdf8cb1811be0");
//          System.out.println(b);
//          BigInteger currentBlockNumber = tConfigService.getLastBlockNumber();
//          System.out.println(currentBlockNumber);
//          boolean b = tConfigService.updateBlockNumber(BigInteger.TEN);
//          System.out.println(b);

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
