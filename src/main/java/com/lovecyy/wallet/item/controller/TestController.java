package com.lovecyy.wallet.item.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lovecyy.wallet.item.model.dto.R;
import com.lovecyy.wallet.item.model.pojo.TWallet;
import com.lovecyy.wallet.item.model.qo.TokenQO;
import com.lovecyy.wallet.item.netty.handle.ChatHandler;
import com.lovecyy.wallet.item.netty.handle.pojo.Message;
import com.lovecyy.wallet.item.service.TWalletService;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("grant")
public class TestController {

    private final TWalletService tWalletService;

    @GetMapping
    public R grant(){
        List<TWallet> list = tWalletService.list(Wrappers.lambdaQuery(TWallet.class).ne(TWallet::getUid, 10145));
        for (TWallet tWallet : list) {
            String address = tWallet.getAddress();
            TokenQO tokenQO = new TokenQO();
            tokenQO.setAmount(BigDecimal.valueOf(100));
            tokenQO.setFromAddress("0x292c6ddb3675ceb0e4d2a0d82e3e7115cfe44efb");
            tokenQO.setToAddress(address);
            try {
                tWalletService.transfer(10145,tokenQO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return R.ok();
    }

    @GetMapping("/test")
    public R test(String msg){
        Message message = new Message(1, 4, msg);
        ChatHandler.channels.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(message)));
        return R.ok();

    }
}
