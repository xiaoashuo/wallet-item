package com.lovecyy.wallet.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.lovecyy.wallet.item.common.convert.TContractConvert;
import com.lovecyy.wallet.item.common.utils.TokenUtilWrapper;
import com.lovecyy.wallet.item.common.utils.Web3JUtilWrapper;
import com.lovecyy.wallet.item.model.pojo.ContractInfo;
import com.lovecyy.wallet.item.model.pojo.TWallet;
import com.lovecyy.wallet.item.model.qo.ContractQO;
import com.lovecyy.wallet.item.service.TWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lovecyy.wallet.item.mapper.TContractMapper;
import com.lovecyy.wallet.item.model.pojo.TContract;
import com.lovecyy.wallet.item.service.TContractService;
import org.springframework.util.Assert;
import org.web3j.crypto.Credentials;
import org.web3j.utils.Convert;

@Service
@RequiredArgsConstructor
public class TContractServiceImpl extends ServiceImpl<TContractMapper, TContract> implements TContractService{

    private final Web3JUtilWrapper web3JUtil;
    private final TokenUtilWrapper tokenUtilWrapper;



    @Override
    public String deploy(ContractQO contractQo, TWallet wallet) throws Exception {
        Credentials credentials = web3JUtil.openWalletByJSON(wallet.getPassword(), wallet.getKeyStore());
        BigDecimal balance = web3JUtil.getBalanceByAddress(wallet.getAddress());
        if (balance.compareTo(BigDecimal.ZERO)<=0){
            throw new RuntimeException("用户余额不足");
        }

        String transactionHash = web3JUtil.deployByWait3(credentials, contractQo.getName(), contractQo.getSymbol(), contractQo.getDecimals(), contractQo.getTotalSupply(), Convert.toWei("22", Convert.Unit.GWEI).toBigInteger(), BigInteger.valueOf(4000000));
        TContract tContract=new TContract();
        tContract.setUid(contractQo.getUserId());
        tContract.setTxHash(transactionHash);
        tContract.setName(contractQo.getName());
        tContract.setSymbol(contractQo.getSymbol());
        tContract.setDecimals(contractQo.getDecimals());
        tContract.setTotalSupply(contractQo.getTotalSupply());
        tContract.setIssueAddress(contractQo.getAddress());
        this.getBaseMapper().insert(tContract);
        return transactionHash;
    }

    @Override
    public ContractInfo info(Integer userId,String contractAddress) {
        QueryWrapper<TContract> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(TContract.COL_UID,userId);
        queryWrapper.eq(TContract.COL_ADDRESS,contractAddress);
        TContract tContract = this.getBaseMapper().selectOne(queryWrapper);
        if (tContract!=null){
            return TContractConvert.INSTANCE.ContractTOContractInfo(tContract);
        }
        return tokenUtilWrapper.getContractInfo(contractAddress);
    }

    @Override
    public boolean updateContractStatus(Integer status,String txHash) {
        Date date = new Date();
        return SqlHelper.retBool(getBaseMapper().updateContractStatus(status,date,txHash));
    }

    @Override
    public boolean isExistsContractHash(String txHash) {
        return  SqlHelper.retBool(getBaseMapper().isExistsContractHash(txHash));
    }

    @Override
    public List<TContract> listByUserId(Integer userId) {
        QueryWrapper<TContract> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(TContract.COL_UID,userId);
        return getBaseMapper().selectList(queryWrapper);
    }
}
