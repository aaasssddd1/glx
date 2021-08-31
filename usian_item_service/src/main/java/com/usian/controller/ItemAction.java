package com.usian.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usian.pojo.*;
import com.usian.service.*;
import com.usian.utils.IDUtils;
import com.usian.utils.PageResult;
import com.usian.utils.RedisClient;
import com.usian.vo.ItemResultVo;
import com.usian.vo.ItemVo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.beans.Transient;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/item")
public class ItemAction {
    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemDescService itemDescService;

    @Autowired
    private ItemParamItemService itemParamItemService;

    @Autowired
    private ItemCatService itemCatService;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RedisClient redisClient;

    // 商品详情页 规格参数
    @RequestMapping("/selectTbItemParamItemByItemId")
    public ItemParamItem selectTbItemParamItemByItemId(@RequestParam("itemId")Long itemId){
        //先查询缓存
        ItemParamItem itemParamItem = (ItemParamItem) redisClient.hget("ITEM_DETALL_ITEMARAMITEM",itemId+"");

        if(itemParamItem!=null){
            return itemParamItem;
        }

        //没有，查询数据库，并存入缓存
        if(redisClient.setNx("ITEM_DETALL_ITEMPARAMITEM_LOCK","",5, TimeUnit.SECONDS)){
            try {
                QueryWrapper<ItemParamItem> condition = new QueryWrapper<>();
                condition.eq("item_id",itemId);
                itemParamItem = itemParamItemService.getOne(condition);

                if (itemParamItem==null){//解决缓存穿透
                    redisClient.hset("ITEM_DETALL_ITEMARAMITEM",itemId+"",new Item());
                    redisClient.expire("ITEM_DETALL_ITEMARAMITEM",30);
                }else {
                    redisClient.hset("ITEM_DETALL_ITEMARAMITEM",itemId+"",itemParamItem);
                    redisClient.expire("ITEM_DETALL_ITEMARAMITEM",24*60*60);
                }
            } catch (Exception e) {
            } finally {
                redisClient.del("ITEM_DETALL_ITEMPARAMITEM_LOCK");
            }
        }else {
            selectTbItemParamItemByItemId(itemId);
        }
        return itemParamItem;
    }

    // 商品详情页 商品介绍
    @RequestMapping("/selectItemDescByItemId")
    public ItemDesc selectItemDescByItemId(@RequestParam("itemId")Long itemId){
        //先查询缓存
        ItemDesc itemDesc = (ItemDesc) redisClient.hget("ITEM_DETALL_ITEMDESC",itemId+"");

        if(itemDesc!=null){
            return itemDesc;
        }

        //没有，查询数据库，并存入缓存
        if(redisClient.setNx("ITEM_DETALL_ITEMDESC_LOCK","",5, TimeUnit.SECONDS)){
            try {
                QueryWrapper<ItemDesc> condition = new QueryWrapper<>();
                condition.eq("item_id",itemId);
                itemDesc = itemDescService.getOne(condition);
                if (itemDesc==null){//解决缓存穿透
                    redisClient.hset("ITEM_DETALL_ITEMDESC",itemId+"",new Item());
                    redisClient.expire("ITEM_DETALL_ITEMDESC",30);
                }else {
                    redisClient.hset("ITEM_DETALL_ITEMDESC",itemId+"",itemDesc);
                    redisClient.expire("ITEM_DETALL_ITEMDESC",24*60*60);
                }
            } catch (Exception e) {
            } finally {
                redisClient.del("ITEM_DETALL_ITEMDESC_LOCK");
            }
        }else {
            selectItemDescByItemId(itemId);
        }
        return itemDesc;
    }

    // 商品详情页 详情接口
    @RequestMapping("/selectItemInfo")
    public Item selectItemInfo(@RequestParam("itemId")Long itemId){
        //先查询缓存
        Item item = (Item) redisClient.hget("ITEM_DETALL_ITEM",itemId+"");

        if(item!=null){
            return item;
        }

        //没有，查询数据库，并存入缓存
        if(redisClient.setNx("ITEM_DETALL_ITEM_LOCK","",5, TimeUnit.SECONDS)){
            try {
                item = itemService.getById(itemId);
                if (item==null){//解决缓存穿透
                    redisClient.hset("ITEM_DETALL_ITEM",itemId+"",new Item());
                    redisClient.expire("ITEM_DETALL_ITEM",30);
                }else {
                    redisClient.hset("ITEM_DETALL_ITEM",itemId+"",item);
                    redisClient.expire("ITEM_DETALL_ITEM",24*60*60);
                }
            } catch (Exception e) {

            } finally {
                redisClient.del("ITEM_DETALL_ITEM_LOCK");
            }
        }else {
            selectItemInfo(itemId);
        }
        return item;
    }

