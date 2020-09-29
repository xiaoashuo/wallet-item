package com.lovecyy.wallet.item.common.utils;

import com.lovecyy.wallet.item.model.pojo.ContractInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class TokenUtilWrapper {

    private final Web3j web3j;

    /**
     * 得到token余额
     * @param fromAddress
     * @param contractAddress
     * @return
     */
    public BigInteger getTokenBalance(String fromAddress,String contractAddress){
        BigInteger tokenBalance = TokenUtil.getTokenBalance(web3j, fromAddress, contractAddress);
        BigInteger tokenDecimals = TokenUtil.getTokenDecimals(web3j, contractAddress);
        return FormatConvert.getNumberRemoveDecimal(tokenBalance, tokenDecimals);
    }

    /**
     * 得到合约信息
     * @param contractAddress
     * @return
     */
    public  ContractInfo getContractInfo(String contractAddress){
       return TokenUtil.getContractInfo(web3j, contractAddress);
    }

    /**
     * 得到合约精度
     * @param contractAddress
     * @return
     */
    public BigInteger getContractDecimal(String contractAddress){
        return TokenUtil.getTokenDecimals(web3j,contractAddress);
    }

    /**
     * 发送代币
     * @param fromAddress
     * @param toAddress
     * @param amount
     * @param privateKey
     * @param contractAddress
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws IOException
     */
    public  String signTokenTransaction(String fromAddress, String toAddress, BigDecimal amount
            , String privateKey, String contractAddress) throws InterruptedException, ExecutionException, IOException {
        return TokenUtil.signTokenTransaction(web3j, fromAddress, toAddress, amount, privateKey, contractAddress);
    }

    /**
     * 发送token 手动指定gas price单价
     * @param fromAddress
     * @param toAddress
     * @param amount
     * @param privateKey
     * @param contractAddress
     * @param _gasPrice
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws IOException
     */
    public  String signTokenTransaction(String fromAddress, String toAddress, BigDecimal amount
            , String privateKey, String contractAddress, BigDecimal _gasPrice) throws InterruptedException, ExecutionException, IOException {
        return TokenUtil.signTokenTransaction(web3j, fromAddress, toAddress, amount, privateKey, contractAddress,_gasPrice);
    }

    /**
     * 默认天然器
     * @param fromAddress
     * @param toAddress
     * @param amount
     * @param contractAddress
     * @param walletPassword
     * @param ketStore
     * @return
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public  String signTokenTransaction( String fromAddress, String toAddress, BigDecimal amount
            , String contractAddress , String walletPassword,String ketStore) throws IOException, ExecutionException, InterruptedException {
        Credentials credentials = Web3JUtil.openWalletByJSON(walletPassword, ketStore);
        return TokenUtil.signTokenTransaction(web3j, fromAddress, toAddress, amount, credentials, contractAddress);
    }
    /**
     * 发送代币
     * @param fromAddress
     * @param toAddress
     * @param amount
     * @param contractAddress
     * @param _gasPrice
     * @param walletPassword
     * @param ketStore
     * @return
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public  String signTokenTransaction( String fromAddress, String toAddress, BigDecimal amount
           , String contractAddress, BigDecimal _gasPrice , String walletPassword,String ketStore) throws IOException, ExecutionException, InterruptedException {
        Credentials credentials = Web3JUtil.openWalletByJSON(walletPassword, ketStore);
        return TokenUtil.signTokenTransaction(web3j, fromAddress, toAddress, amount, credentials, contractAddress, _gasPrice);
    }


}
