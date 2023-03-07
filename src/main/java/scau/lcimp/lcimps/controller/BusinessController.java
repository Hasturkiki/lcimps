package scau.lcimp.lcimps.controller;

import scau.lcimp.lcimps.common.PageResult;
import scau.lcimp.lcimps.common.R;
import scau.lcimp.lcimps.domain.*;
import scau.lcimp.lcimps.service.*;
import scau.lcimp.lcimps.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Api(tags = "业务管理")
@RestController
@RequestMapping("/business")
public class BusinessController {

    @Resource
    private BusinessService businessService;

    @ApiImplicitParam(name = "queryParams", value = "基础分页请求对象", required = true)
    @ApiOperation("分页获取业务列表扩展：PageNum=0时代表获取所有业务")
    @GetMapping("getBusinessListVoByPage")
    public PageResult<BusinessVo> getBusinessListVoByPage(BusinessPageQuery queryParams) {
        if (queryParams.getPageNum() == 0)
            return businessService.getAllBusinessVoList();
        else
            return businessService.getBusinessVoListByPage(queryParams);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取单个业务详情")
    @ApiImplicitParam(name = "id", value = "业务Id", required = true)
    public R<BusinessVo> getBusinessOne(@PathVariable Integer id) {
        return businessService.getBusinessVoById(id);
    }

    @PostMapping("/AdminAddBusiness")
    @ApiImplicitParam(name = "business", value = "货品实体", required = true)
    @ApiOperation("管理员新增业务")
    public R<Boolean> AdminAddBusiness(@RequestBody Business business) {
        if(businessService.save(business))
            return R.success(true);
        else
            return R.error("新增业务失败");
    }

    @PutMapping("/updateBusiness")
    @ApiOperation("更新业务")
    public R<Business> updateBusiness(@RequestBody Business business) {
        if (businessService.updateById(business)) {
            return R.success(business);
        } else {
            return R.error("更新业务失败");
        }
    }

    @DeleteMapping("/{ids}")
    @ApiOperation("通过用户id来删除业务，多个业务ID以英文逗号(,)间隔")
    @ApiImplicitParam(name = "ids", value = "IDs", required = true)
    public R<Boolean> deleteBusinessByIds(@PathVariable String ids) {
        return businessService.deleteBusinessByIds(ids);
    }

    @DeleteMapping
    @ApiOperation("根据业务id删除业务")
    @ApiImplicitParam(name = "id", value = "业务Id", required = true)
    public R<Boolean> deleteBusiness(Integer id) {
        if (businessService.removeById(id)) {
            return R.success(true);
        } else {
            return R.error("业务删除失败！");
        }
    }

    @PutMapping("/businessStart")
    @ApiOperation("签订业务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主播ID", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "goodsIds", value = "货品ID", required = true, paramType = "query", dataType = "String")
    })
    public R<Boolean> businessStart(
            @RequestParam(value = "id") Integer id,
            @RequestParam(value = "goodsIds") String goodsIds) {
        return businessService.businessStart(id, goodsIds);
    }

    @PutMapping("/businessComplete")
    @ApiOperation("企业确认业务已完成")
    @ApiImplicitParam(name = "id", value = "业务Id", required = true, paramType = "query", dataType = "Integer")
    public R<Boolean> businessComplete(@RequestParam("id") Integer id) {
        return businessService.businessComplete(id);
    }

    @PutMapping("/businessCancel")
    @ApiOperation("企业/主播取消业务")
    @ApiImplicitParam(name = "id", value = "业务Id", required = true, paramType = "query", dataType = "Integer")
    public R<Boolean> businessCancel(@RequestParam("id") Integer id) {
        return businessService.businessCancel(id);
    }

    @GetMapping("/getAll")
    @ApiOperation("获取所有业务信息")
    public R<List<BusinessVo>> getBusinessList() {
        return businessService.getBusinessList();
    }

    @GetMapping("/search")
    @ApiOperation("查询业务信息")
    @ApiImplicitParam(name = "searchKey", value = "搜索关键词", required = true, paramType = "query", dataType = "String")
    public R<List<BusinessVo>> searchBusiness(@RequestParam(value = "searchKey") String searchKey) {
        return businessService.searchBusiness(searchKey);
    }
}
