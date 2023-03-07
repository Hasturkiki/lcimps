package scau.lcimp.lcimps.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 货品
 *
 * @TableName goods
 */
@ApiModel(value = "Goods对象", description = "货品")
@TableName(value = "goods")
@Data
public class Goods implements Serializable {
    /**
     * 货品ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("货品ID")
    private Integer id;

    /**
     * 货品名称
     */
    @TableField(value = "goods_name")
    @ApiModelProperty("货品名称")
    private String goodsName;

    /**
     * 货品简介
     */
    @TableField(value = "description")
    @ApiModelProperty("货品简介")
    private String description;

    /**
     * 货品所属企业用户ID
     */
    @TableField(value = "owner_id")
    @ApiModelProperty("货品所属企业用户ID")
    private Integer ownerId;

    /**
     * 佣金
     */
    @TableField(value = "brokerage")
    @ApiModelProperty("佣金")
    private BigDecimal brokerage;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableField(value = "is_deleted")
    @ApiModelProperty("是否删除")
    private Boolean isDeleted;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
