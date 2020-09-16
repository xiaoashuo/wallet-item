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

@ApiModel(value="com-lovecyy-wallet-item-model-pojo-TFeedback")
@Data
@Builder
@TableName(value = "t_feedback")
public class TFeedback implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="id")
    private Integer id;

    /**
     * 用户id
     */
    @TableField(value = "uid")
    @ApiModelProperty(value="用户id")
    private Integer uid;

    @TableField(value = "username")
    @ApiModelProperty(value="")
    private String username;

    /**
     * 反馈内容
     */
    @TableField(value = "content")
    @ApiModelProperty(value="反馈内容")
    private String content;

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

    public static final String COL_UID = "uid";

    public static final String COL_USERNAME = "username";

    public static final String COL_CONTENT = "content";

    public static final String COL_GMT_CREATE = "gmt_create";

    public static final String COL_GMT_MODIFIED = "gmt_modified";
}