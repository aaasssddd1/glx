package com.usian.controller;

import com.usian.api.ContentFeign;
import com.usian.utils.Result;
import com.usian.vo.ADVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/frontend/content")
public class ContentController {

    @Autowired
    private ContentFeign contentFeign;

    @RequestMapping("selectFrontendContentByAD")
    public Result selectFrontendContentByAD(){

       List<ADVO> data =  contentFeign.selectFrontendContentByAD();

       return Result.ok(data);
    }
}
