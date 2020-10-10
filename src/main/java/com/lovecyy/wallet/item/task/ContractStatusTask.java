package com.lovecyy.wallet.item.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lovecyy.wallet.item.common.utils.FormatConvert;
import com.lovecyy.wallet.item.common.utils.Web3JUtilWrapper;
import com.lovecyy.wallet.item.model.pojo.TContract;
import com.lovecyy.wallet.item.model.pojo.TTransaction;
import com.lovecyy.wallet.item.model.pojo.TUserRelationContract;
import com.lovecyy.wallet.item.model.pojo.TWallet;
import com.lovecyy.wallet.item.service.TContractService;
import com.lovecyy.wallet.item.service.TTransactionService;
import com.lovecyy.wallet.item.service.TUserRelationContractService;
import com.lovecyy.wallet.item.service.TWalletService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 合约状态扫描 每一分钟扫描一次
 */
@Component
@RequiredArgsConstructor
public class ContractStatusTask {

    private static final Logger log= LoggerFactory.getLogger(ContractStatusTask.class);


    private final TContractService tContractService;
    private final TTransactionService tTransactionService;
    private final TWalletService tWalletService;
    private final TUserRelationContractService tUserRelationContractService;

    private final Web3JUtilWrapper web3JUtilWrapper;

    /**
     * 每3分钟扫1次 每笔交易扫描5次 若是5次都未更新状态 则会更新为失败
     */
    @Scheduled(cron = "0 0/3 * * * ?")
    public void contractStatusTask(){
        try {
            log.info("合约状态扫描开始");
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
                if (transactionReceiptByHash==null){
                    log.info("合约状态扫描，当前交易状态为查询到[{}]",txHash);
                    continue;
                }
                handleCTAndUCRelation(transactionReceiptByHash);
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
        } catch (Exception e) {
            log.error("合约状态查询出现异常",e);
        }


    }

    /**
     * 处理用户交易记录 和用户合约关系绑定
     * @param transactionReceipt
     */
    private void handleCTAndUCRelation(TransactionReceipt transactionReceipt) {
        String fromAddress = transactionReceipt.getFrom();
        //判断转出转入用户是否在我们钱包内
        boolean isTrue= tWalletService.validateAddressInWallet(fromAddress);
        if (!isTrue){
            log.info("此交易非本钱包内账户[{}]",transactionReceipt);
            return;
        }
        String txHash = transactionReceipt.getTransactionHash();
        boolean existsTxHash = tTransactionService.isExistsTxHash(txHash);
        if (existsTxHash){
            log.info("交易hash已经存在,交易信息[{}]",transactionReceipt);
            return;
        }
        EthTransaction ethTransaction = web3JUtilWrapper.getTransactionByHash(txHash);
        if (!ethTransaction.getTransaction().isPresent()){
            log.info("交易未查询到[{}]",transactionReceipt);
            return;
        }
        Transaction transaction = ethTransaction.getTransaction().get();
        String contractAddress = transactionReceipt.getContractAddress();
        //若合约地址不为空 则进行处理
        if (!StringUtils.isEmpty(contractAddress)){
            BigInteger status;
            //真实使用gas
            BigInteger gasUd = transactionReceipt.getGasUsed();
            //gaslimit
            BigInteger gasLimit = transaction.getGas();
            if (transactionReceipt.getStatus()!=null){
                status = FormatConvert.hexToDec(transactionReceipt.getStatus());
            }
            else{
                status=gasUd.compareTo(gasLimit)<=0?BigInteger.ONE:BigInteger.ZERO;
            }
            BigInteger valueWei = transaction.getValue();
            //转账金额
            BigDecimal value = FormatConvert.WeiTOEth(valueWei.toString());
            BigInteger blockNumber = transaction.getBlockNumber();
            BigInteger gasPriceWei = transaction.getGasPrice();
            BigInteger gasPrice = FormatConvert.WeiTOGWei(gasPriceWei);
            Date date = new Date();
            //构造交易信息
            TTransaction tTransaction = TTransaction.builder().tradingHash(txHash)
                    .fromAddress(fromAddress)
                    .toAddress(null)
                    .amount(value)
                    .status(status.intValue())
                    .type(3)
                    .blockNumber(blockNumber)
                    .contractAddress(contractAddress).gasUse(gasUd).gasPrice(gasPrice)
                    .gmtCreate(date).gmtModified(date).build();
            tTransactionService.save(tTransaction);
            log.info("保存交易信息落库[{}]",transaction);
            //执行合约用户关系绑定判断 根据
            TWallet fromWallet = tWalletService.getWalletByAddress(fromAddress);
            if (fromWallet!=null&&transactionReceipt.isStatusOK()){
                TUserRelationContract tUserRelationContract=new TUserRelationContract();
                tUserRelationContract.setUid(fromWallet.getUid());
                tUserRelationContract.setWalletAddress(fromAddress);
                tUserRelationContract.setContractAddress(contractAddress);
                boolean existsRelation = tUserRelationContractService.isExistsRelation(tUserRelationContract);
                if (!existsRelation){
                    log.info("关联用户与合同关系[{}]",tUserRelationContract);
                    tUserRelationContractService.saveInfo(tUserRelationContract);
                }
            }
        }

    }


}
