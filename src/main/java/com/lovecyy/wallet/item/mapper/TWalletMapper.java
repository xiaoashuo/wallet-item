package com.lovecyy.wallet.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lovecyy.wallet.item.model.pojo.TWallet;

public interface TWalletMapper extends BaseMapper<TWallet> {
    /**
     * 验证地址是否在钱包内
     * @param addresses
     * @return
     */
    Integer validateAddressInWallet(String... addresses);
}