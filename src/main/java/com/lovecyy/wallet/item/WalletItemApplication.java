package com.lovecyy.wallet.item;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@MapperScan("com.lovecyy.wallet.item.mapper")
public class WalletItemApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalletItemApplication.class, args);
    }

}
