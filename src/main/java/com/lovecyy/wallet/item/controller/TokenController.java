package com.lovecyy.wallet.item.controller;

import com.lovecyy.wallet.item.model.dto.R;
import com.lovecyy.wallet.item.model.dto.TokenDTO;
import com.lovecyy.wallet.item.model.qo.TokenQO;
import com.lovecyy.wallet.item.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * 通证操作
 * @author Yakir
 */
@RequiredArgsConstructor
@RequestMapping("token")
@RestController
@Validated
public class TokenController extends BaseController{

    private static final Logger log= LoggerFactory.getLogger(TokenController.class);


    private final TokenService tokenService;

    @GetMapping("balance")
    public R getTokenBalance(String address,String contractAddress){
        try {
            BigInteger tokenBalance = tokenService.getTokenBalance(address, contractAddress);
            return R.ok(tokenBalance);
        } catch (Exception e) {
            log.error("获取代币余额发生异常",e);
            return R.fail("获取代币余额失败");
        }
    }

    /**
     * 发送token交易
     * @param tokenQO
     * @return
     */
    @PostMapping("send")
    public R sendTokenTransaction(TokenQO tokenQO){
        try {
            Integer userId = getUser().getId();
            TokenDTO result=tokenService.sendTokenTransaction(userId,tokenQO);
            return R.ok(result);
        } catch (Exception e) {
            log.error("代币转账失败",e);
            return R.fail(e.getMessage());
        }
    }

    /**
     * 获取gas信息
     * @return
     */
    @GetMapping("gasInfo")
    public R gasInfo(){
        try {
            BigInteger gasPrice = tokenService.currentGasInfo();
            Map<String,Object> map=new HashMap<>();
            map.put("gasPrice",gasPrice);
            return R.ok(map);
        } catch (IOException e) {
            log.error("gas信息获取失败",e);
            return R.fail(e.getMessage());
        }
    }
}
