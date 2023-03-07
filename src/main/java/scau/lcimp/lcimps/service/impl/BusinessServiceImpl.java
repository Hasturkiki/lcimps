package scau.lcimp.lcimps.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import scau.lcimp.lcimps.common.PageResult;
import scau.lcimp.lcimps.common.R;
import scau.lcimp.lcimps.domain.*;
import scau.lcimp.lcimps.service.*;
import scau.lcimp.lcimps.mapper.BusinessMapper;
import org.springframework.stereotype.Service;
import scau.lcimp.lcimps.vo.BusinessPageQuery;
import scau.lcimp.lcimps.vo.BusinessVo;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Hastur kiki
 * @description 针对表【business(业务)】的数据库操作Service实现
 * @createTime 2023-02-20 00:42:08
 */
@Service
public class BusinessServiceImpl extends ServiceImpl<BusinessMapper, Business>
        implements BusinessService {

    @Resource
    private UserService userService;

    @Resource
    private GoodsService goodsService;

    @Resource
    private UserHistoryService userHistoryService;

    @Resource
    private GoodsHistoryService goodsHistoryService;

    @Override
    public List<BusinessVo> searchByName(String searchKey) {
        List<Business> businessList = getBusinessListKeyword(searchKey);
        if (businessList.size() == 0)
            return null;
        businessList = businessList.stream().distinct().toList();
        return businessList.stream().map(this::TransBusinessToVo).toList();
    }

    @Override
    public PageResult<BusinessVo> getAllBusinessVoList() {
        List<Business> businessList = lambdaQuery().orderByAsc(Business::getId).page(new Page<>(1, 1024)).getRecords();
        long total = lambdaQuery().orderByAsc(Business::getId).page(new Page<>(1, 1024)).getTotal();
        if (businessList.isEmpty()) {
            return PageResult.error("无业务数据");
        }
        return getBusinessVoPageResult(businessList, total);
    }

    @Override
    public PageResult<BusinessVo> getBusinessVoListByPage(BusinessPageQuery queryParams) {
        /* 参数构建 */
        int pageNum = queryParams.getPageNum();
        int pageSize = queryParams.getPageSize();
        String keywords = queryParams.getKeywords();
        Integer upId = queryParams.getUpId();
        Integer companyId = queryParams.getCompanyId();
        Integer state = queryParams.getState();
        List<Business> businessList;
        long total;

        if (upId != null && upId == 0)
            upId = null;
        if (companyId != null && companyId == 0)
            companyId = null;
        if (state != null && state == -1)
            state = null;
        if (upId == null) {
            if (companyId == null) {
                if (state == null) {
                    businessList = lambdaQuery().orderByAsc(Business::getId).page(new Page<>(pageNum, pageSize)).getRecords();
                    total = lambdaQuery().orderByAsc(Business::getId).page(new Page<>(pageNum, pageSize)).getTotal();
                } else {
                    businessList = lambdaQuery().eq(Business::getState, state).orderByAsc(Business::getId).page(new Page<>(pageNum, pageSize)).getRecords();
                    total = lambdaQuery().eq(Business::getState, state).orderByAsc(Business::getId).page(new Page<>(pageNum, pageSize)).getTotal();
                }
            } else {
                if (state == null) {
                    businessList = lambdaQuery().eq(Business::getCompanyId, companyId).orderByAsc(Business::getId).page(new Page<>(pageNum, pageSize)).getRecords();
                    total = lambdaQuery().eq(Business::getCompanyId, companyId).orderByAsc(Business::getId).page(new Page<>(pageNum, pageSize)).getTotal();
                } else {
                    businessList = lambdaQuery().eq(Business::getCompanyId, companyId).eq(Business::getState, state).orderByAsc(Business::getId).page(new Page<>(pageNum, pageSize)).getRecords();
                    total = lambdaQuery().eq(Business::getCompanyId, companyId).eq(Business::getState, state).orderByAsc(Business::getId).page(new Page<>(pageNum, pageSize)).getTotal();
                }
            }
        } else {
            if (companyId == null) {
                if (state == null) {
                    businessList = lambdaQuery().eq(Business::getUpId, upId).orderByAsc(Business::getId).page(new Page<>(pageNum, pageSize)).getRecords();
                    total = lambdaQuery().eq(Business::getUpId, upId).orderByAsc(Business::getId).page(new Page<>(pageNum, pageSize)).getTotal();
                } else {
                    businessList = lambdaQuery().eq(Business::getUpId, upId).eq(Business::getState, state).orderByAsc(Business::getId).page(new Page<>(pageNum, pageSize)).getRecords();
                    total = lambdaQuery().eq(Business::getUpId, upId).eq(Business::getState, state).orderByAsc(Business::getId).page(new Page<>(pageNum, pageSize)).getTotal();
                }
            } else {
                if (state == null) {
                    businessList = lambdaQuery().eq(Business::getUpId, upId).eq(Business::getCompanyId, companyId).orderByAsc(Business::getId).page(new Page<>(pageNum, pageSize)).getRecords();
                    total = lambdaQuery().eq(Business::getUpId, upId).eq(Business::getCompanyId, companyId).orderByAsc(Business::getId).page(new Page<>(pageNum, pageSize)).getTotal();
                } else {
                    businessList = lambdaQuery().eq(Business::getUpId, upId).eq(Business::getCompanyId, companyId).eq(Business::getState, state).orderByAsc(Business::getId).page(new Page<>(pageNum, pageSize)).getRecords();
                    total = lambdaQuery().eq(Business::getUpId, upId).eq(Business::getCompanyId, companyId).eq(Business::getState, state).orderByAsc(Business::getId).page(new Page<>(pageNum, pageSize)).getTotal();
                }
            }
        }
        if (keywords != null) {
            List<Business> businessListFromKeyword = getBusinessListKeyword(keywords);
            businessList = businessList.stream().filter(businessList::contains).filter(businessListFromKeyword::contains).toList();
            total = businessList.size();
        }
        return getBusinessVoPageResult(businessList, total);
    }

    /**
     * 获取所有业务
     *
     * @return R<List < BusinessVo>
     */
    @Override
    public R<List<BusinessVo>> getBusinessList() {
        List<Business> businessList = lambdaQuery().orderByDesc(Business::getId).list();
        if (businessList.size() == 0) {
            return R.error("没有查找到数据");
        }
        List<BusinessVo> businessVoList = businessList.stream().map(this::TransBusinessToVo).toList();
        return R.success(businessVoList);
    }

    /**
     * 根据业务的ID获取单个业务的详细信息
     *
     * @param id
     * @return R<BusinessVo>
     */
    @Override
    public R<BusinessVo> getBusinessVoById(Integer id) {
        Business business = lambdaQuery().eq(Business::getId, id).one();
        if (business == null) {
            return R.error("不存在对应业务");
        }
        return R.success(TransBusinessToVo(business));
    }

    @Override
    public R<Boolean> deleteBusinessByIds(String idsStr) {
        Assert.isTrue(StrUtil.isNotBlank(idsStr), "删除的业务数据为空");
        List<Integer> ids = Arrays.stream(idsStr.split(",")).map(Integer::parseInt).toList();
        if (removeByIds(ids)) {
            return R.success(true);
        } else {
            return R.error("删除失败");
        }
    }

    /**
     * 根据搜索关键词查询业务信息。
     *
     * @param searchKey
     * @return R<List < BusinessVo>
     */
    @Override
    public R<List<BusinessVo>> searchBusiness(String searchKey) {
        List<BusinessVo> businessVoList = searchByName(searchKey);
        if (businessVoList == null) {
            return R.error("无相应业务");
        }
        return R.success(businessVoList);
    }

    /**
     * 签订业务
     *
     * @param id
     * @param goodsIdsStr
     * @return R<Boolean>
     */
    @Override
    public R<Boolean> businessStart(Integer id, String goodsIdsStr) {
        Assert.isTrue(StrUtil.isNotBlank(goodsIdsStr), "所选择的货品数据异常（货品为空）");
        List<Integer> goodsIds = Arrays.stream(goodsIdsStr.split(",")).map(Integer::parseInt).toList();
        User up = userService.lambdaQuery().eq(User::getId, id).one();
        if (up == null || !up.getIsEnabled())
            return R.error("主播账号异常");

        for (Integer goodsId : goodsIds) {
            Goods goods = goodsService.lambdaQuery().eq(Goods::getId, goodsId).one();
            if (goods == null)
                return R.error("编号为" + goodsId + "的货品信息异常（货品不存在）");
        }
        for (Integer goodsId : goodsIds) {
            Integer companyId = goodsService.lambdaQuery().eq(Goods::getId, goodsId).one().getOwnerId();
            BigDecimal brokerage = goodsService.lambdaQuery().eq(Goods::getId, goodsId).one().getBrokerage();
            User company = userService.lambdaQuery().eq(User::getId, companyId).one();
            if (company == null || !company.getIsEnabled())
                return R.error("企业账号异常");
            Business business = new Business();
            business.setUpId(id);
            business.setCompanyId(companyId);
            business.setGoodsId(goodsId);
            business.setBrokerage(brokerage);
            business.setState(0);
            if (!save(business))
                return R.error("签订业务失败");
        }
        return R.success(true);
    }

    @Override
    public R<Boolean> businessComplete(Integer id) {
        Business business = lambdaQuery().eq(Business::getId, id).one();
        if (business != null)
            if (business.getState() == 0) {
                business.setState(1);
                if (updateById(business)) {
                    return R.success(true);
                } else {
                    return R.error("结算业务失败！");
                }
            } else if (business.getState() == 1)
                return R.error("业务已结算，不能重复结算。");
            else if (business.getState() == 2)
                return R.error("业务已取消，无法结算。");
            else
                return R.error("结算业务失败！");
        else
            return R.error("业务信息异常，结算失败！");
    }

    @Override
    public R<Boolean> businessCancel(Integer id) {
        Business business = lambdaQuery().eq(Business::getId, id).one();
        if (business != null)
            if (business.getState() == 0) {
                business.setState(2);
                if (updateById(business)) {
                    return R.success(true);
                } else {
                    return R.error("取消业务失败！");
                }
            } else if (business.getState() == 1)
                return R.error("业务已结算，不能取消。");
            else if (business.getState() == 2)
                return R.error("业务已取消，不能重复取消。");
            else
                return R.error("取消业务失败！");
        else
            return R.error("业务信息异常，结算失败！");
    }

    @Override
    public boolean updateByGoods(Goods goods) {
        Integer goodsId = goods.getId();
        Integer companyId = goods.getOwnerId();
        List<Business> businessList = lambdaQuery().eq(Business::getGoodsId, goodsId).list();
        if (businessList.size() == 0)
            return true;
        if (businessList.get(0).getCompanyId().equals(companyId))
            return true;
        for (Business business : businessList) {
            business.setCompanyId(companyId);
            if (!updateById(business))
                return false;
        }

        return true;
    }

    private List<Business> getBusinessListKeyword(String keywords) {
        List<Business> businessList = new ArrayList<>();
        List<Integer> goodsIdList = null;
        List<Integer> userIdList = null;

        if (userHistoryService.searchByName(keywords) != null)
            userIdList = userHistoryService.searchByName(keywords).stream().map(UserHistory::getId).toList();
        if (goodsHistoryService.searchByName(keywords) != null)
            goodsIdList = goodsHistoryService.searchByName(keywords).stream().map(GoodsHistory::getId).toList();

        if (userIdList != null)
            for (Integer userId : userIdList)
                businessList.addAll(
                        lambdaQuery().eq(Business::getUpId, userId)
                                .or().eq(Business::getCompanyId, userId).list());
        if (goodsIdList != null)
            for (Integer goodsId : goodsIdList)
                businessList.addAll(
                        lambdaQuery().eq(Business::getGoodsId, goodsId).list());
        return businessList;
    }

    /**
     * 将传入的Business进行转换封装成一个Vo对象
     * 方法封装
     *
     * @param business
     * @return BusinessVo
     */
    private BusinessVo TransBusinessToVo(Business business) {
        BusinessVo businessVo = new BusinessVo();
        //  复制business信息
        BeanUtil.copyProperties(business, businessVo);
        //  设置业务关联主播名称
        businessVo.setUpName(userHistoryService.lambdaQuery().eq(UserHistory::getId, business.getUpId()).one().getUsername());
        //  设置业务关联企业名称
        businessVo.setCompanyName(userHistoryService.lambdaQuery().eq(UserHistory::getId, business.getCompanyId()).one().getUsername());
        //  设置业务关联货品名称
        businessVo.setGoodsName(goodsHistoryService.lambdaQuery().eq(GoodsHistory::getId, business.getGoodsId()).one().getGoodsName());
        return businessVo;
    }

    /**
     * 将传入的BusinessList转换封装成一个PageResult对象
     * 方法封装
     *
     * @param businessList
     * @return PageResult<BusinessVo>
     */
    private PageResult<BusinessVo> getBusinessVoPageResult(List<Business> businessList, long total) {
        List<BusinessVo> businessVoList = businessList.stream().map(this::TransBusinessToVo).toList();
        Page<BusinessVo> businessVoPage = new Page<>();
        businessVoPage.setTotal(total);
        businessVoPage.setRecords(businessVoList);
        return PageResult.success(businessVoPage);
    }
}




