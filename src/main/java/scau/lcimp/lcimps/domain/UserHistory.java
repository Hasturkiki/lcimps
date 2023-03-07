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
 * 用户历史记录
 * @TableName user_history
 */
@ApiModel(value = "UserHistory对象", description = "用户历史记录")
@TableName(value ="user_history")
@Data
public class UserHistory implements Serializable {
    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 用户类型(0代表管理员、1代表企业、2代表主播)
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 用户状态（0代表未删除、1代表已删除，默认为0）
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
