package scau.lcimp.lcimps.mapper;

import org.apache.ibatis.annotations.Mapper;
import scau.lcimp.lcimps.domain.GoodsHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Hastur kiki
* @description 针对表【goods_history(货品历史记录)】的数据库操作Mapper
* @createTime 2023-03-04 16:22:51
* @Entity scau.lcimp.lcimps.domain.GoodsHistory
*/
@Mapper
public interface GoodsHistoryMapper extends BaseMapper<GoodsHistory> {

}




