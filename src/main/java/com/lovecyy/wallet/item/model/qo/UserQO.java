package com.lovecyy.wallet.item.model.qo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class UserQO {

    /**
     * 用户名
     */
    @ApiModelProperty(value="用户名")
    @NotEmpty(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value="密码")
    @NotEmpty(message = "密码不能为空")
    private String password;

    /**
     * 状态：0正常 1禁用
     */
    @ApiModelProperty(value="状态：0正常 1禁用")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private Date gmtModified;
}
