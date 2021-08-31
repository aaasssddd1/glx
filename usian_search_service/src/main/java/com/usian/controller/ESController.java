package com.usian.controller;

import com.usian.service.ESService;
import com.usian.vo.ESItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ESController {
    @Autowired
    private ESService esService;


    @RequestMapping("importAll")
    public void importAll(){
        esService.importAll();
    }

    @RequestMapping("/es/list")
    public List<ESItemVo> list(@RequestParam("q") String q){
        return esService.list(q);
    }
}
