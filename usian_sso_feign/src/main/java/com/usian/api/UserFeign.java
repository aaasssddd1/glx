package com.usian.api;

import com.usian.pojo.User;
import com.usian.vo.LoginUserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("usian-sso-service")
public interface UserFeign {

    //注册验证接口
    @RequestMapping("user/checkUserInfo/{checkValue}/{checkFlag}")
    public Boolean checkUserInfo(@PathVariable("checkValue") String checkValue,
                                @PathVariable("checkFlag") Integer checkFlag);

    //用户注册接口
    @RequestMapping("user/userRegister")
    public Boolean userRegister(@RequestBody User user);

    //登录接口
    @RequestMapping("user/userLogin")
    public LoginUserVo userLogin(@RequestParam("username") String username,
                                 @RequestParam("password")String password);

    @RequestMapping("user/getUserByToken/{token}")
    public User getUserByToken(@PathVariable("token")String token);

    //退出登录接口
    @RequestMapping("/user/logOut")
    public Boolean logOut(@RequestParam("token") String token);
}
