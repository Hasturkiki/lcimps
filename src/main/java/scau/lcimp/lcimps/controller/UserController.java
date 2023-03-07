package scau.lcimp.lcimps.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import scau.lcimp.lcimps.common.PageResult;
import scau.lcimp.lcimps.common.R;
import scau.lcimp.lcimps.domain.User;
import scau.lcimp.lcimps.domain.UserHistory;
import scau.lcimp.lcimps.service.UserService;
import scau.lcimp.lcimps.utils.JWTUtils;
import scau.lcimp.lcimps.vo.UserPageQuery;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "用户主页")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/me")
    @ApiOperation("获取已登录用户信息")
    public R<User> getUserLoginInfo(HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            System.out.println("cookies is null");
            return R.error("cookies is null");
        } else {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("lcimp-token"))
                    token = cookie.getValue();
            }
        }
        if (token == null) {
            System.out.println("token is null");
            return R.error("token is null");
        }
        DecodedJWT verify = JWTUtils.verify(token);
        return userService.getUserLoginInfo(verify);
    }

    @ApiImplicitParam(name = "queryParams", value = "基础分页请求对象", required = true)
    @ApiOperation("分页获取用户列表扩展：PageNum=0时代表获取所有用户")
    @GetMapping("/getUserListVoByPage")
    public PageResult<User> getUserListVoByPage(UserPageQuery queryParams) {
        if (queryParams.getPageNum() == 0)
            return userService.getAllUserList();
        else {
            return userService.getUserListByPage(queryParams);
        }
    }

    @ApiOperation("获取企业历史数据列表")
    @GetMapping("/getCompanyHistory")
    public PageResult<UserHistory> getCompanyHistory() {
        return userService.getCompanyHistory();
    }

    @ApiOperation("获取主播历史数据列表")
    @GetMapping("/getUpHistory")
    public PageResult<UserHistory> getUpHistory() {
        return userService.getUpHistory();
    }

    @GetMapping("/{id}")
    @ApiOperation("获取单个用户详情")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true)
    public R<User> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PutMapping("/updateUser")
    @ApiOperation("修改编辑用户信息")
    @ApiImplicitParam(name = "user", value = "用户数据信息的实体", required = true, paramType = "body")
    public R<Boolean> updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @PostMapping("/AdminAddUser")
    @ApiOperation("管理员后台新增用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query"),
            @ApiImplicitParam(name = "type", value = "用户类型", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "telephone", value = "联系方式", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "电子邮箱", paramType = "query"),
            @ApiImplicitParam(name = "isEnabled", value = "是否启用(false禁用,true启用)", paramType = "query")
    })
    public R<User> AdminAddUser(@RequestParam("username") String username,
                                @RequestParam("type") Integer type,
                                @RequestParam("telephone") String telephone,
                                @RequestParam("email") String email,
                                @RequestParam("isEnabled") boolean isEnabled) {
        return userService.AdminAddUser(username, type, telephone, email, isEnabled);
    }

    @PutMapping("/enable")
    @ApiOperation("通过用户id来启用用户")
    @ApiImplicitParam(name = "id", value = "ID", required = true, paramType = "query", dataType = "Integer")
    public R<Boolean> enableUserById(@RequestParam("id") Integer id) {
        return userService.enableUserById(id);
    }

    @PutMapping("/disable")
    @ApiOperation("通过用户Id来禁用用户")
    @ApiImplicitParam(name = "id", value = "ID", required = true, paramType = "query", dataType = "Integer")
    public R<Boolean> disableUserById(@RequestParam("id") Integer id) {
        return userService.disableUserById(id);
    }

    @PutMapping("/updatePassword")
    @ApiOperation("更新用户密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query")
    })
    public R<Boolean> updatePassword(@RequestParam("id") Integer id, @RequestParam("password") String password) {
        return userService.updatePassword(id, password);
    }

    @DeleteMapping("/{ids}")
    @ApiOperation("通过用户id来删除用户，多个用户ID以英文逗号(,)间隔")
    @ApiImplicitParam(name = "ids", value = "IDs", required = true)
    public R<Boolean> deleteUserByIds(@PathVariable String ids) {
        return userService.deleteUserByIds(ids);
    }

    @DeleteMapping("/logout")
    @ApiOperation("用户登出系统")
    public R<String> login() {
        return R.success("成功登出系统");
    }

    @ApiOperation("获取所有用户列表")
    @GetMapping
    public R<List<User>> getUserList() {
        return R.success(userService.list());
    }

    @ApiOperation("获取全部用户列表")
    @GetMapping("/getAll")
    public R<List<User>> getUserListByPage() {
        return userService.getUserList();
    }

    @GetMapping("/search")
    @ApiOperation("通过用户名搜索用户")
    @ApiImplicitParam(name = "name", value = "用户名", required = true, paramType = "query", dataType = "String")
    public R<List<User>> getSearchUserList(@RequestParam("name") String name) {
        return userService.getSearchUserList(name);
    }
}
