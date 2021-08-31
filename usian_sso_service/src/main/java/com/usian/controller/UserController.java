package com.usian.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usian.pojo.User;
import com.usian.service.UserService;
import com.usian.utils.MD5Utils;
import com.usian.utils.RedisClient;
import com.usian.vo.LoginUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("user/")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisClient redisClient;

    //注册验证接口
    @RequestMapping("checkUserInfo/{checkValue}/{checkFlag}")
    public Boolean checkUserInfo(@PathVariable("checkValue") String checkValue,
                                 @PathVariable("checkFlag") Integer checkFlag){
        //1.判断校验的类型
        if (checkFlag == 1){
            //2.如果是用户名 select * from user where username = ?zhangsan?
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("username",checkValue);

            List<User> list = userService.list(wrapper);
            if (list.size()>=1){
                return false;//不能用
            }
        }else {//如果是手机号 select * from user where phone = ?12344xxx?
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("phone",checkValue);

            List<User> list = userService.list(wrapper);
            if (list.size()>=1){
                return false;//不能用
            }
        }
        return true;
    }

    //用户注册接口
    @RequestMapping("userRegister")
    public Boolean userRegister(@RequestBody User user){
        Date date = new Date();
        user.setCreated(date);
        user.setUpdated(date);

        //对前台传递的密码加密 md5utils
        user.setPassword(MD5Utils.digest(user.getPassword()));
        //添加（注册用户）
        boolean save = userService.save(user);
        return save;
    }

    //登录接口
    @RequestMapping("userLogin")
    public LoginUserVo userLogin(@RequestParam("username") String username,
                                 @RequestParam("password")String password){
        //1.登录校验  select * from user where username = ?? and password = 加密（???）
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        wrapper.eq("password",MD5Utils.digest(password));
        List<User> list = userService.list(wrapper);

        if (list.size()>=1){
            //2.成功,生成一个token，组装前台需要的数据返回
            String token = UUID.randomUUID().toString();
            LoginUserVo loginUserVO = new LoginUserVo();
            loginUserVO.setUsername(username);
            loginUserVO.setUserid(list.get(0).getId()+"");
            loginUserVO.setToken(token);

            //3.将用户信息与token存储到redis ？？？？？？
            redisClient.set(token,list.get(0)); //session
            return loginUserVO;
        }
        //失败 ，返回 null结果
        return null;
    }

    @RequestMapping("getUserByToken/{token}")
    public User getUserByToken(@PathVariable("token")String token){
        User user = (User) redisClient.get(token);
        return user;
    }

    @RequestMapping("/logOut")
    public Boolean logOut(@RequestParam("token") String token){
        return redisClient.del(token);
    }
}
