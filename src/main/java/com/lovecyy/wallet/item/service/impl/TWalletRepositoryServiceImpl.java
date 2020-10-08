package com.lovecyy.wallet.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lovecyy.wallet.item.mapper.TWalletMapper;
import com.lovecyy.wallet.item.mapper.TWalletRepositoryMapper;
import com.lovecyy.wallet.item.model.pojo.TWallet;
import com.lovecyy.wallet.item.model.pojo.TWalletRepository;
import com.lovecyy.wallet.item.service.TWalletRepositoryService;

import org.springframework.stereotype.Service;

@Service
public class TWalletRepositoryServiceImpl extends ServiceImpl<TWalletRepositoryMapper, TWalletRepository> implements TWalletRepositoryService {

    @Override
    public TWalletRepository getAvaliableAccount() {
        QueryWrapper<TWalletRepository> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("used",1);
        queryWrapper.last("limit 1");
        TWalletRepository tWalletRepository = this.getBaseMapper().selectOne(queryWrapper);
        if (tWalletRepository!=null){
            UpdateWrapper<TWalletRepository> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("used",0);
            updateWrapper.eq("id",tWalletRepository.getId());
            this.getBaseMapper().update(null, updateWrapper);
        }
        return tWalletRepository;
    }
}
