package com.lovecyy.wallet.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lovecyy.wallet.item.model.pojo.TUserRelationContract;

public interface TUserRelationContractMapper extends BaseMapper<TUserRelationContract> {
    /**
     * 判断是否存在
     * @param tUserRelationContract
     * @return
     */
    Integer isExistsRelation(TUserRelationContract tUserRelationContract);
}