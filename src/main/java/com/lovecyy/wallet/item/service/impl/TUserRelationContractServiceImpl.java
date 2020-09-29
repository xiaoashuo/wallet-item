package com.lovecyy.wallet.item.service.impl;

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
}
