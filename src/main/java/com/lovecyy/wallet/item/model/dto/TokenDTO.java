package com.lovecyy.wallet.item.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 代币数据传输
 * @author Yakir
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenDTO {
    private String fromAddress;
    private String toAddress;
    private String txHash;
    private BigDecimal amount;

}
