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
 * 业务
 * @TableName business
 */
@ApiModel(value = "Business对象", description = "业务")
@TableName(value ="business")
@Data
public class Business implements Serializable {
    /**
     * 业务ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("业务ID")
    private Integer id;

    /**
     * 用户ID
     */
    @TableField(value = "up_id")
    @ApiModelProperty("主播ID")
    private Integer upId;

    /**
     * 企业ID
     */
    @TableField(value = "company_id")
    @ApiModelProperty("企业ID")
    private Integer companyId;

    /**
     * 货品ID
     */
    @TableField(value = "goods_id")
    @ApiModelProperty("货品ID")
    private Integer goodsId;

    /**
     * 佣金
     */
    @TableField(value = "brokerage")
    @ApiModelProperty("佣金")
    private BigDecimal brokerage;

    /**
     * 业务状态(0未结算,1已结算,2已取消)
     */
    @TableField(value = "state")
    @ApiModelProperty("业务状态(0未结算,1已结算,2已取消)")
    private Integer state;

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
