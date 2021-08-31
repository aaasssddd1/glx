package com.usian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.pojo.User;
import com.usian.mapper.UserMapper;
import com.usian.service.UserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-08-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
