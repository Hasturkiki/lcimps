package scau.lcimp.lcimps.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import scau.lcimp.lcimps.common.PageResult;
import scau.lcimp.lcimps.common.R;
import scau.lcimp.lcimps.domain.*;
import scau.lcimp.lcimps.service.*;
import scau.lcimp.lcimps.mapper.GoodsMapper;
import org.springframework.stereotype.Service;
import scau.lcimp.lcimps.vo.GoodsPageQuery;
import scau.lcimp.lcimps.vo.GoodsVo;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Hastur kiki
 * @description 针对表【goods(货品)】的数据库操作Service实现
 * @createTime 2023-02-20 00:42:08
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods>
        implements GoodsService {

    @Resource
    private UserService userService;

    @Resource
    private BusinessService businessService;

    @Resource
    private GoodsMainImageService goodsMainImageService;

    @Resource
    private GoodsDetailImageService goodsDetailImageService;

    @Resource
    private GoodsHistoryService goodsHistoryService;

    @Value("${spring.servlet.multipart.location}")
    public String uploadRootPath;

    @Override
    public List<GoodsVo> searchByName(String searchKey) {
        List<Goods> goodsList = lambdaQuery().like(Goods::getGoodsName, searchKey).list();
        if (goodsList.isEmpty()) {
            return null;
        }
        return goodsList.stream().map(this::TransGoodsToVo).toList();
    }

    @Override
    public PageResult<GoodsVo> getAllGoodsVoList() {
        List<Goods> goodsList = lambdaQuery().orderByAsc(Goods::getId).page(new Page<>(1, 1024)).getRecords();
        long total = lambdaQuery().orderByAsc(Goods::getId).page(new Page<>(1, 1024)).getTotal();
        if (goodsList.isEmpty()) {
            return PageResult.error("无货品数据");
        }
        return getGoodsVoPageResult(goodsList, total);
    }

    @Override
    public PageResult<GoodsVo> getGoodsVoListByPage(GoodsPageQuery queryParams) {
        /* 参数构建 */
        int pageNum = queryParams.getPageNum();
        int pageSize = queryParams.getPageSize();
        String keywords = queryParams.getKeywords();
        Integer ownerId = queryParams.getOwnerId();
        List<Goods> goodsList;
        long total;

        if (ownerId != null && ownerId == 0)
            ownerId = null;
        if (keywords == null) {
            if (ownerId == null) {
                goodsList = lambdaQuery().orderByAsc(Goods::getId).page(new Page<>(pageNum, pageSize)).getRecords();
                total = lambdaQuery().orderByAsc(Goods::getId).page(new Page<>(pageNum, pageSize)).getTotal();
            } else {
                goodsList = lambdaQuery().eq(Goods::getOwnerId, ownerId).orderByAsc(Goods::getId).page(new Page<>(pageNum, pageSize)).getRecords();
                total = lambdaQuery().eq(Goods::getOwnerId, ownerId).orderByAsc(Goods::getId).page(new Page<>(pageNum, pageSize)).getTotal();
            }
        } else {
            if (ownerId == null) {
                goodsList = lambdaQuery().like(Goods::getGoodsName, keywords).orderByAsc(Goods::getId).page(new Page<>(pageNum, pageSize)).getRecords();
                total = lambdaQuery().like(Goods::getGoodsName, keywords).orderByAsc(Goods::getId).page(new Page<>(pageNum, pageSize)).getTotal();
            } else {
                goodsList = lambdaQuery().like(Goods::getGoodsName, keywords).eq(Goods::getOwnerId, ownerId).orderByAsc(Goods::getId).page(new Page<>(pageNum, pageSize)).getRecords();
                total = lambdaQuery().like(Goods::getGoodsName, keywords).eq(Goods::getOwnerId, ownerId).orderByAsc(Goods::getId).page(new Page<>(pageNum, pageSize)).getTotal();
            }
        }
        return getGoodsVoPageResult(goodsList, total);
    }

    /**
     * 获取所有货品
     *
     * @return R<List < GoodsVo>
     */
    @Override
    public R<List<GoodsVo>> getGoodsList() {
        List<Goods> goodsList = lambdaQuery().orderByDesc(Goods::getId).list();
        List<GoodsVo> goodsVoList = goodsList.stream().map(this::TransGoodsToVo).collect(Collectors.toList());
        return R.success(goodsVoList);
    }

    /**
     * 前台搜索货品
     *
     * @return R<List < GoodsVo>
     */
    @Override
    public R<List<GoodsVo>> searchByName(List<String> stringList) {
        if (stringList.isEmpty()) {
            return R.error("无对应货品");
        }
        List<Goods> goodsList = new ArrayList<>();
        for (String string : stringList) {
            goodsList.addAll(lambdaQuery().like(Goods::getGoodsName, string).list());
        }
        List<Goods> goodsList2 = goodsList.stream().distinct().toList();
        List<GoodsVo> goodsVoList = new ArrayList<>();
        for (Goods goods : goodsList2) {
            goodsVoList.add(TransGoodsToVo(goods));
        }
        return R.success(goodsVoList);
    }

    /**
     * 查询一个货品的详细信息
     *
     * @param id
     * @return R<GoodsVo>
     */
    @Override
    public R<GoodsVo> getGoodsVoById(Integer id) {
        Goods goods = lambdaQuery().eq(Goods::getId, id).one();
        if (goods == null) {
            R.error("不存在对应货品");
        }
        return R.success(TransGoodsToVo(goods));
    }

    @Override
    public R<Boolean> updateWithCheck(Goods goods, MultipartFile[] mainImages, MultipartFile[] detailImages, Integer[] deleteMainImageIds, Integer[] deleteDetailImageIds) {
        goodsMainImageService.removeByIds(Arrays.stream(deleteMainImageIds).toList());
        goodsDetailImageService.removeByIds(Arrays.stream(deleteDetailImageIds).toList());
        if (lambdaQuery().eq(Goods::getId, goods.getId()).list().size() == 0)
            return R.error("货品信息异常，更新失败");
        if (userService.lambdaQuery().eq(User::getId, goods.getOwnerId()).list().size() == 0)
            return R.error("所属企业信息异常(企业不存在)，更新失败");
        if (userService.lambdaQuery().eq(User::getId, goods.getOwnerId()).one().getType() != 1)
            return R.error("所属企业信息异常（所选用户类型错误），更新失败");
        if (mainImages != null) {
            //  添加货品描述图片
            for (MultipartFile mainImage : mainImages) {
                String orgName = mainImage.getOriginalFilename();
                String extName = orgName.substring(orgName.lastIndexOf('.'));
                String destName = UUID.randomUUID().toString().toUpperCase() + extName;
                try {
                    mainImage.transferTo(new File(uploadRootPath, destName));
                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                    return R.error("描述图图片上传失败");
                }
                GoodsMainImage goodsMainImage = new GoodsMainImage();
                goodsMainImage.setGoodsId(goods.getId());
                goodsMainImage.setImageName(destName);
                goodsMainImage.setImagePosition("/pic/" + destName);
                goodsMainImage.setOriginalName(orgName);
                goodsMainImageService.save(goodsMainImage);
            }
        }
        if (detailImages != null) {
            //  添加货品详细图片
            for (MultipartFile file : detailImages) {
                if (!file.isEmpty()) {
                    String orgName = file.getOriginalFilename();
                    String extName = orgName.substring(orgName.lastIndexOf('.'));
                    String destName = UUID.randomUUID().toString().toUpperCase() + extName;
                    try {
                        file.transferTo(new File(uploadRootPath, destName));
                    } catch (IllegalStateException | IOException e) {
                        e.printStackTrace();
                        return R.error("图片上传失败");
                    }
                    GoodsDetailImage detailImage = new GoodsDetailImage();
                    detailImage.setGoodsId(goods.getId());
                    detailImage.setImageName(destName);
                    detailImage.setImagePosition("/pic/" + destName);
                    detailImage.setOriginalName(orgName);
                    goodsDetailImageService.save(detailImage);
                }
            }
        }
        GoodsHistory goodsHistory = goodsHistoryService.lambdaQuery().eq(GoodsHistory::getId, goods.getId()).one();
        if (goodsHistory == null)
            return R.error("货品历史记录不存在，更新失败");
        if (updateById(goods)) {
            if (businessService.updateByGoods(goods)) {
                goodsHistory.setGoodsName(goods.getGoodsName());
                goodsHistory.setOwnerId(goods.getOwnerId());
                if (goodsHistoryService.updateById(goodsHistory))
                    return R.success(true);
                else
                    return R.error("货品信息更新成功，但货品历史记录更新失败");
            } else
                return R.error("货品信息更新成功，但业务相应信息更新失败");
        } else
            return R.error("货品信息更新失败");
    }

    /**
     * 管理员新增货品
     *
     * @param goods
     * @param mainImages
     * @param detailImages
     * @return R<String>
     */
    @Override
    public R<String> AdminAddGoods(Goods goods, MultipartFile[] mainImages, MultipartFile[] detailImages) {
        return addGoods(goods, mainImages, detailImages);
    }

    /**
     * 企业新增货品
     *
     * @param goods
     * @param mainImages
     * @param detailImages
     * @return R<String>
     */
    @Override
    public R<String> CompanyAddGoods(@RequestParam Goods goods, MultipartFile[] mainImages, MultipartFile[] detailImages) {
        if (userService.getUserById(goods.getOwnerId()).getCode().equals("404"))
            return R.error("用户信息异常（用户不存在）");
        if (userService.getUserById(goods.getOwnerId()).getData().getType() != 1)
            return R.error("用户类型错误（非企业用户）");
        return addGoods(goods, mainImages, detailImages);
    }

    private R<String> addGoods(@RequestParam Goods goods, MultipartFile[] mainImages, MultipartFile[] detailImages) {
        int indexNow = lambdaQuery().eq(Goods::getGoodsName, goods.getGoodsName()).list().size();
        if (save(goods)) {
            if (mainImages != null) {
                //  添加货品描述图片
                for (MultipartFile mainImage : mainImages) {
                    String orgName = mainImage.getOriginalFilename();
                    String extName = orgName.substring(orgName.lastIndexOf('.'));
                    String destName = UUID.randomUUID().toString().toUpperCase() + extName;
                    try {
                        mainImage.transferTo(new File(uploadRootPath, destName));
                    } catch (IllegalStateException | IOException e) {
                        e.printStackTrace();
                        return R.error("描述图图片上传失败");
                    }
                    GoodsMainImage goodsMainImage = new GoodsMainImage();
                    goodsMainImage.setGoodsId(goods.getId());
                    goodsMainImage.setImageName(destName);
                    goodsMainImage.setImagePosition("/pic/" + destName);
                    goodsMainImage.setOriginalName(orgName);
                    goodsMainImageService.save(goodsMainImage);
                }
            }
            if (detailImages != null) {
                //  添加货品详细图片
                for (MultipartFile file : detailImages) {
                    if (!file.isEmpty()) {
                        String orgName = file.getOriginalFilename();
                        String extName = orgName.substring(orgName.lastIndexOf('.'));
                        String destName = UUID.randomUUID().toString().toUpperCase() + extName;
                        try {
                            file.transferTo(new File(uploadRootPath, destName));
                        } catch (IllegalStateException | IOException e) {
                            e.printStackTrace();
                            return R.error("图片上传失败");
                        }
                        GoodsDetailImage detailImage = new GoodsDetailImage();
                        detailImage.setGoodsId(goods.getId());
                        detailImage.setImageName(destName);
                        detailImage.setImagePosition("/pic/" + destName);
                        detailImage.setOriginalName(orgName);
                        goodsDetailImageService.save(detailImage);
                    }
                }
            }

            Goods goodsLog = lambdaQuery().eq(Goods::getGoodsName, goods.getGoodsName()).orderByAsc(Goods::getCreateTime).list().get(indexNow);
            if (goodsLog == null)
                return R.error("新增货品失败");

            GoodsHistory goodsHistory = new GoodsHistory();
            goodsHistory.setId(goodsLog.getId());
            goodsHistory.setGoodsName(goodsLog.getGoodsName());
            goodsHistory.setOwnerId(goodsLog.getOwnerId());
            if (!goodsHistoryService.save(goodsHistory))
                return R.error("货品创建成功但货品历史记录创建失败");

            return R.success("货品图片添加成功");
        } else
            return R.error("货品添加失败");
    }

    @Override
    public R<Boolean> AdminDeleteGoodsByIds(String idsStr) {
        Assert.isTrue(StrUtil.isNotBlank(idsStr), "删除的货品数据为空");
        List<Integer> ids = Arrays.stream(idsStr.split(",")).map(Integer::parseInt).toList();
        if (removeByIds(ids)) {
            if (goodsHistoryService.setSleepByIds(ids))
                return R.success(true);
            else
                return R.error("货品删除成功，但货品历史记录更新失败");
        } else {
            return R.error("删除失败");
        }
    }

    @Override
    public R<Boolean> CompanyDeleteGoodsByIds(String idsStr) {
        Assert.isTrue(StrUtil.isNotBlank(idsStr), "删除的货品数据为空");
        List<Integer> ids = Arrays.stream(idsStr.split(",")).map(Integer::parseInt).toList();
        if (removeByIds(ids)) {
            if (goodsHistoryService.setSleepByIds(ids))
                return R.success(true);
            else
                return R.error("货品删除成功，但货品历史记录更新失败");
        } else {
            return R.error("删除失败");
        }
    }

    /**
     * 将传入的Goods进行转换封装成一个Vo对象
     * 方法封装
     *
     * @param goods
     * @return GoodsVo
     */
    private GoodsVo TransGoodsToVo(Goods goods) {
        GoodsVo goodsVo = new GoodsVo();
        //  复制goods信息
        BeanUtil.copyProperties(goods, goodsVo);
        //  设置货品描述图片ID、原始名与图片地址
        List<GoodsMainImage> mainImages = goodsMainImageService.lambdaQuery().eq(GoodsMainImage::getGoodsId, goods.getId()).list();
        goodsVo.setMainImageOriginalNames(mainImages.stream().map(GoodsMainImage::getOriginalName).toList());
        goodsVo.setMainImagePositions(mainImages.stream().map(GoodsMainImage::getImagePosition).toList());
        goodsVo.setMainImageIDs(mainImages.stream().map(GoodsMainImage::getId).toList());
        //  设置货品详情图片ID、原始名与图片地址
        List<GoodsDetailImage> detailImages = goodsDetailImageService.lambdaQuery().eq(GoodsDetailImage::getGoodsId, goods.getId()).list();
        goodsVo.setDetailImageOriginalNames(detailImages.stream().map(GoodsDetailImage::getOriginalName).toList());
        goodsVo.setDetailImagePositions(detailImages.stream().map(GoodsDetailImage::getImagePosition).toList());
        goodsVo.setDetailImageIDs(detailImages.stream().map(GoodsDetailImage::getId).toList());
        //  设置货品所属企业名称
        goodsVo.setOwnerName(userService.getUserById(goods.getOwnerId()).getData().getUsername());
        return goodsVo;
    }

    /**
     * 将传入的GoodsList转换封装成一个PageResult对象
     * 方法封装
     *
     * @param goodsList
     * @return PageResult<GoodsVo>
     */
    private PageResult<GoodsVo> getGoodsVoPageResult(List<Goods> goodsList, long total) {
        List<GoodsVo> goodsVoList = goodsList.stream().map(this::TransGoodsToVo).toList();
        Page<GoodsVo> goodsVoPage = new Page<>();
        goodsVoPage.setTotal(total);
        goodsVoPage.setRecords(goodsVoList);
        return PageResult.success(goodsVoPage);
    }
}
