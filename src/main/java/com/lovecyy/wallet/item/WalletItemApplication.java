package com.lovecyy.wallet.item;

import com.lovecyy.wallet.item.netty.annotation.EnableWebSocket;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EnableWebSocket
@MapperScan("com.lovecyy.wallet.item.mapper")
public class WalletItemApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalletItemApplication.class, args);
    }

}
