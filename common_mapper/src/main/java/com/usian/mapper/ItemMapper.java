package com.usian.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.usian.pojo.Item;
import com.usian.vo.ESItemVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author 庞堤文
 * @since 2021-08-06
 */
public interface ItemMapper extends BaseMapper<Item> {

    //public List<ESItemVo> queryAllFromMysql
    //自动封装limit语句
    public <E extends IPage<ESItemVo>> E queryAllFromMysql(E page);

    //修改库存量的接口
    public int updateNum(@Param("id") String id, @Param("num") Integer num);
}
