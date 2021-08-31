package com.usian.controller;

import com.usian.api.UserFeign;
import com.usian.pojo.User;
import com.usian.utils.Result;
import com.usian.vo.LoginUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("frontend/sso")
public class UserWebController {

    @Autowired
    private UserFeign userFeign;

    //注册验证接口
    @RequestMapping("checkUserInfo/{checkValue}/{checkFlag}")
    public Result checkUserInfo(@PathVariable("checkValue") String checkValue,
                                @PathVariable("checkFlag") Integer checkFlag){
        Boolean result = userFeign.checkUserInfo(checkValue,checkFlag);
        if (result){
            return Result.ok();
        }
        return Result.error("注册过了！！！");
    }

    //用户注册接口
    @RequestMapping("userRegister")
    public Result userRegister(User user){
        Boolean result = userFeign.userRegister(user);
        if (result){
            //注册成功
            return Result.ok();
        }
        return Result.error("注册失败！！！");
    }

    //登录接口
    @RequestMapping("userLogin")
    public Result userLogin(@RequestParam("username") String username,
                            @RequestParam("password")String password){
        LoginUserVo loginUserVo = userFeign.userLogin(username,password);
        if (loginUserVo==null){
            return Result.error("登录失败！！1");
        }
        return Result.ok(loginUserVo);
    }

    @RequestMapping("getUserByToken/{token}")
    public Result getUserByToken(@PathVariable("token")String token){
        User user = userFeign.getUserByToken(token);
        if(user == null){
            return Result.error("获取用户信息失败！");
        }
        return Result.ok();
    }

    //退出登录接口
    @RequestMapping("logOut")
    public Result logOut(@RequestParam("token") String token){
        Boolean logOut=userFeign.logOut(token);
        if(logOut){
            return Result.ok();
        }
        return Result.error("退出失败");
    }
}
