package com.usian.api;

import com.usian.pojo.Content;
import com.usian.pojo.ContentCategory;
import com.usian.vo.ADVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("usian-content-service")
public interface ContentFeign {

    @RequestMapping("/content/selectContentCategoryByParentId")
    List<ContentCategory> selectContentCategoryByParentId(@RequestParam(name = "id",defaultValue = "0") Long id);

    @RequestMapping("/content/insertContentCategory")
    void insertContentCategory(@RequestParam("parentId")Long parentId,@RequestParam("name")String name);

    @RequestMapping("/content/deleteContentCategoryById")
    Boolean deleteContentCategoryById(@RequestParam("categoryId")Long categoryId);

    @RequestMapping("/content/updateContentCategory")
    void updateContentCategory(@RequestParam("id")Long id,@RequestParam("name")String name);

    @RequestMapping("/content/selectTbContentAllByCategoryId")
    List<Content> selectTbContentAllByCategoryId(@RequestParam("categoryId") Long categoryId);

    @RequestMapping("/content/insertTbContent")
    void insertTbContent(@RequestBody Content content);

    @RequestMapping("/content/deleteContentByIds")
    void deleteContentByIds(@RequestParam("ids")Long ids);

    @RequestMapping("/content/selectFrontendContentByAD")
    List<ADVO> selectFrontendContentByAD();

}
