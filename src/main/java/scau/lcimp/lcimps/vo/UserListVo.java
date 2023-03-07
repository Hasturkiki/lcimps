package scau.lcimp.lcimps.vo;

import scau.lcimp.lcimps.domain.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "UserListVo对象", description = "货品列表扩展")
public class UserListVo {
    @ApiModelProperty("货品列表原型")
    private List<User> UserList;

    @ApiModelProperty("货品列表页数：pagesNumber=0时代表获取全部货品的列表，pagesNumber=1时代表货品条目数少于单页容量10条，已获取全部货品列表")
    private Long pagesNumber;

    public UserListVo(List<User> UserList) {
        this.UserList = UserList;
    }
}
