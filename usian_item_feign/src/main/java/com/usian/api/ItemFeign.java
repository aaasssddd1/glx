package com.usian.api;

import com.usian.pojo.*;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import com.usian.vo.ItemCategoryAllVo;
import com.usian.vo.ItemResultVo;
import com.usian.vo.ItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: ItemFeign
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/6 9:41
 */
@FeignClient("usian-item-service")
public interface ItemFeign {

    @RequestMapping("/item/") // 跟下游路径保持一致
    public List<Item> queryAll();

    @GetMapping("/item/selectTbItemAllByPage")
    public PageResult<Item> selectTbItemAllByPage(@RequestParam(name = "page",defaultValue = "1") Integer page, @RequestParam(name="rows",defaultValue = "2") Integer rows);

    @RequestMapping("/itemCat/selectItemCategoryByParentId")
    List<ItemCat> selectItemCategoryByParentId(@RequestParam(name = "id",defaultValue = "0") Long id);

    @RequestMapping("/itemParam/selectItemParamByItemCatId/{itemCatId}")
    public ItemParam selectItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId);

    @RequestMapping("/item/insertTbItem")
    void insertTbItem(@RequestBody ItemVo itemVo);

    @RequestMapping("/item/preUpdateItem")
    ItemResultVo preUpdateItem(@RequestParam("itemId") Long itemId);

    @RequestMapping("/item/updateTbItem")
    void updateTbItem(@RequestBody ItemVo itemVo);

    @RequestMapping("/item/deleteItemById")
    void deleteItemById(@RequestParam(name = "itemId")Long itemId);

    @RequestMapping("/itemParam/selectItemParamAll")
    PageResult selectItemParamAll(@RequestParam(name = "page",defaultValue = "1") Integer page,@RequestParam(name = "rows",defaultValue = "10") Integer rows);

    @RequestMapping("/itemParam/insertItemParam")
    void insertItemParam(@RequestParam long itemCatId,@RequestParam String paramData);

    @RequestMapping("/itemParam/deleteItemParamById")
    void deleteItemParamById(@RequestParam long id);

    @RequestMapping("/itemCat/selectItemCategoryAll")
    ItemCategoryAllVo selectItemCategoryAll();

    @RequestMapping("/item/selectItemInfo")
    Item selectItemInfo(@RequestParam("itemId")Long itemId);

    @RequestMapping("/item/selectItemDescByItemId")
    ItemDesc selectItemDescByItemId(@RequestParam("itemId")Long itemId);

    @RequestMapping("/item/selectTbItemParamItemByItemId")
    ItemParamItem selectTbItemParamItemByItemId(@RequestParam("itemId")Long itemId);


}
