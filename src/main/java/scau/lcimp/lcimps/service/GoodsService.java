package scau.lcimp.lcimps.service;

import io.swagger.models.auth.In;
import org.springframework.web.multipart.MultipartFile;
import scau.lcimp.lcimps.common.PageResult;
import scau.lcimp.lcimps.common.R;
import scau.lcimp.lcimps.domain.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import scau.lcimp.lcimps.vo.GoodsPageQuery;
import scau.lcimp.lcimps.vo.GoodsVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Hastur kiki
 * @description 针对表【goods(货品)】的数据库操作Service
 * @createTime 2023-02-20 00:42:08
 */
public interface GoodsService extends IService<Goods> {

    List<GoodsVo> searchByName(String searchKey);

    PageResult<GoodsVo> getAllGoodsVoList();

    PageResult<GoodsVo> getGoodsVoListByPage(GoodsPageQuery queryParams);

    R<List<GoodsVo>> getGoodsList();

    R<GoodsVo> getGoodsVoById(Integer id);

    R<Boolean> updateWithCheck(Goods goods, MultipartFile[] mainImages, MultipartFile[] detailImages, Integer[] deleteMainImageIds, Integer[] deleteDetailImageIds);

    R<String> AdminAddGoods(Goods goods, MultipartFile[] mainImages, MultipartFile[] detailImages);

    R<String> CompanyAddGoods(Goods goods, MultipartFile[] mainImages, MultipartFile[] detailImages);

    R<Boolean> AdminDeleteGoodsByIds(String idsStr);

    R<Boolean> CompanyDeleteGoodsByIds(String ids);

    R<List<GoodsVo>> searchByName(List<String> stringList);
}
