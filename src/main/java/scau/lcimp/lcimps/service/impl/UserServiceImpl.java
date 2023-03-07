package scau.lcimp.lcimps.service.impl;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import scau.lcimp.lcimps.common.PageResult;
import scau.lcimp.lcimps.common.R;
import scau.lcimp.lcimps.domain.Goods;
import scau.lcimp.lcimps.domain.User;
import scau.lcimp.lcimps.domain.UserHistory;
import scau.lcimp.lcimps.service.GoodsHistoryService;
import scau.lcimp.lcimps.service.GoodsService;
import scau.lcimp.lcimps.service.UserHistoryService;
import scau.lcimp.lcimps.service.UserService;
import scau.lcimp.lcimps.mapper.UserMapper;
import org.springframework.stereotype.Service;
import scau.lcimp.lcimps.vo.UserPageQuery;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Hastur kiki
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createTime 2023-02-20 00:42:08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private GoodsService goodsService;

    @Resource
    private UserHistoryService userHistoryService;

    @Resource
    private GoodsHistoryService goodsHistoryService;

    @Override
    public List<User> searchByName(String searchKey) {
        List<User> userList = lambdaQuery().like(User::getUsername, searchKey).list();
        if (userList.isEmpty()) {
            return null;
        }
        return userList;
    }

    /**
     * 管理员输入账号和密码登入到系统
     *
     * @param userName
     * @param password
     * @return
     */
    @Override
    public R<User> adminLoginToSystem(String userName, String password) {
        if (StrUtil.isBlank(userName)) {
            return R.error("未输入管理员账号");
        }
        if (StrUtil.isBlank(password)) {
            return R.error("未输入管理员密码");
        }
        User user = lambdaQuery().eq(User::getUsername, userName).eq(User::getPassword, password).one();
        if (user == null) {
            return R.error("账号或密码不正确");
        }
        if (user.getType() != 0) {
            return R.error("账号权限不足");
        }
        if (!user.getIsEnabled()) {
            return R.error("管理员账号未启用，请检查数据库");
        }
        return R.success(user);
    }

    /**
     * 用户输入账号和密码登入到系统
     *
     * @param userName
     * @param password
     * @return
     */
    @Override
    public R<User> loginToSystem(String userName, String password) {
        if (StrUtil.isBlank(userName)) {
            return R.error("未输入用户名");
        }
        if (StrUtil.isBlank(password)) {
            return R.error("未输入密码");
        }
        User user = lambdaQuery().eq(User::getUsername, userName).eq(User::getPassword, password).one();
        if (user == null) {
            return R.error("用户名或密码不正确");
        }
        if (user.getType() == 0) {
            return R.error("账号类型错误");
        }
        if (!user.getIsEnabled()) {
            return R.error("账号已被封禁，请联系管理员");
        }
        return R.success(user);
    }

    @Override
    public R<User> getUserLoginInfo(DecodedJWT verify) {
        User user = lambdaQuery()
                .eq(User::getUsername, verify.getClaim("username").asString())
                .eq(User::getPassword, verify.getClaim("password").asString())
                .one();
        if (user == null)
            return R.error("不存在对应用户");
        return getUserR(user);
    }

    @Override
    public PageResult<User> getAllUserList() {
        Page<User> userPage = lambdaQuery().orderByAsc(User::getId).page(new Page<>(1, 1024));
        if (userPage.getRecords().size() == 0) {
            return PageResult.error("无用户数据");
        }
        return PageResult.success(userPage);
    }

    @Override
    public PageResult<User> getUserListByPage(UserPageQuery queryParams) {
        /* 参数构建 */
        int pageNum = queryParams.getPageNum();
        int pageSize = queryParams.getPageSize();
        String keywords = queryParams.getKeywords();
        Boolean isEnabled = queryParams.getIsEnabled();
        Integer type = queryParams.getType();
        Page<User> userPage;

        if (type != null && type == -1)
            type = null;
        if (keywords == null) {
            if (isEnabled == null) {
                if (type == null)
                    userPage = lambdaQuery().ne(User::getType, 0).orderByAsc(User::getId).page(new Page<>(pageNum, pageSize));
                else
                    userPage = lambdaQuery().ne(User::getType, 0).eq(User::getType, type).orderByAsc(User::getId).page(new Page<>(pageNum, pageSize));
            } else {
                if (type == null) {
                    userPage = lambdaQuery().ne(User::getType, 0).eq(User::getIsEnabled, isEnabled).orderByAsc(User::getId).page(new Page<>(pageNum, pageSize));
                } else
                    userPage = lambdaQuery().ne(User::getType, 0).eq(User::getIsEnabled, isEnabled).eq(User::getType, type).orderByAsc(User::getId).page(new Page<>(pageNum, pageSize));
            }
        } else {
            if (isEnabled == null) {
                if (type == null)
                    userPage = lambdaQuery().ne(User::getType, 0).like(User::getUsername, keywords).orderByAsc(User::getId).page(new Page<>(pageNum, pageSize));
                else
                    userPage = lambdaQuery().ne(User::getType, 0).like(User::getUsername, keywords).eq(User::getType, type).orderByAsc(User::getId).page(new Page<>(pageNum, pageSize));
            } else {
                if (type == null) {
                    userPage = lambdaQuery().ne(User::getType, 0).like(User::getUsername, keywords).eq(User::getIsEnabled, isEnabled).orderByAsc(User::getId).page(new Page<>(pageNum, pageSize));
                } else
                    userPage = lambdaQuery().ne(User::getType, 0).like(User::getUsername, keywords).eq(User::getIsEnabled, isEnabled).eq(User::getType, type).orderByAsc(User::getId).page(new Page<>(pageNum, pageSize));
            }
        }
        return PageResult.success(userPage);
    }

    @Override
    public PageResult<UserHistory> getCompanyHistory() {
        return userHistoryService.getCompanyHistory();
    }

    @Override
    public PageResult<UserHistory> getUpHistory() {
        return userHistoryService.getUpHistory();
    }

    /**
     * 获取所有用户
     *
     * @return R<List < GoodsVo>
     */
    @Override
    public R<List<User>> getUserList() {
        List<User> userList = lambdaQuery().orderByDesc(User::getId).list();
        if (userList.isEmpty()) {
            return R.error("没有查找到用户数据");
        }
        return R.success(userList);
    }

    /**
     * 根据id获取单个用户的信息
     *
     * @param id
     * @return R<User>
     */
    @Override
    public R<User> getUserById(Integer id) {
        User user = lambdaQuery().eq(User::getId, id).one();
        if (user == null) {
            return R.error("未查询到相应用户");
        }
        return getUserR(user);
    }

    /**
     * 根据用户名模糊搜索相关用户
     *
     * @param name
     * @return R<List < User>
     */
    @Override
    public R<List<User>> getSearchUserList(String name) {
        List<User> userList = lambdaQuery().like(User::getUsername, name).list();
        if (userList.isEmpty()) {
            return R.error("未查询到相应用户");
        }
        return R.success(userList);
    }

    /**
     * 根据用户Id启用用户账号
     *
     * @param id
     * @return R<Boolean>
     */
    @Override
    public R<Boolean> enableUserById(Integer id) {
        User user = lambdaQuery().eq(User::getId, id).one();
        if (user == null) {
            return R.error("用户不存在");
        }
        if (user.getIsEnabled()) {
            return R.error("重复启用行为，用户已启用");
        }
        boolean success = lambdaUpdate().eq(User::getId, id).set(User::getIsEnabled, true).update();
        if (success) {
            return R.success(true);
        } else {
            return R.error(id + "启用失败");
        }

    }

    /**
     * 根据用户Id禁用用户账号
     *
     * @param id
     * @return R<Boolean>
     */
    @Override
    public R<Boolean> disableUserById(Integer id) {
        User user = lambdaQuery().eq(User::getId, id).one();
        if (user == null) {
            return R.error("用户不存在");
        }
        if (!user.getIsEnabled()) {
            return R.error("重复封禁行为 " + id + "用户已被封禁");
        }
        boolean success = lambdaUpdate().eq(User::getId, id).set(User::getIsEnabled, false).update();
        if (success) {
            return R.success(true);
        } else {
            return R.error(id + "禁用失败");
        }
    }

    @Override
    public R<Boolean> deleteUserByIds(String idsStr) {
        Assert.isTrue(StrUtil.isNotBlank(idsStr), "删除的用户数据为空");
        List<Integer> ids = Arrays.stream(idsStr.split(",")).map(Integer::parseInt).toList();
        List<Integer> goodsIds = new ArrayList<>();
        for (Integer id : ids) {
            User user = lambdaQuery().eq(User::getId, id).one();
            if (user != null) {
                if (user.getType() == 1)
                    goodsIds.addAll(goodsService.lambdaQuery().eq(Goods::getOwnerId, id).list().stream().map(Goods::getId).toList());
            }
        }
        if (removeByIds(ids)) {
            if (goodsIds.isEmpty()) {
                if (userHistoryService.setSleepByIds(ids))
                    return R.success(true);
                else
                    return R.error("用户删除成功，但用户历史记录更新失败");
            } else if (goodsService.removeByIds(goodsIds)) {
                if (userHistoryService.setSleepByIds(ids))
                    if (goodsHistoryService.setSleepByIds(goodsIds))
                        return R.success(true);
                    else
                        return R.error("用户删除成功，但货品历史记录更新失败");
                else
                    return R.error("用户删除成功，但用户历史记录更新失败");
            } else
                return R.error("删除失败（企业关联货品删除失败）");
        } else {
            return R.error("删除失败（用户删除失败）");
        }
    }

    /**
     * 根据用户Id更新用户密码
     *
     * @param id
     * @return R<Boolean>
     */
    @Override
    public R<Boolean> updatePassword(Integer id, String password) {
        User user = lambdaQuery().eq(User::getId, id).one();
        if (user == null) {
            return R.error("用户不存在");
        }
        boolean success = lambdaUpdate().eq(User::getId, id).set(User::getPassword, password).update();
        if (success) {
            return R.success(true);
        } else {
            return R.error("用户" + user.getUsername() + "修改密码失败");
        }
    }

    @Override
    public R<Boolean> updateUser(User user) {
        user.setPassword(null);
        User check = lambdaQuery().eq(User::getUsername, user.getUsername()).one();
        if (check != null && !check.getId().equals(user.getId())) {
            return R.error("用户名已存在");
        }
        UserHistory userHistory = userHistoryService.lambdaQuery().eq(UserHistory::getId, user.getId()).one();
        if (userHistory == null)
            return R.error("用户历史记录不存在，更新失败");
        if (updateById(user)) {
            userHistory.setUsername(user.getUsername());
            userHistory.setType(user.getType());
            if (userHistoryService.updateById(userHistory))
                return R.success(true);
            else
                return R.error("用户信息更新成功，但用户历史记录更新失败");
        } else {
            return R.error("用户信息更新失败");
        }
    }

    /**
     * 管理员后台新增用户
     *
     * @param username
     * @param type
     * @param telephone
     * @param email
     * @param isEnabled
     * @return R<User>
     */
    @Override
    public R<User> AdminAddUser(String username, Integer type, String telephone, String email, boolean isEnabled) {
        if (StrUtil.isBlank(username)) {
            return R.error("未输入用户名");
        }
        if (type == null) {
            return R.error("未选择用户类型");
        }
        if (type < 1 || type > 2) {
            return R.error("新增用户账号类型错误");
        }
        if (lambdaQuery().eq(User::getUsername, username).count() != 0) {
            return R.error("用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword("123456");
        user.setType(type);
        user.setTelephone(telephone);
        user.setEmail(email);
        user.setIsEnabled(isEnabled);
        if (user.getIsEnabled() == null)
            user.setIsEnabled(true);
        if (save(user)) {
            User userLog = lambdaQuery().eq(User::getUsername, user.getUsername()).one();
            if (userLog == null)
                return R.error("新增用户失败");

            UserHistory userHistory = new UserHistory();
            userHistory.setId(userLog.getId());
            userHistory.setUsername(userLog.getUsername());
            userHistory.setType(userLog.getType());
            if (userHistoryService.save(userHistory))
                return R.success(user);
            else
                return R.error("用户创建成功但用户历史记录创建失败，详情查看数据库。");
        } else
            return R.error("新增用户失败");
    }

    /**
     * 用户注册账号
     *
     * @param username
     * @param password
     * @param type
     * @param telephone
     * @param email
     * @return R<User>
     */
    @Override
    public R<User> registerToSystem(String username, String password, String telephone, String email, Integer type) {
        if (StrUtil.isBlank(username)) {
            return R.error("没有输入用户名");
        }
        if (StrUtil.isBlank(password)) {
            return R.error("没有输入密码");
        }
        if (type < 1 || type > 2) {
            return R.error("注册账号类型错误");
        }
        if (lambdaQuery().eq(User::getUsername, username).count() != 0) {
            return R.error("用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setTelephone(telephone);
        user.setEmail(email);
        user.setType(type);
        user.setIsEnabled(true);
        if (save(user)) {
            User userLog = lambdaQuery().eq(User::getUsername, user.getUsername()).one();
            if (userLog == null)
                return R.error("新增用户失败");
            UserHistory userHistory = new UserHistory();
            userHistory.setId(userLog.getId());
            userHistory.setUsername(userLog.getUsername());
            userHistory.setType(userLog.getType());
            if (userHistoryService.save(userHistory)) {
                user.setPassword("");
                return R.success(user);
            } else
                return R.error("用户创建成功但用户历史记录创建失败，请联系管理员。");
        } else
            return R.error("注册失败");
    }

    /**
     * 使用md5单向加密密码
     */
    public static R<User> getUserR(User user) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert md5 != null;
        String ciphertext = new String(md5.digest(user.getPassword().getBytes()));
        user.setPassword(ciphertext);
        return R.success(user);
    }
}




