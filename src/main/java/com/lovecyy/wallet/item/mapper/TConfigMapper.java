package com.lovecyy.wallet.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lovecyy.wallet.item.model.pojo.TConfig;

import java.math.BigInteger;

public interface TConfigMapper extends BaseMapper<TConfig> {
    /**
     * 得到配置值
     * @param cName
     * @return
     */
    BigInteger getConfigValue(String cName);
}