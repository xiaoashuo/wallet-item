package com.lovecyy.wallet.item.model.dto;

import com.lovecyy.wallet.item.model.pojo.TTransaction;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author shuoyu
 */
@Data
public class TransactionDTO implements Serializable {
    /**下次使用页码*/
    private Integer pageNum;

    private Boolean isLastPage;
    /**交易列表*/
    private List<TTransaction> lists;


}
