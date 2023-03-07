package scau.lcimp.lcimps.service;

import scau.lcimp.lcimps.domain.GoodsHistory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Hastur kiki
* @description 针对表【goods_history(货品历史记录)】的数据库操作Service
* @createTime 2023-03-04 16:22:51
*/
public interface GoodsHistoryService extends IService<GoodsHistory> {

    boolean setSleepByIds(List<Integer> ids);

    List<GoodsHistory> searchByName(String keywords);
}
