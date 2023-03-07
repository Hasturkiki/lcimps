package scau.lcimp.lcimps.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 货品详情图片
 * @TableName goods_detail_image
 */
@ApiModel(value = "GoodsDetailImage对象", description = "货品详情图片")
@TableName(value ="goods_detail_image")
@Data
public class GoodsDetailImage implements Serializable {
    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("货品ID")
    @TableField("goods_id")
    private Integer goodsId;

    @ApiModelProperty("图片名")
    @TableField("image_name")
    private String imageName;

    @ApiModelProperty("图片地址")
    @TableField("image_position")
    private String imagePosition;

    @ApiModelProperty("图片原始名")
    @TableField("original_name")
    private String originalName;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("是否删除")
    @TableField("is_deleted")
    @TableLogic
    private Boolean isDeleted;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
