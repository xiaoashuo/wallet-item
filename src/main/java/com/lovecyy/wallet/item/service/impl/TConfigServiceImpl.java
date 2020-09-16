package com.lovecyy.wallet.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lovecyy.wallet.item.common.constants.ETHConstants;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lovecyy.wallet.item.mapper.TConfigMapper;
import com.lovecyy.wallet.item.model.pojo.TConfig;
import com.lovecyy.wallet.item.service.TConfigService;
@Service
public class TConfigServiceImpl extends ServiceImpl<TConfigMapper, TConfig> implements TConfigService{
    @Override
    public BigInteger getLastBlockNumber() {
        return this.getBaseMapper().getConfigValue(ETHConstants.BLOCK_NUMBER+"1");
    }

    @Override
    public boolean updateBlockNumber(BigInteger currentBlockNumber) {
        UpdateWrapper<TConfig> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set(TConfig.COL_C_VALUE,currentBlockNumber);
        updateWrapper.set(TConfig.COL_GMT_MODIFIED,new Date());
        updateWrapper.eq(TConfig.COL_C_NAME,ETHConstants.BLOCK_NUMBER);
        return this.update(updateWrapper);
    }
}
