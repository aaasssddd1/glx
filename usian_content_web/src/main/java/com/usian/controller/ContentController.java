package com.usian.controller;

import com.usian.api.ContentFeign;
import com.usian.pojo.Content;
import com.usian.pojo.ContentCategory;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/backend/content")
public class ContentController {
    @Autowired
    private ContentFeign contentFeign;

    @RequestMapping("selectContentCategoryByParentId")
    public Result selectContentCategoryByParentId(@RequestParam(name = "id",defaultValue = "0") Long id){
        List<ContentCategory>data=contentFeign.selectContentCategoryByParentId(id);
        return Result.ok(data);
    }

    @RequestMapping("insertContentCategory")
    public Result insertContentCategory(@RequestParam("parentId")Long parentId,
                                        @RequestParam("name")String name){
        contentFeign.insertContentCategory(parentId,name);

        return Result.ok();
    }

    @RequestMapping("deleteContentCategoryById")
    public Result deleteContentCategoryById(@RequestParam("categoryId")Long categoryId){
        Boolean flag = contentFeign.deleteContentCategoryById(categoryId);
        if (flag){

            return Result.ok();
        }
        return Result.error("删除失败了！");
    }

    @RequestMapping("updateContentCategory")
    public Result updateContentCategory(@RequestParam("id")Long id,
                                        @RequestParam("name")String name){
        contentFeign.updateContentCategory(id,name);
        return Result.ok();
    }

    @RequestMapping("selectTbContentAllByCategoryId")
    public Result selectTbContentAllByCategoryId(@RequestParam("categoryId") Long categoryId){
        List<Content> data = contentFeign.selectTbContentAllByCategoryId(categoryId);
        return Result.ok(data);
    }

    @RequestMapping("/insertTbContent")
    public Result insertTbContent(Content content){
        contentFeign.insertTbContent(content);
        return Result.ok();
    }

    @RequestMapping("/deleteContentByIds")
    public Result deleteContentByIds(@RequestParam("ids")Long ids){
        contentFeign.deleteContentByIds(ids);
        return Result.ok();
    }
}
