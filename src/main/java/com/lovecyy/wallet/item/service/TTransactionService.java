package com.lovecyy.wallet.item.service;

import com.lovecyy.wallet.item.model.pojo.TTransaction;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TTransactionService extends IService<TTransaction> {
    /**
     * 是否存在hash
     * @param hash
     * @return true|false
     */
    boolean isExistsTxHash(String hash);
}

