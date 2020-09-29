package com.lovecyy.wallet.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.lovecyy.wallet.item.common.constants.ETHConstants;
import com.lovecyy.wallet.item.common.convert.TWalletConvert;
import com.lovecyy.wallet.item.common.utils.FormatConvert;
import com.lovecyy.wallet.item.common.utils.TokenUtilWrapper;
import com.lovecyy.wallet.item.common.utils.Web3JUtilWrapper;
import com.lovecyy.wallet.item.model.dto.AccountDTO;
import com.lovecyy.wallet.item.model.dto.KeystoreDto;

import com.lovecyy.wallet.item.model.dto.TWalletDto;
import com.lovecyy.wallet.item.model.dto.TokenDTO;
import com.lovecyy.wallet.item.model.pojo.TTransaction;
import com.lovecyy.wallet.item.model.pojo.TUserRelationContract;
import com.lovecyy.wallet.item.model.qo.TokenQO;
import com.lovecyy.wallet.item.service.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lovecyy.wallet.item.mapper.TWalletMapper;
import com.lovecyy.wallet.item.model.pojo.TWallet;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

@RequiredArgsConstructor
@Service
public class TWalletServiceImpl extends ServiceImpl<TWalletMapper, TWallet> implements TWalletService{

    private static final Logger log= LoggerFactory.getLogger(TWalletServiceImpl.class);


    private  final Web3JUtilWrapper web3JUtilWrapper;

    private  final TokenUtilWrapper tokenUtilWrapper;

    private  final TConfigService tConfigService;
    private  final TContractService tContractService;

    private  final TTransactionService tTransactionService;
    private  final TUserRelationContractService tUserRelationContractService;

    @Value("${web3j.keystore:''}")
    public String walletKeyStorePath;

    /**
     * 创建钱包
     * @param userId
     * @param password
     * @return
     * @throws Exception
     */
    @Override
    public TWalletDto createWallet(Integer userId,String password) throws Exception {
        KeystoreDto wallet = web3JUtilWrapper.createWalletWithMnemonic(password, walletKeyStorePath);
        String content = wallet.getContent();
        String filename = wallet.getFilename();
        String mnemonic = wallet.getMnemonic();
        Credentials credentials = web3JUtilWrapper.openWalletByJSON(password, content);
        String address = credentials.getAddress();
        String walletPrivateKey = web3JUtilWrapper.getWalletPrivateKey(credentials);
        Date date = new Date();
        TWallet tWallet = TWallet.builder()
                .uid(userId).address(address).keyStore(content)
                .name(filename).mnemonic(mnemonic).privateKey(walletPrivateKey)
                .password(password).gmtCreate(date).gmtModified(date).build();
        TWalletDto potodto = TWalletConvert.INSTANCE.POTODTO(tWallet);
        boolean result = SqlHelper.retBool(this.getBaseMapper().insert(tWallet));
        return result?potodto:null;
    }

    @Override
    public List<TWalletDto> getWallets(Integer userId) throws Exception {
        QueryWrapper<TWallet> conditions = new QueryWrapper<>();
        conditions.eq(TWallet.COL_UID,userId);
        List<TWallet> tWallets = this.getBaseMapper().selectList(conditions);
        return tWallets.stream().map(e -> {
            TWalletDto tWalletDto = new TWalletDto();
            tWalletDto.setId(e.getId());
            tWalletDto.setAddress(e.getAddress());
            return tWalletDto;
        }).collect(Collectors.toList());
    }

