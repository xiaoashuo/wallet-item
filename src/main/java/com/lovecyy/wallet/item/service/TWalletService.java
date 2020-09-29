package com.lovecyy.wallet.item.service;

import com.lovecyy.wallet.item.model.dto.AccountDTO;
import com.lovecyy.wallet.item.model.dto.TWalletDto;
import com.lovecyy.wallet.item.model.dto.TokenDTO;
import com.lovecyy.wallet.item.model.pojo.TWallet;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lovecyy.wallet.item.model.qo.TokenQO;

import java.io.IOException;
import java.util.List;

public interface TWalletService extends IService<TWallet>{

    /**
     * 创建钱包
     * @param userId
     * @param password
     * @return
     */
    public TWalletDto createWallet(Integer userId, String password) throws Exception;

    /**
     * 获取钱包列表
     * @return
     * @throws Exception
     */
    public List<TWalletDto> getWallets(Integer userId) throws Exception;

    /**
     * 根据用户id和地址得到用户信息
     * @param userId
     * @param address
     * @return
     */
    TWallet getWallet(Integer userId,String address);

    /**
     * 删除通过用户id和主键id
     * @param userId
     * @param primaryId
     */
    boolean delWalletById(Integer userId, Integer primaryId);

    /**
     * 查询以太坊地址余额
     * @param address
     * @return
     */
    AccountDTO balance(String address);

    /**
     * 监控以太坊区块
     * 若有关于本钱包交易则录入库
     */
    void monitorEthBlock() throws IOException;

    /**
     * 转账ETH 本币
     *
     * @param userId
     * @param tokenQO
     * @return
     */
    TokenDTO transfer(Integer userId, TokenQO tokenQO) throws Exception;

    /**
     * 验证地址是否在钱包内
     * @param address
     * @return
     */
    boolean validateAddressInWallet(String...address);

    /**
     * 得到钱包 通过地址
     * @param address
     * @return
     */
    TWallet getWalletByAddress(String address);
}
