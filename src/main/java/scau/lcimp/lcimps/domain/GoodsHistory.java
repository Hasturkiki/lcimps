package scau.lcimp.lcimps.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 货品历史记录
 * @TableName goods_history
 */
@ApiModel(value = "GoodsHistory对象", description = "货品历史记录")
@TableName(value ="goods_history")
@Data
public class GoodsHistory implements Serializable {
    /**
     * 货品ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 货品名称
     */
    @TableField(value = "goods_name")
    private String goodsName;

    /**
     * 货品所属企业用户ID
     */
    @TableField(value = "owner_id")
    private Integer ownerId;

    /**
     * 货品状态（0代表未删除、1代表已删除，默认为0）
     */
    @TableField(value = "state")
    private Boolean state;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableField(value = "is_deleted")
    private Boolean isDeleted;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
