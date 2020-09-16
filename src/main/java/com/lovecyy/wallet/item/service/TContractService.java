package com.lovecyy.wallet.item.service;

import com.lovecyy.wallet.item.model.pojo.ContractInfo;
import com.lovecyy.wallet.item.model.pojo.TContract;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lovecyy.wallet.item.model.qo.ContractQO;

public interface TContractService extends IService<TContract>{

    /**
     * 部署合约
     * @param contractQo
     * @return
     */
    String deploy(ContractQO contractQo) throws Exception;

    /**
     * 获取合约信息
     * @param userId
     * @param contractAddress
     * @return
     */
    ContractInfo info(Integer userId,String contractAddress);
}
