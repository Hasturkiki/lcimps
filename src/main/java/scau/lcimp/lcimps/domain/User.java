package scau.lcimp.lcimps.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户
 *
 * @TableName user
 */
@ApiModel(value = "User对象", description = "用户")
@TableName(value = "user")
@Data
public class User implements Serializable {
    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("用户id")
    private Integer id;

    /**
     * 用户名
     */
    @TableField(value = "username")
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "password")
    @ApiModelProperty("密码")
    private String password;

    /**
     * 用户类型(0代表管理员、1代表企业、2代表主播)
     */
    @TableField(value = "type")
    @ApiModelProperty("用户类型(0代表管理员、1代表企业、2代表主播)")
    private Integer type;

    /**
     * 联系方式
     */
    @TableField(value = "telephone")
    @ApiModelProperty("联系方式")
    private String telephone;

    /**
     * 电子邮箱
     */
    @TableField(value = "email")
    @ApiModelProperty("电子邮箱")
    private String email;

    /**
     * 个性签名
     */
    @TableField(value = "signature")
    @ApiModelProperty("个性签名")
    private String signature;

    /**
     * 是否启用(false禁用,true启用)
     */
    @TableField(value = "is_enabled")
    @ApiModelProperty("是否启用(false禁用,true启用)")
    private Boolean isEnabled;

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
