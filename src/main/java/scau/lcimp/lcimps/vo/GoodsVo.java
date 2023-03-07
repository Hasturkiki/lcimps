package scau.lcimp.lcimps.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import scau.lcimp.lcimps.domain.Goods;

import java.util.List;

@Data
@ApiModel(value = "GoodsVo对象", description = "货品对象扩展")
public class GoodsVo extends Goods {
    @ApiModelProperty("货品描述图片原始名")
    private List<String> mainImageOriginalNames;

    @ApiModelProperty("货品描述图片地址")
    private List<String> mainImagePositions;

    @ApiModelProperty("货品描述图片ID")
    private List<Integer> mainImageIDs;

    @ApiModelProperty("货品详情图片原始名")
    private List<String> detailImageOriginalNames;

    @ApiModelProperty("货品详情图片地址")
    private List<String> detailImagePositions;

    @ApiModelProperty("货品详情图片ID")
    private List<Integer> detailImageIDs;

    @ApiModelProperty("货品所属企业名称")
    private String ownerName;
}
