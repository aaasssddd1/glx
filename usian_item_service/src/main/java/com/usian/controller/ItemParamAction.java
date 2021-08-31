package com.usian.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usian.pojo.ItemParam;
import com.usian.service.ItemParamService;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商品规则参数 前端控制器
 * </p>
 *
 * @author 
 * @since 2021-08-09
 */
@RestController
@RequestMapping("/itemParam")
public class ItemParamAction {


    @Autowired
    private ItemParamService itemParamService;


    @RequestMapping("selectItemParamByItemCatId/{itemCatId}")
    public ItemParam selectItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId){

        QueryWrapper<ItemParam> condition = new QueryWrapper<>();

        condition.eq("item_cat_id",itemCatId);

        List<ItemParam> list = itemParamService.list(condition);

        if(list!=null){
            return list.get(0);
        }
        return null;
    }

    @RequestMapping("/selectItemParamAll")
    public PageResult selectItemParamAll(@RequestParam(name = "page",defaultValue = "1")Integer page,
                                         @RequestParam(name = "rows",defaultValue = "10")Integer rows){
        Page<ItemParam> paramPage = itemParamService.page(new Page<>(page, rows));
        PageResult<ItemParam> result = new PageResult<>();
        result.setPageIndex(page);
        result.setTotalPage(paramPage.getPages());
        result.setResult(paramPage.getRecords());
        return result;
    }

    @RequestMapping("insertItemParam")
    public void insertItemParam(@RequestParam long itemCatId, @RequestParam String paramData){
        Date date = new Date();
        ItemParam itemParam = new ItemParam();
        itemParam.setItemCatId(itemCatId);
        itemParam.setParamData(paramData);
        itemParam.setUpdated(date);
        itemParam.setCreated(date);
        itemParamService.save(itemParam);
    }

    @RequestMapping("deleteItemParamById")
    public void deleteItemParamById(@RequestParam long id){
        itemParamService.removeById(id);
    }
}
