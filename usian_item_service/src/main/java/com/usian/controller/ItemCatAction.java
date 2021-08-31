package com.usian.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usian.pojo.ItemCat;
import com.usian.service.ItemCatService;
import com.usian.utils.RedisClient;
import com.usian.vo.ItemCategoryAllVo;
import com.usian.vo.ItemCategoryNodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品类目 前端控制器
 * </p>
 *
 * @author 
 * @since 2021-08-09
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatAction {


    @Autowired
    private ItemCatService itemCatService;

    @Autowired
    private RedisClient redisClient;

    @RequestMapping("/selectItemCategoryAll")
    public ItemCategoryAllVo selectItemCategoryAll(){
        //查询缓存中是否有数据
        ItemCategoryAllVo redisData  = (ItemCategoryAllVo) redisClient.get("USIAN_INDEX_CATEGORY_ALL");
        //如果有数据就直接返回
        if (redisData!=null){
            return redisData;
        }
        //1.从数据库中查询全部的类目，平铺的方式
//        List<ItemCat> itemCats = itemCatService.list();

        //2. 改变成树形
        //  parent_id = 0;  select * from tb_item_cat where parent_id = ?
        //2.1 先找到1级，根据1级2级    。。。。   。。。。

        //查询询全部的类目，树形

        List oneItemCat =  queryData(0L);

        ItemCategoryAllVo itemCategoryAllVo = new ItemCategoryAllVo();
        itemCategoryAllVo.setData(oneItemCat);

        redisClient.set("USIAN_INDEX_CATEGORY_ALL",itemCategoryAllVo);
        return itemCategoryAllVo;
    }

    /**
     * 根据父id查询对应的子节节点集合
     * @param parentId
     * @return
     */
    public List queryData(Long parentId){ // 0

        QueryWrapper<ItemCat> condition = new QueryWrapper<>();
        condition.eq("parent_id",parentId).eq("status","1");
        List<ItemCat> childrens = itemCatService.list(condition);//一级类目

        // ItemCat ----> ItemCategoryNodeVo/string
        List  result = new ArrayList();
        for (ItemCat itemCat:childrens){

            if(itemCat.getIsParent()){// 是父节点
                ItemCategoryNodeVo node = new ItemCategoryNodeVo();
                node.setN(itemCat.getName());
                node.setI(queryData(itemCat.getId()));//  select * from tb_item_cat where parent_id = 1
                result.add(node);

            }else{//不是父节点
                result.add(itemCat.getName());
            }
        }

        return result;

    }

    @RequestMapping("/selectItemCategoryByParentId")
    public List<ItemCat> selectItemCategoryByParentId(@RequestParam(name = "id") Long id) {
        QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("parent_id",id);
        return itemCatService.list(queryWrapper);
    }

    @GetMapping(value = "/")
    public ResponseEntity<Page<ItemCat>> list(@RequestParam(required = false) Integer current, @RequestParam(required = false) Integer pageSize) {
        if (current == null) {
            current = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        Page<ItemCat> aPage = itemCatService.page(new Page<>(current, pageSize));
        return new ResponseEntity<>(aPage, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ItemCat> getById(@PathVariable("id") String id) {
        return new ResponseEntity<>(itemCatService.getById(id), HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Object> create(@RequestBody ItemCat params) {
        itemCatService.save(params);
        return new ResponseEntity<>("created successfully", HttpStatus.OK);
    }

    @PostMapping(value = "/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
        itemCatService.removeById(id);
        return new ResponseEntity<>("deleted successfully", HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<Object> delete(@RequestBody ItemCat params) {
        itemCatService.updateById(params);
        return new ResponseEntity<>("updated successfully", HttpStatus.OK);
    }
}
