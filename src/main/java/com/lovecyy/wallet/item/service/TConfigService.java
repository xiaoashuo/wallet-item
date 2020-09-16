package com.lovecyy.wallet.item.service;

import com.lovecyy.wallet.item.model.pojo.TConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigInteger;

public interface TConfigService extends IService<TConfig>{
    /**
     * 得到上次区块号
     * @return 区号号 不存在则返回值
     */
   BigInteger getLastBlockNumber();

    /**
     * 更新扫描到的区块数
     * @pauram currentBlockNumber
     * @return
     */
   boolean updateBlockNumber(BigInteger currentBlockNumber);
}
