package com.lovecyy.wallet.item.service.impl;

import com.lovecyy.wallet.item.common.exceptions.TokenException;
import com.lovecyy.wallet.item.common.utils.FormatConvert;
import com.lovecyy.wallet.item.common.utils.TokenUtilWrapper;
import com.lovecyy.wallet.item.common.utils.Web3JUtilWrapper;
import com.lovecyy.wallet.item.model.dto.TokenDTO;
import com.lovecyy.wallet.item.model.pojo.TWallet;
import com.lovecyy.wallet.item.model.qo.TokenQO;
import com.lovecyy.wallet.item.service.TUsersService;
import com.lovecyy.wallet.item.service.TWalletService;
import com.lovecyy.wallet.item.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

/**
 * 代币操作
 * @author Yakir
 */
@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private static final Logger log= LoggerFactory.getLogger(TokenServiceImpl.class);


    private final TokenUtilWrapper tokenUtilWrapper;

    private final Web3JUtilWrapper web3JUtilWrapper;

    private final TUsersService tUsersService;
    private final TWalletService tWalletService;

    @Override
    public BigInteger getTokenBalance(String address, String contractAddress) {
        return tokenUtilWrapper.getTokenBalance(address, contractAddress);
    }

    @Override
    public TokenDTO sendTokenTransaction(Integer userId, TokenQO tokenQO) throws InterruptedException, ExecutionException, IOException {
        String fromAddress = tokenQO.getFromAddress();
        TWallet wallet = tWalletService.getWallet(userId, fromAddress);
        if (wallet==null){
            throw new TokenException("钱包不存在");
        }
        String txHash;
        if (tokenQO.getGasPrice()==null){
            txHash = tokenUtilWrapper.signTokenTransaction(fromAddress, tokenQO.getToAddress(), tokenQO.getAmount(), tokenQO.getContractAddress(),  wallet.getPassword(), wallet.getKeyStore());
        }else{
            txHash = tokenUtilWrapper.signTokenTransaction(fromAddress, tokenQO.getToAddress(), tokenQO.getAmount(), tokenQO.getContractAddress(), tokenQO.getGasPrice(), wallet.getPassword(), wallet.getKeyStore());
        }
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setFromAddress(fromAddress);
        tokenDTO.setToAddress(tokenQO.getToAddress());
        tokenDTO.setTxHash(txHash);
        tokenDTO.setAmount(tokenQO.getAmount());
        return tokenDTO;
    }

    @Override
    public BigInteger currentGasInfo() throws IOException {
        BigInteger gasPrice = web3JUtilWrapper.getGasPrice();
        return FormatConvert.WeiTOGWei(gasPrice);
    }


}
