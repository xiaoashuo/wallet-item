package com.lovecyy.wallet.item.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class UserDTO {
    /**
     * 用户id
     */
    private Integer id;
    /**
     * 用户名
     */
    private String username;

}
