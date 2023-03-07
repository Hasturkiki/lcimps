package scau.lcimp.lcimps.mapper;

import org.apache.ibatis.annotations.Mapper;
import scau.lcimp.lcimps.domain.UserHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Hastur kiki
* @description 针对表【user_history(用户历史记录)】的数据库操作Mapper
* @createTime 2023-03-04 16:22:51
* @Entity scau.lcimp.lcimps.domain.UserHistory
*/
@Mapper
public interface UserHistoryMapper extends BaseMapper<UserHistory> {

}




