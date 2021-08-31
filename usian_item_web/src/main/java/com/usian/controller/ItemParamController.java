package com.usian.controller;

import com.usian.api.ItemFeign;
import com.usian.pojo.ItemParam;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backend/itemParam/")
public class ItemParamController {

    @Autowired
    private ItemFeign itemFeign;

    /**
     * 根据商品类目ID，查询对应的规格参数模板（某种商品类型特有的属性）
     * @param itemCatId
     * @return
     * 浏览器 --Resutl- WEB层 --有效数据-- service服务层
     */


    @RequestMapping("selectItemParamByItemCatId/{itemCatId}")
    public Result selectItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId){

        ItemParam itemParam= itemFeign.selectItemParamByItemCatId(itemCatId);

        return  Result.ok(itemParam);
    }

    @RequestMapping("/selectItemParamAll")
    public Result selectItemParamAll(@RequestParam(name = "page",defaultValue = "1")Integer page,
                                     @RequestParam(name = "rows",defaultValue = "10")Integer rows){
        try {
            PageResult pageResult = itemFeign.selectItemParamAll(page,rows);
            return Result.ok(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }


    @RequestMapping("insertItemParam")
    public Result insertItemParam(@RequestParam long itemCatId,@RequestParam String paramData){
        try {
            itemFeign.insertItemParam(itemCatId,paramData);
            return Result.ok("成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("失败");
        }
    }

    @RequestMapping("deleteItemParamById")
    public Result deleteItemParamById(@RequestParam long id){
        try {
            itemFeign.deleteItemParamById(id);
            return Result.ok("成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("失败");
        }
    }
}
