package com.lovecyy.wallet.item.service;

import com.lovecyy.wallet.item.model.dto.TransactionDTO;
import com.lovecyy.wallet.item.model.pojo.TTransaction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lovecyy.wallet.item.model.qo.TransactionQO;

import java.util.List;

public interface TTransactionService extends IService<TTransaction> {
    /**
     * 是否存在hash
     * @param hash
     * @return true|false
     */
    boolean isExistsTxHash(String hash);

    /**
     *  获取合约交易列表
      * @param transactionQO
     * @return
     */
    TransactionDTO listByTypeAndPage(TransactionQO transactionQO);
}

