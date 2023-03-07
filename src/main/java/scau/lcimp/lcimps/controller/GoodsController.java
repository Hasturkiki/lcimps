package scau.lcimp.lcimps.controller;

import scau.lcimp.lcimps.common.PageResult;
import scau.lcimp.lcimps.common.R;
import scau.lcimp.lcimps.domain.Goods;
import scau.lcimp.lcimps.vo.GoodsPageQuery;
import scau.lcimp.lcimps.vo.GoodsVo;
import scau.lcimp.lcimps.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "货品管理")
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    @ApiImplicitParam(name = "queryParams", value = "基础分页请求对象", required = true)
    @ApiOperation("分页获取货品列表扩展：PageNum=0时代表获取所有货品")
    @GetMapping("/getGoodsListVoByPage")
    public PageResult<GoodsVo> getGoodsListVoByPage(GoodsPageQuery queryParams) {
        if (queryParams.getPageNum() == 0)
            return goodsService.getAllGoodsVoList();
        else
            return goodsService.getGoodsVoListByPage(queryParams);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取单个货品详情")
    @ApiImplicitParam(name = "id", value = "货品ID", required = true)
    public R<GoodsVo> getGoodsVoById(@PathVariable Integer id) {
        return goodsService.getGoodsVoById(id);
    }

    @PostMapping
    @ApiOperation("更新货品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goods", value = "货品数据信息的实体", required = true, paramType = "body"),
            @ApiImplicitParam(name = "mainImages", value = "货品描述图图片"),
            @ApiImplicitParam(name = "detailImages", value = "货品详细图图片")
    })
    public R<Boolean> updateGoods(
            @RequestPart Goods goods, @RequestPart(required = false) MultipartFile[] mainImages, @RequestPart(required = false) MultipartFile[] detailImages,
            @RequestPart Integer[] deleteMainImageIds, @RequestPart Integer[] deleteDetailImageIds) {
        return goodsService.updateWithCheck(goods, mainImages, detailImages, deleteMainImageIds, deleteDetailImageIds);
    }

    @PostMapping("/AdminAddGoods")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goods", value = "货品数据信息的实体", required = true, paramType = "body"),
            @ApiImplicitParam(name = "mainImages", value = "货品描述图图片", required = true),
            @ApiImplicitParam(name = "detailImages", value = "货品详细图图片", required = true)
    })
    @ApiOperation("管理员新增货品")
    public R<String> AdminAddGoods(
            @RequestPart Goods goods, @RequestPart MultipartFile[] mainImages, @RequestPart MultipartFile[] detailImages) {
        return goodsService.AdminAddGoods(goods, mainImages, detailImages);
    }

    @PostMapping("/CompanyAddGoods")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goods", value = "货品数据信息的实体", required = true, paramType = "body"),
            @ApiImplicitParam(name = "mainImages", value = "货品描述图图片", required = true),
            @ApiImplicitParam(name = "detailImages", value = "货品详细图图片", required = true)
    })
    @ApiOperation("企业新增货品")
    public R<String> CompanyAddGoods(
            @RequestPart Goods goods, @RequestPart MultipartFile[] mainImages, @RequestPart MultipartFile[] detailImages) {
        return goodsService.CompanyAddGoods(goods, mainImages, detailImages);
    }

    @DeleteMapping("/{ids}")
    @ApiOperation("管理员通过货品id来删除货品，多个用户ID以英文逗号(,)间隔")
    @ApiImplicitParam(name = "ids", value = "IDs", required = true)
    public R<Boolean> AdminDeleteGoods(@PathVariable String ids) {
        return goodsService.AdminDeleteGoodsByIds(ids);
    }

    @DeleteMapping("/company/{ids}")
    @ApiOperation("企业通过货品id来删除货品，多个用户ID以英文逗号(,)间隔")
    @ApiImplicitParam(name = "ids", value = "IDs", required = true)
    public R<Boolean> CompanyDeleteGoods(@PathVariable String ids) {
        return goodsService.CompanyDeleteGoodsByIds(ids);
    }

    @GetMapping("/getAll")
    @ApiOperation("获取所有货品信息")
    public R<List<GoodsVo>> getGoodsList() {
        return goodsService.getGoodsList();
    }

    @PostMapping("/searchByName")
    @ApiOperation("前台搜索货品信息")
    public R<List<GoodsVo>> searchByName(@RequestParam String searchKey) {
        List<String> stringList = new ArrayList<>();
        stringList.add(searchKey);
        return goodsService.searchByName(stringList);
    }
}
