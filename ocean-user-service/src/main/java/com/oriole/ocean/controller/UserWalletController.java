package com.oriole.ocean.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oriole.ocean.common.auth.AuthUser;
import com.oriole.ocean.common.po.mysql.WalletChangeRecordEntity;
import com.oriole.ocean.common.vo.AuthUserEntity;
import com.oriole.ocean.common.vo.MsgEntity;
import com.oriole.ocean.service.base.UserWalletChangeRecordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userWalletService")
public class UserWalletController {

    @Autowired
    UserWalletChangeRecordServiceImpl walletChangeRecordService;

    @RequestMapping(value = "/getWalletChangeRecord",method = RequestMethod.GET)
    public MsgEntity<Page<WalletChangeRecordEntity>> getWalletChangeRecord(
            @AuthUser AuthUserEntity authUser,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {

        Page<WalletChangeRecordEntity> pageResult = walletChangeRecordService.getWalletChangeRecord(
                authUser.getUsername(),
                pageNum,
                pageSize
        );

        return new MsgEntity<>("SUCCESS", "1", pageResult);
    }
}
