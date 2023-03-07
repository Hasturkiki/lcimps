package scau.lcimp.lcimps.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import scau.lcimp.lcimps.common.PageResult;
import scau.lcimp.lcimps.domain.UserHistory;
import scau.lcimp.lcimps.service.UserHistoryService;
import scau.lcimp.lcimps.mapper.UserHistoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Hastur kiki
 * @description 针对表【user_history(用户历史记录)】的数据库操作Service实现
 * @createTime 2023-03-04 16:22:51
 */
@Service
public class UserHistoryServiceImpl extends ServiceImpl<UserHistoryMapper, UserHistory>
        implements UserHistoryService {

    @Override
    public boolean setSleepByIds(List<Integer> ids) {
        for (Integer id : ids) {
            UserHistory userHistory = lambdaQuery().eq(UserHistory::getId, id).one();
            if (userHistory != null) {
                userHistory.setState(true);
                if (!updateById(userHistory))
                    return false;
            } else
                return false;
        }
        return true;
    }

    @Override
    public List<UserHistory> searchByName(String keywords) {
        List<UserHistory> userHistoryList = lambdaQuery().like(UserHistory::getUsername, keywords).list();
        if (userHistoryList.isEmpty())
            return null;
        return userHistoryList;
    }

    @Override
    public PageResult<UserHistory> getCompanyHistory() {
        Page<UserHistory> userPage = lambdaQuery().eq(UserHistory::getType, 1).orderByAsc(UserHistory::getId).page(new Page<>(1, 1024));
        if (userPage.getRecords().size() == 0) {
            return PageResult.error("无主播数据");
        }
        return PageResult.success(userPage);
    }

    @Override
    public PageResult<UserHistory> getUpHistory() {
        Page<UserHistory> userPage = lambdaQuery().eq(UserHistory::getType, 2).orderByAsc(UserHistory::getId).page(new Page<>(1, 1024));
        if (userPage.getRecords().size() == 0) {
            return PageResult.error("无主播数据");
        }
        return PageResult.success(userPage);
    }
}




