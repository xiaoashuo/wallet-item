package com.lovecyy.wallet.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.lovecyy.wallet.item.common.constants.ETHConstants;
import com.lovecyy.wallet.item.common.convert.TWalletConvert;
import com.lovecyy.wallet.item.common.utils.Web3JUtilWrapper;
import com.lovecyy.wallet.item.model.dto.AccountDTO;
import com.lovecyy.wallet.item.model.dto.KeystoreDto;

import com.lovecyy.wallet.item.model.dto.TWalletDto;
import com.lovecyy.wallet.item.model.dto.TokenDTO;
import com.lovecyy.wallet.item.model.qo.TokenQO;
import com.lovecyy.wallet.item.service.TConfigService;
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
import com.lovecyy.wallet.item.service.TWalletService;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthBlock;

@RequiredArgsConstructor
@Service
public class TWalletServiceImpl extends ServiceImpl<TWalletMapper, TWallet> implements TWalletService{

    private static final Logger log= LoggerFactory.getLogger(TWalletServiceImpl.class);


    private  final Web3JUtilWrapper web3JUtilWrapper;

    private  final TConfigService tConfigService;

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
                log.info("转出地址为空...跳过");
                continue;
            }
            //得到交易金额 如果小于等于0 则为代币交易 反之就是ETH的交易
            int value = transaction.getValue().intValue();
            if (value<=0){
                //执行代币交易
                handleTokenTransaction(transaction);
            }else{
                //执行eth交易
                handleEthTransaction(transaction);
            }

        }

    }

    /**
     * 处理代币交易
     * @param transaction
     */
    private void handleTokenTransaction(EthBlock.TransactionObject transaction) {

    }

    /**
     * 处理本币交易
     * @param transaction
     */
    private void handleEthTransaction(EthBlock.TransactionObject transaction) {
        System.out.println(transaction);
        System.out.println("测试");
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
