package com.oriole.ocean.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oriole.ocean.common.enumerate.MainType;
import com.oriole.ocean.common.enumerate.NotifyAction;
import com.oriole.ocean.common.po.mysql.NotifyEntity;

public interface NotifyDao extends BaseMapper<NotifyEntity> {
    void removeNotify(String targetId, MainType targetType, String commentId, NotifyAction action, String buildUsername);
}
