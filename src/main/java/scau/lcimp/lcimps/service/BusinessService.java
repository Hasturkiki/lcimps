package scau.lcimp.lcimps.service;

import scau.lcimp.lcimps.common.PageResult;
import scau.lcimp.lcimps.common.R;
import scau.lcimp.lcimps.domain.Business;
import com.baomidou.mybatisplus.extension.service.IService;
import scau.lcimp.lcimps.domain.Goods;
import scau.lcimp.lcimps.vo.BusinessPageQuery;
import scau.lcimp.lcimps.vo.BusinessVo;

import java.util.List;

/**
* @author Hastur kiki
* @description 针对表【business(业务)】的数据库操作Service
* @createTime 2023-02-20 00:42:08
*/
public interface BusinessService extends IService<Business> {

    List<BusinessVo> searchByName(String searchKey);

    PageResult<BusinessVo> getAllBusinessVoList();

    PageResult<BusinessVo> getBusinessVoListByPage(BusinessPageQuery queryParams);

    R<List<BusinessVo>> getBusinessList();

    R<BusinessVo> getBusinessVoById(Integer id);

    R<Boolean> deleteBusinessByIds(String idsStr);

    R<List<BusinessVo>> searchBusiness(String searchKey);

    R<Boolean> businessStart(Integer id, String goodsIdsStr);

    R<Boolean> businessComplete(Integer id);

    R<Boolean> businessCancel(Integer id);

    boolean updateByGoods(Goods goods);
}
