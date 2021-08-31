package com.usian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.CartFeign;
import com.usian.dto.OrderDTO;
import com.usian.mapper.OrderItemMapper;
import com.usian.mapper.OrderShippingMapper;
import com.usian.pojo.Order;
import com.usian.mapper.OrderMapper;
import com.usian.pojo.OrderItem;
import com.usian.pojo.OrderShipping;
import com.usian.service.OrderService;
import com.usian.vo.CartOrOrderItem;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Autowired
    private CartFeign cartFeign;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderShippingMapper orderShippingMapper;


    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public List<CartOrOrderItem> queryOrderItems(Long userId, Long[] ids) {// 2,3
        //查询 订单项的集合
        //1. 获取当前登录人 购物车中所有的数据
        Collection<CartOrOrderItem> cartItems = cartFeign.showCart(userId);// A B C
        //2. 筛选出，购买的订单项的数据
        List<CartOrOrderItem> orderItems = new ArrayList<>();//订单项集合
        for (Long id: ids){// 2
            for (CartOrOrderItem item : cartItems){
                if(item.getId() .equals( id)){
                    orderItems.add(item);
                }
            }
        }
        return orderItems;
    }

    /**
     * 新增订单业务层接口
     * @param orderDTO
     * @return
     */
    @Override
    @Transactional
    public String insertOrder(OrderDTO orderDTO) {
        // 生成订单相关的记录
        // part1：  新增到order
        // 生成一个唯一的订单号
        String orderId = UUID.randomUUID().toString()+new Random().nextInt(100);
        Date now = new Date();
        Order order = orderDTO.getOrder();
        order.setOrderId(orderId);
        order.setStatus(1);
        order.setCreateTime(now);
        this.save(order);


        HashMap<String, Integer> itemNums = new HashMap<>();
        //part2: 新增到 order_item
        List<OrderItem> orderItems = orderDTO.getOrderItems();
        for (OrderItem orderItem : orderItems){// A 1  B 2
            String orderItemID = new Date().getTime() + new Random().nextInt(100)+"";
            orderItem.setId(orderItemID);
            orderItem.setOrderId(orderId);
            orderItemMapper.insert(orderItem);
            itemNums.put(orderItem.getItemId(),orderItem.getNum());
        }
        //part3: 新增到 order_shipping
        OrderShipping orderShipping = orderDTO.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(now);
        orderShippingMapper.insert(orderShipping);


        // 订单触发其他的关联操作
        // 修改库存    update tb_item  set  num = num - ??? where item_id = ???   item_service  Map 集合
        // order service 调用你的 item service   openfeign  http原生客户端也可以    mq---RabbitMQ
        amqpTemplate.convertAndSend("item_kucun_exchang","update.num",itemNums);


        // 修改购物车的数据  扩展
        // xxx

        return orderId;
    }
}
