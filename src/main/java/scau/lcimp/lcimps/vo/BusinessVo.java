package scau.lcimp.lcimps.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import scau.lcimp.lcimps.domain.Business;

@Data
@ApiModel(value = "BusinessVo对象", description = "业务对象扩展")
public class BusinessVo extends Business {
    @ApiModelProperty("主播名称")
    private String upName;

    @ApiModelProperty("企业名称")
    private String companyName;

    @ApiModelProperty("货品名称")
    private String goodsName;
}
