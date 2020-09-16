package com.lovecyy.wallet.item.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;

/**
 * web3j 配置
 * @author Yakir
 */
@Configuration
public class Web3JConfig {

    private static final Logger log= LoggerFactory.getLogger(Web3JConfig.class);

    @Value("${web3j.url:http://localhost:8545}")
    private String web3jUrl;



    @Bean
    public Web3j web3j(){
        Web3j web3j = Web3j.build(new HttpService(web3jUrl));
        try {
            //输出客户端版本
            Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            log.info("当前Web3J客户端版本 [{}]",clientVersion);
         } catch (IOException e) {
            log.error("Web3J初始化获取版本发生错误",e);
        }
        return web3j;
    }



//    @Bean
//    public TokenERC20 tokenERC20(Web3JUtil web3JUtil){
//       TokenERC20 tokenERC20=web3JUtil.load()
//    }
}
