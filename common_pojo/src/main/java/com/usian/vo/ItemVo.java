package com.usian.vo;

import com.usian.pojo.Item;

public class ItemVo extends Item {
    private String desc;
    private String itemParams;
    private String catName;//分类名称

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getItemParams() {
        return itemParams;
    }

    public void setItemParams(String itemParams) {
        this.itemParams = itemParams;
    }
}
