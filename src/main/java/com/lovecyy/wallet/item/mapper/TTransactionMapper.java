package com.lovecyy.wallet.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lovecyy.wallet.item.model.pojo.TTransaction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TTransactionMapper extends BaseMapper<TTransaction> {

    /**
     * 查询所有 地址在from 或to 并且附带类型
     * @param address
     * @param contractAddress
     * @param status
     * @return
     */
    List<TTransaction> listAllByAddressInFromOrToAndType(@Param("address") String address, @Param("contractAddress") String contractAddress,@Param("status") Integer status);

    /**
     * 查询转出附带类型
     * @param address
     * @param contractAddress
     * @return
     */
    List<TTransaction> listAllByFromAddressAndType(@Param("address") String address, @Param("contractAddress") String contractAddress);

    /**
     * 查询转入附带类型
     * @param address
     * @param contractAddress
     * @return
     */
    List<TTransaction> listAllByToAddressAndType(@Param("address") String address, @Param("contractAddress") String contractAddress);


}