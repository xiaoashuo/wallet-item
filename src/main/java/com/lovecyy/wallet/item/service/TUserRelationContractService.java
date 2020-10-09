package com.lovecyy.wallet.item.service;

import com.lovecyy.wallet.item.model.pojo.TUserRelationContract;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface TUserRelationContractService extends IService<TUserRelationContract>{
    /**
     * 判断是否存在关系
     * @param tUserRelationContract
     * @return
     */
    boolean isExistsRelation(TUserRelationContract tUserRelationContract);

    /**
     * 根据用户id钱包地址获取关联合约
     * @param userId
     * @param walletAddress
     * @return
     */
    List<TUserRelationContract> listByUidAndWalletAddress(Integer userId, String walletAddress);

    void saveInfo(TUserRelationContract tUserRelationContract);
}
