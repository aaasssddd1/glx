package com.usian.controller;

import com.usian.api.ItemFeign;
import com.usian.pojo.ItemCat;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/backend/itemCategory")
public class ItemCatController {
    @Autowired
    private ItemFeign itemFeign;

    /**
     * 根据父节点查询子节点
     */

    @RequestMapping("/selectItemCategoryByParentId")
    public Result selectItemCategoryByParentId(@RequestParam(name = "id",defaultValue = "0")Long id){
        List<ItemCat> catList = itemFeign.selectItemCategoryByParentId(id);
        return Result.ok(catList);
    }
}
