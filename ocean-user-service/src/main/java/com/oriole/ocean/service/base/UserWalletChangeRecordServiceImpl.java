package com.oriole.ocean.service.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriole.ocean.common.po.mysql.WalletChangeRecordEntity;
import com.oriole.ocean.dao.WalletChangeRecordDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserWalletChangeRecordServiceImpl extends ServiceImpl<WalletChangeRecordDao, WalletChangeRecordEntity> {

    public Page<WalletChangeRecordEntity> getWalletChangeRecord(String username, Integer pageNum, Integer pageSize) {
        QueryWrapper<WalletChangeRecordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);

        Page<WalletChangeRecordEntity> page = new Page<>(pageNum, pageSize);

        return baseMapper.selectPage(page, queryWrapper);
    }

}
