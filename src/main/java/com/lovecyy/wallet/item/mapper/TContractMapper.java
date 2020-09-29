package com.lovecyy.wallet.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lovecyy.wallet.item.model.pojo.TContract;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface TContractMapper extends BaseMapper<TContract> {
    /**
     * 更新合约状态
     * @param status
     * @param gmtModified
     * @param txHash
     * @return
     */
     Integer updateContractStatus(@Param("status") Integer status,@Param("gmtModified")  Date gmtModified,
                                  @Param("txHash") String txHash);

    /**
     * 是否存在交易hash
     * @param txHash
     * @return
     */
     Integer isExistsContractHash(String txHash);
}