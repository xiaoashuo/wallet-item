package com.lovecyy.wallet.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lovecyy.wallet.item.model.pojo.TUserRelationContract;
import com.lovecyy.wallet.item.mapper.TUserRelationContractMapper;
import com.lovecyy.wallet.item.service.TUserRelationContractService;
@Service
public class TUserRelationContractServiceImpl extends ServiceImpl<TUserRelationContractMapper, TUserRelationContract> implements TUserRelationContractService{

    @Override
    public boolean isExistsRelation(TUserRelationContract tUserRelationContract) {
        Integer existsRelation = getBaseMapper().isExistsRelation(tUserRelationContract);
        return existsRelation!=null?true:false;
    }

    @Override
    public List<TUserRelationContract> listByUidAndWalletAddress(Integer userId, String walletAddress) {
        QueryWrapper<TUserRelationContract> tUserRelationContractQueryWrapper = new QueryWrapper<>();
        tUserRelationContractQueryWrapper.eq(TUserRelationContract.COL_UID,userId);
        tUserRelationContractQueryWrapper.eq(TUserRelationContract.COL_WALLET_ADDRESS,walletAddress);
        return this.getBaseMapper().selectList(tUserRelationContractQueryWrapper);

    }
}
