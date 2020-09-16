package com.lovecyy.wallet.item.common.utils;

import com.lovecyy.wallet.item.model.pojo.ContractInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;



public class TokenUtil {

    private static final Logger log= LoggerFactory.getLogger(TokenUtil.class);


    private static String emptyAddress = "0x0000000000000000000000000000000000000000";

    /**
     * 查询代币名称
     * @param contractAddress
     * @return
     */
    public static String getTokenName(Web3j web3j,String contractAddress) {
        String methodName = "name";
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            return results.get(0).getValue().toString();
        } catch (InterruptedException | ExecutionException e) {
            log.error("查询代币名称发生异常",e);
        }
        return null;
    }

    /**
     * 查询代币符号
     *
     * @param web3j
     * @param contractAddress
     * @return
     */
    public static String getTokenSymbol(Web3j web3j, String contractAddress) {
        String methodName = "symbol";
        String symbol = null;
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            symbol = results.get(0).getValue().toString();
        } catch (InterruptedException | ExecutionException e) {
           log.error("查询代币符号发生异常",e);
        }
        return symbol;
    }

    /**
     * 查询代币精度
     *
     * @param web3j
     * @param contractAddress
     * @return
     */
    public static BigInteger getTokenDecimals(Web3j web3j, String contractAddress) {
        String methodName = "decimals";
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Uint8> typeReference = new TypeReference<Uint8>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            return new BigInteger(results.get(0).getValue().toString());
        } catch (InterruptedException | ExecutionException e) {
           log.error("查询代币精度发生异常",e);
        }
        return BigInteger.ZERO;
    }

    /**
     * 查询代币发行总量
     *
     * @param web3j
     * @param contractAddress
     * @return
     */
    public static BigInteger getTokenTotalSupply(Web3j web3j, String contractAddress) {
        String methodName = "totalSupply";
        String fromAddr = emptyAddress;
        BigInteger totalSupply = BigInteger.ZERO;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            totalSupply = (BigInteger) results.get(0).getValue();
        } catch (InterruptedException | ExecutionException e) {
            log.error("查询代币总发行量发生异常",e);
         }
        return totalSupply;
    }
    /**
     * 查询代币余额
     */
    public static BigInteger getTokenBalance(Web3j web3j, String fromAddress, String contractAddress) {

        String methodName = "balanceOf";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address address = new Address(fromAddress);
        inputParameters.add(address);

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddress, contractAddress, data);

        EthCall ethCall;
        BigInteger balanceValue = BigInteger.ZERO;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            balanceValue = (BigInteger) results.get(0).getValue();
        } catch (IOException e) {
           log.error("查询代币余额发生异常",e);
        }
        return balanceValue;
    }

    /**
     * 得到余额不带精度的
     * @param web3j
     * @param fromAddress
     * @param contractAddress
     * @return
     */
    public static BigInteger getTokenBalanceNoDecimals(Web3j web3j, String fromAddress, String contractAddress) {
        BigInteger tokenBalance = getTokenBalance(web3j, fromAddress, contractAddress);
        BigInteger tokenDecimals = TokenUtil.getTokenDecimals(web3j, contractAddress);
        BigInteger realBalance = FormatConvert.getNumberRemoveDecimal(tokenBalance, tokenDecimals);
        return realBalance;
    }
    /**
     * 得到合约信息
     * @param web3j
     * @param contractAddress
     * @return
     */
    public static ContractInfo getContractInfo(Web3j web3j, String contractAddress){
        String tokenName = getTokenName(web3j, contractAddress);
        String tokenSymbol = getTokenSymbol(web3j, contractAddress);
        BigInteger tokenDecimals = getTokenDecimals(web3j, contractAddress);
        BigInteger tokenTotalSupply = getTokenTotalSupply(web3j, contractAddress);
        return new ContractInfo(contractAddress,tokenName,tokenSymbol,tokenDecimals,tokenTotalSupply);
    }

    /**
     * ETH代币转账
     * @param fromAddress
     * @param toAddress
     * @param amount
     * @param privateKey 发送方私钥
     * @param contractAddress coinAddress代币合约地址
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static String signTokenTransaction(Web3j web3j,String fromAddress,String toAddress,BigDecimal amount
    ,String privateKey,String contractAddress) throws IOException, ExecutionException, InterruptedException {
        //查询地址交易编号
        //   BigInteger nonce = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.PENDING).send().getTransactionCount();
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).send();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        //支付的矿工费
        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
        //凭证
        Credentials credentials = Credentials.create(privateKey);
        BigInteger tokenDecimals = getTokenDecimals(web3j, contractAddress);
        BigInteger amountWei = FormatConvert.setNumberAddDecimal(amount, tokenDecimals);
        checkTokenBalance(web3j,fromAddress,contractAddress,amountWei);
        //封装转账交易
        Function function = new Function(
                "transfer",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(toAddress),
                        new org.web3j.abi.datatypes.generated.Uint256(amountWei)),
                Collections.emptyList());
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createFunctionCallTransaction(fromAddress, nonce, gasPrice, null, contractAddress, data);
        EthEstimateGas send = web3j.ethEstimateGas(transaction).send();
        BigInteger gasLimit = send.getAmountUsed();
        log.info("评估交易GasLimit",gasLimit);
        //签名交易
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, contractAddress, data);
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        //广播交易
        String hash = web3j.ethSendRawTransaction(Numeric.toHexString(signMessage)).sendAsync().get().getTransactionHash();
        return hash;
    }
    public static String signTokenTransaction(Web3j web3j,String fromAddress,String toAddress,BigDecimal amount
            ,Credentials credentials,String contractAddress) throws IOException, ExecutionException, InterruptedException {
        //查询地址交易编号
        //   BigInteger nonce = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.PENDING).send().getTransactionCount();
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).send();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        //支付的矿工费
        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
        //凭证
        BigInteger tokenDecimals = getTokenDecimals(web3j, contractAddress);
        BigInteger amountWei = FormatConvert.setNumberAddDecimal(amount, tokenDecimals);
        checkTokenBalance(web3j, fromAddress, contractAddress, amountWei);
        //封装转账交易
        Function function = new Function(
                "transfer",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(toAddress),
                        new org.web3j.abi.datatypes.generated.Uint256(amountWei)),
                Collections.emptyList());
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createFunctionCallTransaction(fromAddress, nonce, gasPrice, null, contractAddress, data);
        EthEstimateGas send = web3j.ethEstimateGas(transaction).send();
        Assert.notNull(send.getResult(),send.getError()!=null?send.getError().getMessage():null);
        BigInteger gasLimit = send.getAmountUsed();
        log.info("评估交易GasLimit",gasLimit);
        //签名交易
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, contractAddress, data);
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        //广播交易
        String hash = web3j.ethSendRawTransaction(Numeric.toHexString(signMessage)).sendAsync().get().getTransactionHash();
        return hash;
    }

    /**
     * 检查代币余额是否足够
     * @param web3j
     * @param fromAddress
     * @param contractAddress
     * @param amountWei
     */
    private static void checkTokenBalance(Web3j web3j, String fromAddress, String contractAddress, BigInteger amountWei) {
        BigInteger tokenBalance = getTokenBalance(web3j, fromAddress, contractAddress);
        Assert.isTrue(amountWei.compareTo(tokenBalance)<=0,"用户余额不足");
    }

    /**
     * 代币转账 手动指定 gasPrice
     * @param web3j
     * @param fromAddress
     * @param toAddress
     * @param amount
     * @param privateKey
     * @param contractAddress
     * @param _gasPrice
     * @return
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static String signTokenTransaction(Web3j web3j,String fromAddress,String toAddress,BigDecimal amount
            ,String privateKey,String contractAddress,BigDecimal _gasPrice) throws IOException, ExecutionException, InterruptedException {
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).send();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        //凭证
        Credentials credentials = Credentials.create(privateKey);
        BigInteger gasPrice = FormatConvert.GWeiTOWei(_gasPrice);
        BigInteger tokenDecimals = getTokenDecimals(web3j, contractAddress);
        BigInteger amountWei = FormatConvert.setNumberAddDecimal(amount, tokenDecimals);
        checkTokenBalance(web3j,fromAddress,contractAddress,amountWei);
        //封装转账交易
        Function function = new Function(
                "transfer",
                Arrays.asList(new org.web3j.abi.datatypes.Address(toAddress),
                        new org.web3j.abi.datatypes.generated.Uint256(amountWei)),
                Collections.emptyList());
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createFunctionCallTransaction(fromAddress, nonce, gasPrice, null, contractAddress, data);
        EthEstimateGas send = web3j.ethEstimateGas(transaction).send();
        BigInteger gasLimit = send.getAmountUsed();
        log.info("评估交易GasLimit",gasLimit);
        //签名交易
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, contractAddress, data);
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        //广播交易
        String hash = web3j.ethSendRawTransaction(Numeric.toHexString(signMessage)).sendAsync().get().getTransactionHash();
        return hash;
    }

    /**
     * 代币转账 手动指定 凭据 gasPrice
     * @param web3j
     * @param fromAddress
     * @param toAddress
     * @param amount
     * @param credentials
     * @param contractAddress
     * @param _gasPrice
     * @return
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static String signTokenTransaction(Web3j web3j,String fromAddress,String toAddress,BigDecimal amount
            ,Credentials credentials,String contractAddress,BigDecimal _gasPrice) throws IOException, ExecutionException, InterruptedException {
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).send();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        //凭证
        BigInteger gasPrice = FormatConvert.GWeiTOWei(_gasPrice);
        BigInteger tokenDecimals = getTokenDecimals(web3j, contractAddress);
        BigInteger amountWei = FormatConvert.setNumberAddDecimal(amount, tokenDecimals);
        checkTokenBalance(web3j,fromAddress,contractAddress,amountWei);
        //封装转账交易
        Function function = new Function(
                "transfer",
                Arrays.asList(new org.web3j.abi.datatypes.Address(toAddress),
                        new org.web3j.abi.datatypes.generated.Uint256(amountWei)),
                Collections.emptyList());
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createFunctionCallTransaction(fromAddress, nonce, gasPrice, null, contractAddress, data);
        EthEstimateGas send = web3j.ethEstimateGas(transaction).send();
        BigInteger gasLimit = send.getAmountUsed();
        log.info("评估交易GasLimit",gasLimit);
        //签名交易
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, contractAddress, data);
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        //广播交易
        String hash = web3j.ethSendRawTransaction(Numeric.toHexString(signMessage)).sendAsync().get().getTransactionHash();
        return hash;
    }

}
