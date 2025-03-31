package com.oriole.ocean.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriole.ocean.common.po.mysql.FileTagEntity;
import com.oriole.ocean.dao.TagDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DocTagServiceImpl extends ServiceImpl<TagDao, FileTagEntity> {

}
