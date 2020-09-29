package com.lovecyy.wallet.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lovecyy.wallet.item.model.pojo.TTransaction;
import com.lovecyy.wallet.item.mapper.TTransactionMapper;
import com.lovecyy.wallet.item.service.TTransactionService;

@Service
public class TTransactionServiceImpl extends ServiceImpl<TTransactionMapper, TTransaction> implements TTransactionService {

    @Override
    public boolean isExistsTxHash(String hash) {
        QueryWrapper<TTransaction> queryCondition=new QueryWrapper<>();
        queryCondition.eq(TTransaction.COL_TRADING_HASH,hash);
        return SqlHelper.retBool(count(queryCondition));
    }
}

