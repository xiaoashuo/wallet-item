package com.lovecyy.wallet.item.service;

import com.lovecyy.wallet.item.model.dto.TokenDTO;
import com.lovecyy.wallet.item.model.qo.TokenQO;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public interface TokenService {

    /**
     * 得到代币余额
     * @param address
     * @param contractAddress
     * @return
     */
    public BigInteger getTokenBalance(String address,String contractAddress);

    /**
     * 用户id
     * @param userId
     * @param tokenQO
     * @return
     */
    TokenDTO sendTokenTransaction(Integer userId, TokenQO tokenQO) throws InterruptedException, ExecutionException, IOException;

    /**
     * 当前gas信息
     * @throws IOException
     */
    BigInteger currentGasInfo() throws IOException;


}