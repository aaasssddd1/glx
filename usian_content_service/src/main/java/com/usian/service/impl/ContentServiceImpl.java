package com.usian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.pojo.Content;
import com.usian.mapper.ContentMapper;
import com.usian.service.ContentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2021-08-09
 */
@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, Content> implements ContentService {

}
