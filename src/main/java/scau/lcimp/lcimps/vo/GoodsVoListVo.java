package scau.lcimp.lcimps.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "GoodsVoListVo对象", description = "货品列表对象扩展")
public class GoodsVoListVo {
    @ApiModelProperty("货品列表原型")
    private List<GoodsVo> GoodsList;

    @ApiModelProperty("货品列表页数：pagesNumber=0时代表获取全部货品的列表，pagesNumber=1时代表货品条目数少于单页容量10条，已获取全部货品列表")
    private Long pagesNumber;

    public GoodsVoListVo(List<GoodsVo> GoodsList) {
        this.GoodsList = GoodsList;
    }
}
