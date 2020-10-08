package com.lovecyy.wallet.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lovecyy.wallet.item.model.dto.TransactionDTO;
import com.lovecyy.wallet.item.model.qo.TransactionQO;
import com.sun.xml.internal.ws.api.message.Attachment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lovecyy.wallet.item.model.pojo.TTransaction;
import com.lovecyy.wallet.item.mapper.TTransactionMapper;
import com.lovecyy.wallet.item.service.TTransactionService;
import org.springframework.util.Assert;

@Service
public class TTransactionServiceImpl extends ServiceImpl<TTransactionMapper, TTransaction> implements TTransactionService {

    @Override
    public boolean isExistsTxHash(String hash) {
        QueryWrapper<TTransaction> queryCondition=new QueryWrapper<>();
        queryCondition.eq(TTransaction.COL_TRADING_HASH,hash);
        return SqlHelper.retBool(count(queryCondition));
    }

    @Override
    public TransactionDTO listByTypeAndPage(TransactionQO transactionQO) {
        Integer type = transactionQO.getOptionType();
        Assert.notNull(type,"交易类型不能为空");
        PageInfo<TTransaction> transactions;

        switch (type){
            case 0:
                transactions = listAllByAddressInFromOrToAndType(transactionQO);
                break;
            case 1:
               transactions = listAllByFromAddressAndType(transactionQO);
                break;
            case 2:
                transactions=listAllByToAddressAndType(transactionQO);
                break;
            case 3:
                transactionQO.setStatus(0);
                transactions=listAllByAddressInFromOrToAndType(transactionQO);
                break;
            default:
                throw new RuntimeException("交易类型错误");
        }
        TransactionDTO transactionDTO=new TransactionDTO();
        boolean isLastPage = transactions.isIsLastPage();
        transactionDTO.setIsLastPage(transactions.isIsLastPage());
        transactionDTO.setPageNum(isLastPage?transactionQO.getPageNum():transactions.getNextPage());
        transactionDTO.setLists(transactions.getList());
        return transactionDTO;
    }
    private   PageInfo<TTransaction>  listAllByAddressInFromOrToAndType(TransactionQO transactionQO){
        PageHelper.startPage(transactionQO.getPageNum(), transactionQO.getPageSize());
        List<TTransaction> transactions = this.getBaseMapper().listAllByAddressInFromOrToAndType(transactionQO.getAddress(), transactionQO.getContractAddress(), transactionQO.getStatus());
        PageInfo<TTransaction> transactionPageInfo = new PageInfo<>(transactions);
        return transactionPageInfo;
    }


    /**
     * 查询转出附带类型
     * @param transactionQO
     * @return
     */
    PageInfo<TTransaction> listAllByFromAddressAndType(TransactionQO transactionQO){
        PageHelper.startPage(transactionQO.getPageNum(), transactionQO.getPageSize());
        List<TTransaction> transactions = this.getBaseMapper().listAllByFromAddressAndType(transactionQO.getAddress(), transactionQO.getContractAddress());
        PageInfo<TTransaction> transactionPageInfo = new PageInfo<>(transactions);

        return transactionPageInfo;
    }

    /**
     * 查询转入附带类型
     * @param transactionQO
     * @return
     */
    PageInfo<TTransaction> listAllByToAddressAndType(TransactionQO transactionQO){
        PageHelper.startPage(transactionQO.getPageNum(), transactionQO.getPageSize());

        List<TTransaction> transactions = this.getBaseMapper().listAllByToAddressAndType(transactionQO.getAddress(), transactionQO.getContractAddress());
        PageInfo<TTransaction> transactionPageInfo = new PageInfo<>(transactions);

        return transactionPageInfo;
    }

}

