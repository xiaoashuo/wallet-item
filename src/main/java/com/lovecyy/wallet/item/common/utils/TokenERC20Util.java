package com.lovecyy.wallet.item.common.utils;


import com.lovecyy.wallet.item.contracts.TokenERC20;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class TokenERC20Util {


    private static final Logger logger = LoggerFactory.getLogger(TokenERC20Util.class);


    private final TokenERC20 tokenERC20;

    /**
     * 得到符号标记
     * @return
     * @throws Exception
     */
    public String getSymbol() throws Exception {
        return tokenERC20.symbol().send();
    }

    /**
     * 得到token名字
     * @return
     * @throws Exception
     */
    public String getName() throws Exception {
        return tokenERC20.name().send();
    }

    /**
     * 获得小数点后位数 精度
     * @return
     * @throws Exception
     */
    public BigInteger getDecimal() throws Exception {
        return tokenERC20.decimals().send();
    }

    /**
     * 获得总发行量合约
     * @return
     * @throws Exception
     */
    public BigInteger getTotalSupply() throws Exception {
        return tokenERC20.totalSupply().send();
    }

    /**
     * 获取地址余额
     * @param address
     * @return
     * @throws Exception
     */
    public BigInteger balanceOf(String address) throws Exception {
        return tokenERC20.balanceOf(address).send();
    }

    /**
     * 转账
     * @param toAddress
     * @param value
     * @return
     */
    public String transfer(String toAddress,BigInteger value) throws Exception {
        TransactionReceipt receipt = tokenERC20.transfer(toAddress, value).send();
        return receipt.getTransactionHash();
    }

    /**
     * 根据指定交易凭据查询事件
     * @param receipt
     */
    public void transferEventByReceipt(TransactionReceipt receipt){
        List<TokenERC20.TransferEventResponse> events = tokenERC20.getTransferEvents(receipt);
        events.forEach(event
                -> System.out.println("from: " + event.from + ", to: " + event.to + ", value: " + event.value));
    }

    /**
     * 订阅所有事件
     */
    public void transferEventFlowable(){
        tokenERC20.transferEventFlowable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST)
                .subscribe(event
                        -> System.out.println("from: " + event.from + ", to: " + event.to + ", value: " + event.value));
    }

    /**
     * 监听ERC20 token 交易
     */
    public  void eventContract(String contractAddress){

        final EthFilter ethFilter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST,
                contractAddress);
        List<TypeReference<?>> parameters=new ArrayList<>();
        parameters.add(new TypeReference<Address>(){});
        parameters.add(new TypeReference<Address>(){});
        parameters.add(new TypeReference<Uint256>(){});
        Event event = new Event("Transfer",
                parameters
        );

        String topicData = EventEncoder.encode(event);
        ethFilter.addSingleTopic(topicData);
       // ethFilter.addOptionalTopics("0x" + TypeEncoder.encode(new Address("0x00a329c0648769a73afac7f9381e08fb43dbea72")));

        tokenERC20
                .transferEventFlowable(ethFilter)
                .subscribe(logRecord -> {
                    logger.info("当前参数[{}]",logRecord);

                });
    }



}
