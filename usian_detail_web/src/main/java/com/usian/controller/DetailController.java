package com.usian.controller;

import com.usian.api.ItemFeign;
import com.usian.pojo.Item;
import com.usian.pojo.ItemDesc;
import com.usian.pojo.ItemParam;
import com.usian.pojo.ItemParamItem;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/frontend/detail")
public class DetailController {

    @Autowired
    private ItemFeign itemFeign;

    @RequestMapping("selectItemInfo")
    public Result selectItemInfo(@RequestParam("itemId")Long itemId){
        Item item = itemFeign.selectItemInfo(itemId);
        return Result.ok(item);
    }

    @RequestMapping("selectItemDescByItemId")
    public Result selectItemDescByItemId(@RequestParam("itemId")Long itemId){
        ItemDesc itemDesc = itemFeign.selectItemDescByItemId(itemId);
        return Result.ok(itemDesc);
    }

    @RequestMapping("selectTbItemParamItemByItemId")
    public Result selectTbItemParamItemByItemId(@RequestParam("itemId")Long itemId){
        ItemParamItem itemParamItem = itemFeign.selectTbItemParamItemByItemId(itemId);
        return Result.ok(itemParamItem);
    }
}
