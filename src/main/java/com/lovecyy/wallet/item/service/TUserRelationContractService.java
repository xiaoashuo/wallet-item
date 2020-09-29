package com.lovecyy.wallet.item.service;

import com.lovecyy.wallet.item.model.pojo.TUserRelationContract;
import com.baomidou.mybatisplus.extension.service.IService;
public interface TUserRelationContractService extends IService<TUserRelationContract>{
    /**
     * 判断是否存在关系
     * @param tUserRelationContract
     * @return
     */
    boolean isExistsRelation(TUserRelationContract tUserRelationContract);

}
