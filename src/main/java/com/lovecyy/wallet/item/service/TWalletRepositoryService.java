package com.lovecyy.wallet.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lovecyy.wallet.item.model.pojo.TWallet;
import com.lovecyy.wallet.item.model.pojo.TWalletRepository;

public interface TWalletRepositoryService  extends IService<TWalletRepository> {

    /**
     * 获得1个可用钱包账户
     * @return
     */
    TWalletRepository getAvaliableAccount();
}
