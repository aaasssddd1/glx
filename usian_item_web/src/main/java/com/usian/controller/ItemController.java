package com.usian.controller;

import com.usian.api.ItemFeign;
import com.usian.pojo.Item;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import com.usian.vo.ItemResultVo;
import com.usian.vo.ItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: ItemController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/6 9:37
 */
@RestController
@RequestMapping("/backend/item/")
public class ItemController {

    @Autowired
    private ItemFeign itemFeign;


    @RequestMapping("/deleteItemById")
    public Result deleteItemById(@RequestParam(name = "itemId")Long itemId){
        try {
            itemFeign.deleteItemById(itemId);
            return Result.ok("成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("失败");
        }
    }

    @RequestMapping("/updateTbItem")
    public Result updateTbItem(ItemVo itemVo){
        try {
            itemFeign.updateTbItem(itemVo);
        } catch (Exception e) {
            Result.error(e.getMessage());
        }
        return Result.ok();
    }

    @RequestMapping("/preUpdateItem")
    public Result preUpdateItem(@RequestParam("itemId") Long itemId){
       ItemResultVo itemResultVo = itemFeign.preUpdateItem(itemId);

       return Result.ok(itemResultVo);
    }

    @RequestMapping("/insertTbItem")
    public Result insertTbItem(ItemVo itemVo){
        try {
            itemFeign.insertTbItem(itemVo);
        } catch (Exception e) {
           Result.error(e.getMessage());
        }
        return Result.ok();
    }

    @RequestMapping("/queryAll")
    public List queryAll(){
      List<Item> data =  itemFeign.queryAll();
      return data;

    }

    @GetMapping("/selectTbItemAllByPage")
    public Result selectTbItemAllByPage(@RequestParam(name = "page",defaultValue = "1") Integer page, @RequestParam(name="rows",defaultValue = "2") Integer rows){

        PageResult pageResult = null;
        try {
            pageResult = itemFeign.selectTbItemAllByPage(page,rows);
            return Result.ok(pageResult);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
