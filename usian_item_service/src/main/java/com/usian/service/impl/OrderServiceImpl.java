package com.usian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.pojo.Order;
import com.usian.mapper.OrderMapper;
import com.usian.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2021-08-09
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
