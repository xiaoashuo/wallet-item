package com.lovecyy.wallet.item.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 最新1000比交易gas信息评估
 * @author Yakir
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GasInfoDTO {
    /**
     * 安全价格
     */
    private String lowPrice;
    /**
     * 较高用时
     */
    private String lowTime;
    /**
     * 平均价格
     */
    private String avgPrice;
    /**
     * 平均时间
     */
    private String avgTime;
    /**
     * 较高价格
     */
    private String highPrice;
    /**
     * 较低用时
     */
    private String highTime;
}
