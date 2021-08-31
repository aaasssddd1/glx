package com.usian.controller;

import com.usian.api.ESFeign;
import com.usian.vo.ESItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/frontend/searchItem")
public class IndexESController {

    @Autowired
    private ESFeign esFeign;

    @RequestMapping("list")
    public List<ESItemVo> list(@RequestParam("q") String q){
        List<ESItemVo> data = esFeign.list(q);
        return data;
    }
}
