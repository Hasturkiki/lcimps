package scau.lcimp.lcimps.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import scau.lcimp.lcimps.common.R;
import scau.lcimp.lcimps.common.Token;
import scau.lcimp.lcimps.domain.User;
import scau.lcimp.lcimps.service.BusinessService;
import scau.lcimp.lcimps.service.GoodsService;
import scau.lcimp.lcimps.service.UserService;
import scau.lcimp.lcimps.utils.JWTUtils;
import scau.lcimp.lcimps.vo.BusinessVo;
import scau.lcimp.lcimps.vo.GoodsVo;

import javax.annotation.Resource;
import java.util.*;

@Api(tags = "首页")
@RestController
public class HomeController {

    @Resource
    private UserService userService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private BusinessService businessService;

    @PostMapping("/adminLogin")
    @ApiOperation("管理员登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query")
    })
    public R<Token> adminLoginToSystem(@RequestParam("username") String username, @RequestParam("password") String password) {
        R<User> user_R = userService.adminLoginToSystem(username, password);
        User user = user_R.getData();
        if (user != null) {
            Token token_R;
            try {
                Map<String, String> payload = new HashMap<>();
                payload.put("username", user.getUsername());
                payload.put("password", user.getPassword());

                //生成JWT的令牌
                String token;
                token = JWTUtils.getToken(payload);
                token_R = Token.builder().accessToken(token).build();
            } catch (Exception e) {
                return R.error("管理员登录失败");
            }
            return R.success(token_R);
        } else
            return R.error(user_R.getMsg().split(";")[0]);
    }

    @PostMapping("/register")
    @ApiOperation("用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query"),
            @ApiImplicitParam(name = "type", value = "用户类型", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "telephone", value = "联系方式", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "电子邮箱", paramType = "query")
    })
    public R<User> registerToSystem(@RequestParam("username") String username,
                                    @RequestParam("password") String password,
                                    @RequestParam("type") Integer type,
                                    @RequestParam("telephone") String telephone,
                                    @RequestParam("email") String email) {
        return userService.registerToSystem(username, password, telephone, email, type);
    }

    @PostMapping("/login")
    @ApiOperation("用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query")
    })
    public R<Token> UserLoginToSystem(@RequestParam("username") String username, @RequestParam("password") String password) {
        R<User> user_R = userService.loginToSystem(username, password);
        User user = user_R.getData();
        if (user != null) {
            Token token_R;
            try {
                Map<String, String> payload;
                payload = new HashMap<>();
                payload.put("username", user.getUsername());
                payload.put("password", user.getPassword());

                /* 生成JWT的令牌 */
                String token;
                token = JWTUtils.getToken(payload);
                token_R = Token.builder().accessToken(token).build();
            } catch (Exception e) {
                return R.error("用户登录失败");
            }
            return R.success(token_R);
        } else
            return R.error(user_R.getMsg().split(";")[0]);
    }

    @ResponseBody
    @GetMapping("/search")
    @ApiOperation("首页搜索")
    @ApiImplicitParam(name = "searchKey", value = "搜索关键词", required = true, paramType = "query", dataType = "String")
    public R<Object> show(String searchKey) {
        if (searchKey != null) {
            List<List> result = new ArrayList<>();
            List<User> userList = userService.searchByName(searchKey);
            List<GoodsVo> goodsList = goodsService.searchByName(searchKey);
            List<BusinessVo> businessList = businessService.searchByName(searchKey);

            result.add(userList);
            result.add(goodsList);
            result.add(businessList);

            if (result.stream().anyMatch(Objects::nonNull)) {
                System.out.println("search_result: \n" + result);
            } else
                System.out.println("search_result: null");
            return R.success(result);
        } else {
            System.out.println("search fail.");
            return R.error("search fail.");
        }
    }
}
