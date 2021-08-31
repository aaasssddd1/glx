package com.usian.task;

import com.usian.mapper.OrderItemMapper;
import com.usian.mapper.OrderMapper;
import com.usian.pojo.OrderItem;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @Title: OrderOverTimeTask
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/25 14:33
 */
@Component
public class OrderOverTimeTask {


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private OrderItemMapper orderItemMapper;
    /**
     * 干啥？ 关闭超时的订单
     * 啥时启动？ 项目启动时我就启动
     *  周期？1分钟     30分
     *
     *
     */
    @Scheduled(cron="0/1 * * * * ?")
    public void closeOverTimeOrder(){

        /*
           1. 获取哪些是超时的订单
           2. 将超时的订单关闭  update tb_order set status = 6 where order_id in(SELECT order_id FROM `tb_order` WHERE TIMESTAMPDIFF(MINUTE,create_time,NOW())>=30)


         */
        orderMapper.closeOverTimeOrders();

        //  3.  将库存在回退到商品里面  update tb_item set num = num - 退回的 where id = 超时订单管理的订单项中的商品的ID

        //根据关闭的订单，获取对应的订单项集合
        // SELECT * FROM tb_order_item WHERE order_id IN (SELECT order_id FROM `tb_order` WHERE TIMESTAMPDIFF(MINUTE,create_time,NOW())>=30)

        List<OrderItem> orderItems = orderItemMapper.queryOverTimeOrderItems();
        HashMap<String, Integer> itemNums = new HashMap<>();
        //part2: 新增到 order_item
        for (OrderItem orderItem : orderItems){// A 1  B 2
            itemNums.put(orderItem.getItemId(),-orderItem.getNum());
        }
        amqpTemplate.convertAndSend("item_kucun_exchang","update.num",itemNums);//



    }
}
