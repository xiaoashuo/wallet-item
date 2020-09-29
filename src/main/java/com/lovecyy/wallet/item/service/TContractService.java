package com.lovecyy.wallet.item.service;

import com.lovecyy.wallet.item.model.pojo.ContractInfo;
import com.lovecyy.wallet.item.model.pojo.TContract;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lovecyy.wallet.item.model.pojo.TWallet;
import com.lovecyy.wallet.item.model.qo.ContractQO;

import java.util.List;

public interface TContractService extends IService<TContract>{

    /**
     * 部署合约
     * @param contractQo
     * @param wallet
     * @return
     */
    String deploy(ContractQO contractQo, TWallet wallet) throws Exception;

    /**
     * 获取合约信息
     * @param userId
     * @param contractAddress
     * @return
     */
    ContractInfo info(Integer userId,String contractAddress);

    /**
     * 更新合约状态
     * @param status
     * @param txHash
     * @return true|false
     */
    boolean updateContractStatus(Integer status,String txHash);

    /**
     * 交易hash是否存在
     * @param txHash
     * @return
     */
    boolean isExistsContractHash(String txHash);

    /**
     * 根据用户id查询所有合约
     * @param userId
     * @return
     */
    List<TContract> listByUserId(Integer userId);
}
