package com.lovecyy.wallet.item.mapper;

import com.lovecyy.wallet.item.model.pojo.TWalletRepository;

public interface TWalletRepositoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TWalletRepository record);

    int insertSelective(TWalletRepository record);

    TWalletRepository selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TWalletRepository record);

    int updateByPrimaryKey(TWalletRepository record);
}