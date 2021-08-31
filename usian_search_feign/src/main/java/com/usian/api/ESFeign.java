package com.usian.api;

import com.usian.vo.ESItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("usian-search-service")
public interface ESFeign {

    @RequestMapping("/es/list")
    public List<ESItemVo> list(@RequestParam("q") String q);
}
