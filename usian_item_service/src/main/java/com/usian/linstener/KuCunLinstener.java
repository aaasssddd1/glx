package com.usian.linstener;

import com.rabbitmq.client.Channel;
import com.usian.mapper.ItemMapper;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @Title: KuCunLinstener
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/25 14:21
 */
@Component
public class KuCunLinstener {

    @Autowired
    private ItemMapper itemMapper;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value="item_num_queue",durable = "true"),
            exchange = @Exchange(value="item_kucun_exchang",type= ExchangeTypes.TOPIC),
            key= {"update.num"}
    ))
    public void listen(Map<String,Integer> msg, Channel channel, Message message){
        // 将库存更改到item表中
        Set<String> ids = msg.keySet();
       for (String id : ids){
           Integer num = msg.get(id);
           itemMapper.updateNum(id,num);
       }
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
