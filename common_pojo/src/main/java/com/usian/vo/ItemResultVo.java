package com.usian.vo;

import com.usian.pojo.Item;

public class ItemResultVo {
    private String itemCat;//商品类目

    private Item item;//商品基本信息对象

    private String itemDesc;//商品描述

    private String itemParamItem;//规格参数值

    public String getItemCat() {
        return itemCat;
    }

    public void setItemCat(String itemCat) {
        this.itemCat = itemCat;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemParamItem() {
        return itemParamItem;
    }

    public void setItemParamItem(String itemParamItem) {
        this.itemParamItem = itemParamItem;
    }
}
