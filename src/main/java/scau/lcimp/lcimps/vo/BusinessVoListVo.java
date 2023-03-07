package scau.lcimp.lcimps.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "BusinessVoListVo对象", description = "订单列表扩展")
public class BusinessVoListVo {
    @ApiModelProperty("订单列表原型")
    private List<BusinessVo> BusinessVoList;

    @ApiModelProperty("订单列表页数：pagesNumber=0时代表获取全部订单的列表，pagesNumber=1时代表订单条目数少于单页容量10条，已获取全部订单列表")
    private Long pagesNumber;

    public BusinessVoListVo(List<BusinessVo> BusinessVoList) {
        this.BusinessVoList = BusinessVoList;
    }
}
