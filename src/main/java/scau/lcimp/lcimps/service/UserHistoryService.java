package scau.lcimp.lcimps.service;

import scau.lcimp.lcimps.common.PageResult;
import scau.lcimp.lcimps.domain.UserHistory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Hastur kiki
* @description 针对表【user_history(用户历史记录)】的数据库操作Service
* @createTime 2023-03-04 16:22:51
*/
public interface UserHistoryService extends IService<UserHistory> {

    boolean setSleepByIds(List<Integer> ids);

    List<UserHistory> searchByName(String keywords);

    PageResult<UserHistory> getCompanyHistory();

    PageResult<UserHistory> getUpHistory();
}