    @RequestMapping("preUpdateItem")
    public ItemResultVo preUpdateItem(@RequestParam("itemId") Long itemId){
        //基本商品信息
        Item item = itemService.getById(itemId);

        //商品描述
        QueryWrapper<ItemDesc> condition1 = new QueryWrapper<>();
        condition1.eq("item_id",itemId);
        ItemDesc itemDesc = itemDescService.getOne(condition1);

        //商品规格参数值
        QueryWrapper<ItemParamItem> condition = new QueryWrapper<>();
        condition.eq("item_id",itemId);
        ItemParamItem itemParamItem = itemParamItemService.getOne(condition);

        //组合三个对象，变成前台需求的vo
        ItemResultVo itemResultVo = new ItemResultVo();
        itemResultVo.setItem(item);
        itemResultVo.setItemDesc(itemDesc.getItemDesc());
        itemResultVo.setItemParamItem(itemParamItem.getParamData());

        //根据cid查询对应类目名称
        ItemCat itemCat = itemCatService.getById(item.getCid());
        itemResultVo.setItemCat(itemCat.getName());//商品类目

        return itemResultVo;
    }

    //查询
    @GetMapping(value = "/")
    public List<Item> list() {
        return itemService.list();
    }

    //分页
    @GetMapping("/selectTbItemAllByPage")
    public PageResult<Item> selectTbItemAllByPage(@RequestParam(name = "page") Integer page, @RequestParam(name="rows") Integer rows){
        //1. 调用mybatis plus 自带的API  进行查询
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("created");

        Page<Item> pageData = itemService.page(new Page<Item>(page, rows),wrapper);


        //2. 将Mybatis plus 页结果类型  转换为  项目中自定义的页结果类型
        PageResult<Item> result = new PageResult<>();
        result.setPageIndex(page);
        result.setTotalPage(pageData.getPages());
        result.setResult(pageData.getRecords());

        return result;
    }

    //添加
    @RequestMapping("/insertTbItem")
    @Transactional
    public void insertTbItem(@RequestBody ItemVo itemVo){
        //基本信息存放到 tb_item ItemService
        long itemID = IDUtils.genItemId();
        Date date = new Date();
        itemVo.setCreated(date);
        itemVo.setUpdated(date);
        itemVo.setId(itemID);
        itemService.save(itemVo);

        //商品描述 存放到tb_item_desc
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(itemID);
        itemDesc.setItemDesc(itemVo.getDesc());
        itemDesc.setCreated(date);
        itemDesc.setUpdated(date);
        itemDescService.save(itemDesc);

        //商品规格参数值 存放到tb_param_item
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setItemId(itemID);
        itemParamItem.setParamData(itemVo.getItemParams());
        itemParamItem.setCreated(date);
        itemParamItem.setUpdated(date);
        itemParamItemService.save(itemParamItem);

        //查询出当前新增商品的分类名称
        ItemCat itemCat = itemCatService.getById(itemVo.getCid());
        itemVo.setCatName(itemCat.getName());

        //将数据保存到es数据库
        amqpTemplate.convertAndSend("usian_item","insert.item",itemVo);
    }

    //修改
    @RequestMapping("/updateTbItem")
    @Transactional
    public void updateTbItem(@RequestBody ItemVo itemVo){
        //基本信息存放到 tb_item ItemService
        Date date = new Date();
        itemVo.setUpdated(date);
        itemService.updateById(itemVo);

        //商品描述 存放到tb_item_desc
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemDesc(itemVo.getDesc());
        itemDesc.setItemId(itemVo.getId());
        itemDesc.setCreated(itemVo.getCreated());
        itemDesc.setUpdated(date);
        QueryWrapper<ItemDesc> wrapper = new QueryWrapper<>();
        wrapper.eq("item_id",itemDesc.getItemId());
        itemDescService.update(itemDesc,wrapper);

        //商品规格参数值 存放到tb_param_item
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setParamData(itemVo.getItemParams());
        itemParamItem.setItemId(itemVo.getId());
        itemParamItem.setCreated(itemVo.getCreated());
        itemParamItem.setUpdated(date);
        QueryWrapper<ItemParamItem> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("item_id",itemParamItem.getItemId());
        itemParamItemService.update(itemParamItem,wrapper1);
    }

    /*
    //预更新商品（回显）
    @RequestMapping("/preUpdateItem")
    public ItemResultVo preUpdateItem(@RequestParam("itemId") Long itemId){
        //基本信息
        Item item = itemService.getById(itemId);

        //商品描述
        QueryWrapper<ItemDesc> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("item_id",itemId);
        ItemDesc itemDesc = itemDescService.getOne(wrapper1);

        //商品规格参数值
        QueryWrapper<ItemParamItem> wrapper = new QueryWrapper<>();
        wrapper.eq("item_id",itemId);
        ItemParamItem itemParamItem = itemParamItemService.getOne(wrapper);

        //组合三个对象，变成前台需求的id
        ItemResultVo itemResultVo = new ItemResultVo();
        itemResultVo.setItem(item);
        itemResultVo.setItemDesc(itemDesc.getItemDesc());
        itemResultVo.setItemParamItem(itemParamItem.getParamData());
        //根据cid查询对应类目名称
        ItemCat itemCat = itemCatService.getById(item.getCid());
        itemResultVo.setItemCat(itemCat.getName());//商品类目

        return itemResultVo;
    }
    */


    //删除
    @GetMapping("/deleteItemById")
    public void deleteItemById(@RequestParam(name = "itemId")Long itemId){
        itemService.removeById(itemId);

        QueryWrapper<ItemDesc> wrapper = new QueryWrapper<>();
        wrapper.eq("item_id",itemId);
        itemDescService.remove(wrapper);

        QueryWrapper<ItemParamItem> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("item_id",itemId);
        itemParamItemService.remove(wrapper1);
    }
}
