package scau.lcimp.lcimps.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import scau.lcimp.lcimps.common.PageResult;
import scau.lcimp.lcimps.common.R;
import scau.lcimp.lcimps.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import scau.lcimp.lcimps.domain.UserHistory;
import scau.lcimp.lcimps.vo.UserPageQuery;

import java.util.List;

/**
* @author Hastur kiki
* @description 针对表【user(用户)】的数据库操作Service
* @createTime 2023-02-20 00:42:08
*/
public interface UserService extends IService<User> {

    List<User> searchByName(String searchKey);

    R<User> adminLoginToSystem(String username, String password);

    R<User> loginToSystem(String username, String password);

    R<User> getUserLoginInfo(DecodedJWT verify);

    PageResult<User> getAllUserList();

    PageResult<User> getUserListByPage(UserPageQuery queryParams);

    PageResult<UserHistory> getCompanyHistory();

    PageResult<UserHistory> getUpHistory();

    R<List<User>> getUserList();

    R<User> getUserById(Integer id);

    R<List<User>> getSearchUserList(String name);

    R<Boolean> enableUserById(Integer id);

    R<Boolean> disableUserById(Integer id);

    R<Boolean> deleteUserByIds(String idsStr);

    R<Boolean> updatePassword(Integer id, String password);

    R<Boolean> updateUser(User user);

    R<User> AdminAddUser(String username, Integer type, String telephone, String email, boolean isEnabled);

    R<User> registerToSystem(String username, String password, String telephone, String email, Integer type);
}
