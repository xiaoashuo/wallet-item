package com.lovecyy.wallet.item.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

/**
    * 配置信息表
    */
@ApiModel(value="com-lovecyy-wallet-item-model-pojo-TConfig")
@Data
@Builder
@TableName(value = "t_config")
public class TConfig implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="id")
    private Integer id;

    /**
     * 配置名称
     */
    @TableField(value = "c_name")
    @ApiModelProperty(value="配置名称")
    private String cName;

    /**
     * 配置值
     */
    @TableField(value = "c_value")
    @ApiModelProperty(value="配置值")
    private String cValue;

    /**
     * 配置描述
     */
    @TableField(value = "c_desc")
    @ApiModelProperty(value="配置描述")
    private String cDesc;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_create")
    @ApiModelProperty(value="创建时间")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @TableField(value = "gmt_modified")
    @ApiModelProperty(value="更新时间")
    private Date gmtModified;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_C_NAME = "c_name";

    public static final String COL_C_VALUE = "c_value";

    public static final String COL_C_DESC = "c_desc";

    public static final String COL_GMT_CREATE = "gmt_create";

    public static final String COL_GMT_MODIFIED = "gmt_modified";
}