    @Override
    public TWallet getWallet(Integer userId, String address) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(TWallet.COL_UID,userId);
        queryWrapper.eq(TWallet.COL_ADDRESS,address);
        return this.getBaseMapper().selectOne(queryWrapper);
    }

    @Override
    public boolean delWalletById(Integer userId, Integer primaryId) {
        QueryWrapper<TWallet> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(TWallet.COL_UID,userId);
        queryWrapper.eq(TWallet.COL_ID,primaryId);
        return SqlHelper.retBool(this.getBaseMapper().delete(queryWrapper));
    }

    @Override
    public AccountDTO balance(String address) {
        BigDecimal balanceByAddress = web3JUtilWrapper.getBalanceByAddress(address);
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAddress(address);
        accountDTO.setBalance(balanceByAddress);
        return accountDTO;
    }

    @Override
    public TokenDTO transfer(Integer userId, TokenQO tokenQO) throws Exception {
        TWallet wallet = getWallet(userId, tokenQO.getFromAddress());
        Assert.notNull(wallet,"用户钱包不正确");
        String keyStore = wallet.getKeyStore();
        String password = wallet.getPassword();
        Credentials credentials = web3JUtilWrapper.openWalletByJSON(password, keyStore);
        String txHash;
        if (tokenQO.getGasPrice()==null){
            txHash = web3JUtilWrapper.sendTransaction(credentials, tokenQO.getToAddress(), tokenQO.getAmount());
        }else{
            txHash = web3JUtilWrapper.sendTransaction(credentials,tokenQO.getToAddress(),tokenQO.getAmount(),tokenQO.getGasPrice());
        }
        return new TokenDTO(tokenQO.getFromAddress(),tokenQO.getToAddress(),txHash,tokenQO.getAmount());
    }

    @Override
    public TWallet getWalletByAddress(String address) {
        QueryWrapper<TWallet> queryCondition=new QueryWrapper<>();
        queryCondition.eq(TWallet.COL_ADDRESS,address);
        return this.getBaseMapper().selectOne(queryCondition);
    }

    /**
     * 验证转出转入是否为钱包内地址 不为则不进行记录
     * @param address
     * @return 存在返回true 否在false
     */
    @Override
    public boolean validateAddressInWallet(String... address) {
        Integer isExist = this.getBaseMapper().validateAddressInWallet(address);
        return isExist!=null?true:false;
    }

    @Override
    public void monitorEthBlock() throws IOException {
        //上次轮询的高度
        BigInteger lastBlockNumber = tConfigService.getLastBlockNumber();
        Assert.notNull(lastBlockNumber,"当前区块高度不能为空");
        //当前区块高度
        BigInteger currentBlockNumber = web3JUtilWrapper.getBlockNumber();
        log.info("最新区块高度：{},扫描区块高度：{}",lastBlockNumber, currentBlockNumber);
        boolean isContinue = validateBlockNumber(lastBlockNumber, currentBlockNumber);
        //返回false 及不到处理区间数 直接结束方法 执行
        if (!isContinue){ return; }
        //未来块
        BigInteger futureBlock = currentBlockNumber.add(BigInteger.ONE);
        //更新处理区块数
        tConfigService.updateBlockNumber(futureBlock);
        log.info("将未来块插入到记录[{}]",futureBlock);
        //批量处理块
        batchProcessBlock(lastBlockNumber,futureBlock);
    }

    /**
     * 批量处理块数据
     * @param _lastBlockNumber
     * @param condition
     */
    private void batchProcessBlock(BigInteger _lastBlockNumber, BigInteger condition) {
        int futureBlock = condition.intValue();
        int  lastBlockNumber= _lastBlockNumber.intValue();
        //遍历区块
        for (int i = lastBlockNumber; i < futureBlock; i++) {
            //获取块
            try {
                EthBlock.Block block = web3JUtilWrapper.getBlockByNumber(BigInteger.valueOf(i));
                if (block==null){continue;}
                handleBlockData(block);
            } catch (IOException e) {
                log.error("当前区块处理异常,进行补偿机制,索引号[{}]",i);
            }
        }
    }

    /**
     * 处理块数据
     * @param block
     */
    private void handleBlockData(EthBlock.Block block) {
        List<EthBlock.TransactionResult> transactions = block.getTransactions();
        //遍历当前块中所有交易
        for (EthBlock.TransactionResult tx : transactions) {
           if (!(tx instanceof EthBlock.TransactionObject)){
               continue;
           }
            //transaction: 块中的单笔交易
            EthBlock.TransactionObject transaction = (EthBlock.TransactionObject) tx;
            String toAddress = transaction.getTo();
            if (StringUtils.isEmpty(toAddress)){
                log.info("处理转出地址为空[{}]",transaction);
                //合约情况更新处理切换回定时任务
               // handleToAddressNull(transaction);
                continue;
            }
            //得到交易金额 如果小于等于0 则为代币交易 反之就是ETH的交易
            BigInteger value = transaction.getValue();
            if (value.compareTo(BigInteger.ZERO)<=0){
                //执行代币交易
                handleTokenTransaction(transaction);
            }else{
                //执行eth交易
                handleEthTransaction(transaction);
            }

        }

    }

    /**
     * 处理to地址为空的 可能为部署合约交易
     * @param transaction
     */
    private void handleToAddressNull(EthBlock.TransactionObject transaction) {
        //交易hash
        String txHash = transaction.getHash();
        boolean existsTxHash = tTransactionService.isExistsTxHash(txHash);
        if (existsTxHash){
            log.info("交易hash已经存在,交易信息[{}]",transaction);
            return;
        }
        //发起方账户
        String fromAddress = transaction.getFrom();
        //判断转出转入用户是否在我们钱包内
        boolean isTrue= validateAddressInWallet(fromAddress);
        if (!isTrue){
            log.info("此交易非本钱包内账户[{}]",transaction);
            return;
        }
        TransactionReceipt transactionReceipt = web3JUtilWrapper.getTransactionReceiptByHash(txHash);
        String contractAddress = transactionReceipt.getContractAddress();
        //若合约地址不为空 则进行处理
        if (!StringUtils.isEmpty(contractAddress)){
            BigInteger status = FormatConvert.hexToDec(transactionReceipt.getStatus());
            BigInteger valueWei = transaction.getValue();
            //转账金额
            BigDecimal value = FormatConvert.WeiTOEth(valueWei.toString());
            BigInteger blockNumber = transaction.getBlockNumber();
            BigInteger gasPriceWei = transaction.getGasPrice();
            BigInteger gasPrice = FormatConvert.WeiTOGWei(gasPriceWei);
            BigInteger gasUse = transaction.getGas();
            Date date = new Date();
            //构造交易信息
            TTransaction tTransaction = TTransaction.builder().tradingHash(txHash)
                    .fromAddress(fromAddress)
                    .toAddress(null)
                    .amount(value)
                    .status(status.intValue())
                    .type(3)
                    .blockNumber(blockNumber)
                    .contractAddress(contractAddress).gasUse(gasUse).gasPrice(gasPrice)
                    .gmtCreate(date).gmtModified(date).build();
            tTransactionService.save(tTransaction);
            log.info("保存交易信息落库[{}]",transaction);
            //执行合约用户关系绑定判断 根据
            TWallet fromWallet = getWalletByAddress(fromAddress);
            if (fromWallet!=null){
                TUserRelationContract tUserRelationContract=new TUserRelationContract();
                tUserRelationContract.setUid(fromWallet.getUid());
                tUserRelationContract.setWalletAddress(fromAddress);
                tUserRelationContract.setContractAddress(contractAddress);
                boolean existsRelation = tUserRelationContractService.isExistsRelation(tUserRelationContract);
                if (!existsRelation){
                    log.info("关联用户与合同关系[{}]",tUserRelationContract);
                    tUserRelationContractService.save(tUserRelationContract);
                }
            }
            //更改用户部署合约状态 若存在 则更改
            boolean existsContractHash = tContractService.isExistsContractHash(txHash);
            if (existsContractHash){
                int contractStatus = status.intValue() == 1 ? 1 : 2;
                tContractService.updateContractStatus(contractStatus,txHash);
            }

        }
    }

    /**
     * 处理代币交易
     * @param transaction
     */
    private void handleTokenTransaction(EthBlock.TransactionObject transaction) {
        //交易hash
        String txHash = transaction.getHash();
        boolean existsTxHash = tTransactionService.isExistsTxHash(txHash);
        if (existsTxHash){
            log.info("交易hash已经存在,交易信息[{}]",transaction);
            return;
        }
        //获取input数据
        String input = transaction.getInput();
        if (StringUtils.isEmpty(input)||input.length()!=138){
            log.info("代币input数据异常[{}]",transaction);
            return;
        }
        //转出方
        String fromAddress = transaction.getFrom();
        //0xa9059cbb 为transfer  param: to - balance 事件ID，固定的，该事件为转账ID
        //0x23b872dd //transferFrom   param:from - to - balance
        //String methodName = input.substring(0, 10); 基于erc20
        //来地址
        //  String fromAddress = transaction.getFrom();
        //去地址
        String toAddress = "0x"+input.substring(34, 74);
        //判断转出转入用户是否在我们钱包内
        boolean isTrue= validateAddressInWallet(fromAddress,toAddress);
        if (!isTrue){
            log.info("此交易非本钱包内账户[{}]",transaction);
            return;
        }
        //合约地址
        String contractAddress = transaction.getTo();
        //数量
        String hexAmount = input.substring(74);
        BigInteger amountWithDecimals = FormatConvert.hexToDecNoPrefix(hexAmount);
        BigInteger contractDecimal = tokenUtilWrapper.getContractDecimal(contractAddress);
        //得到去除精度的
        BigInteger amount = FormatConvert.getNumberRemoveDecimal(amountWithDecimals, contractDecimal);
        TransactionReceipt transactionReceipt = web3JUtilWrapper.getTransactionReceiptByHash(txHash);
        BigInteger status = FormatConvert.hexToDec(transactionReceipt.getStatus());
        BigInteger gasUse = transaction.getGas();
        BigInteger gasPriceWei = transaction.getGasPrice();
        BigInteger gasPrice = FormatConvert.WeiTOGWei(gasPriceWei);
        BigInteger blockNumber = transaction.getBlockNumber();
        Date date = new Date();
        //构造交易信息
        TTransaction tTransaction = TTransaction.builder().tradingHash(txHash)
                .fromAddress(fromAddress)
                .toAddress(toAddress)
                .amount(BigDecimal.valueOf(amount.longValue()))
                .status(status.intValue())
                .type(2)
                .blockNumber(blockNumber)
                .contractAddress(contractAddress).gasUse(gasUse).gasPrice(gasPrice)
                .gmtCreate(date).gmtModified(date).build();
        tTransactionService.save(tTransaction);
        log.info("保存交易信息落库[{}]",transaction);
        //执行合约用户关系绑定判断 根据
        TWallet fromWallet = getWalletByAddress(fromAddress);
        if (fromWallet!=null){
            TUserRelationContract tUserRelationContract=new TUserRelationContract();
            tUserRelationContract.setUid(fromWallet.getUid());
            tUserRelationContract.setWalletAddress(fromAddress);
            tUserRelationContract.setContractAddress(contractAddress);
            boolean existsRelation = tUserRelationContractService.isExistsRelation(tUserRelationContract);
            if (!existsRelation){
                log.info("关联用户与合同关系[{}]",tUserRelationContract);
                tUserRelationContractService.save(tUserRelationContract);
            }
        }
        TWallet toWallet = getWalletByAddress(toAddress);
        if (toWallet!=null){
            TUserRelationContract tUserRelationContract=new TUserRelationContract();
            tUserRelationContract.setUid(toWallet.getUid());
            tUserRelationContract.setWalletAddress(toAddress);
            tUserRelationContract.setContractAddress(contractAddress);
            boolean existsRelation = tUserRelationContractService.isExistsRelation(tUserRelationContract);
            if (!existsRelation){
                log.info("关联用户与合同关系[{}]",tUserRelationContract);
                tUserRelationContractService.save(tUserRelationContract);
            }
        }


    }


    /**
     * 处理本币交易
     * @param transaction
     */
    private void handleEthTransaction(EthBlock.TransactionObject transaction) {
        //转出方
        String fromAddress = transaction.getFrom();
        //接收方
        String toAddress = transaction.getTo();
        //判断转出转入用户是否在我们钱包内
        boolean isTrue= validateAddressInWallet(fromAddress,toAddress);
        if (!isTrue){
            log.info("此交易非本钱包内账户[{}]",transaction);
            return;
        }
        //交易hash
        String txHash = transaction.getHash();
        boolean existsTxHash = tTransactionService.isExistsTxHash(txHash);
        if (existsTxHash){
            log.info("交易hash已经存在,交易信息[{}]",transaction);
            return;
        }


        TransactionReceipt transactionReceipt = web3JUtilWrapper.getTransactionReceiptByHash(txHash);
        BigInteger status = FormatConvert.hexToDec(transactionReceipt.getStatus());

        BigInteger valueWei = transaction.getValue();
        //转账金额
        BigDecimal value = FormatConvert.WeiTOEth(valueWei.toString());
        BigInteger blockNumber = transaction.getBlockNumber();
        BigInteger gasPriceWei = transaction.getGasPrice();
        BigInteger gasPrice = FormatConvert.WeiTOGWei(gasPriceWei);
        BigInteger gas = transaction.getGas();
        Date date = new Date();
        TTransaction tTransaction = TTransaction.builder().blockNumber(blockNumber)
                .fromAddress(fromAddress).toAddress(toAddress)
                .amount(value).gasPrice(gasPrice).gasUse(gas)
                .type(1).tradingHash(txHash).status(status.intValue())
                .gmtCreate(date).gmtModified(date)
                .build();
        tTransactionService.save(tTransaction);
        log.info("保存交易信息落库[{}]",transaction);
    }



    /**
     * 验证当前区块数是否需要处理 超过阈值 则更新当前块 为处理块 记录入库
     * 否则不进行处理
     * @param lastBlockNumber
     * @param currentBlockNumber
     */
    private boolean validateBlockNumber(BigInteger lastBlockNumber, BigInteger currentBlockNumber) {
        int number = currentBlockNumber.subtract(lastBlockNumber).intValue();
        if (number>ETHConstants.BLOCK_INTERVAL_NUMBER){
            return true;
        }
        log.info("当前区块数间隔[{}],不需要处理跳过",number);
        return false;
    }
}
