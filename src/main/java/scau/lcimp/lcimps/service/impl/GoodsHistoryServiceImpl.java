package scau.lcimp.lcimps.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import scau.lcimp.lcimps.domain.GoodsHistory;
import scau.lcimp.lcimps.service.GoodsHistoryService;
import scau.lcimp.lcimps.mapper.GoodsHistoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Hastur kiki
 * @description 针对表【goods_history(货品历史记录)】的数据库操作Service实现
 * @createTime 2023-03-04 16:22:51
 */
@Service
public class GoodsHistoryServiceImpl extends ServiceImpl<GoodsHistoryMapper, GoodsHistory>
        implements GoodsHistoryService {

    @Override
    public boolean setSleepByIds(List<Integer> ids) {
        for (Integer id : ids) {
            GoodsHistory goodsHistory = lambdaQuery().eq(GoodsHistory::getId, id).one();
            if (goodsHistory != null) {
                goodsHistory.setState(true);
                if (!updateById(goodsHistory))
                    return false;
            } else
                return false;
        }
        return true;
    }

    @Override
    public List<GoodsHistory> searchByName(String keywords) {
        List<GoodsHistory> goodsHistoryList = lambdaQuery().like(GoodsHistory::getGoodsName, keywords).list();
        if (goodsHistoryList.isEmpty())
            return null;
        return goodsHistoryList;
    }
}




