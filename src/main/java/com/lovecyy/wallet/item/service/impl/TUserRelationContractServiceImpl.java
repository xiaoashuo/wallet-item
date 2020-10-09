package com.lovecyy.wallet.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lovecyy.wallet.item.common.utils.TokenUtilWrapper;
import com.lovecyy.wallet.item.mapper.TUserRelationContractMapper;
import com.lovecyy.wallet.item.model.pojo.ContractInfo;
import com.lovecyy.wallet.item.model.pojo.TUserRelationContract;
import com.lovecyy.wallet.item.service.TUserRelationContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class TUserRelationContractServiceImpl extends ServiceImpl<TUserRelationContractMapper, TUserRelationContract> implements TUserRelationContractService{

    private final TokenUtilWrapper tokenUtilWrapper;

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

    @Override
    public void saveInfo(TUserRelationContract tUserRelationContract) {
        String contractAddress = tUserRelationContract.getContractAddress();
        ContractInfo contractInfo = tokenUtilWrapper.getContractInfo(contractAddress);
        tUserRelationContract.setContractName(contractInfo.getName());
        tUserRelationContract.setContractSymbol(contractInfo.getSymbol());
        tUserRelationContract.setContractDecimals(contractInfo.getDecimals());
        tUserRelationContract.setContractTotalSupply(contractInfo.getTotalSupply());
        this.getBaseMapper().insert(tUserRelationContract);
    }
}
