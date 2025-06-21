package com.oriole.ocean.controller;

import com.oriole.ocean.common.auth.AuthUser;
import com.oriole.ocean.common.enumerate.UserInfoLevel;
import com.oriole.ocean.common.po.mysql.UserEntity;
import com.oriole.ocean.common.vo.AuthUserEntity;
import com.oriole.ocean.common.vo.BusinessException;
import com.oriole.ocean.common.vo.MsgEntity;
import com.oriole.ocean.dto.UserInfoUpdateDTO;
import com.oriole.ocean.service.UserInfoServiceImpl;
import com.oriole.ocean.service.base.UserBaseInfoServiceImpl;
import com.oriole.ocean.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/userInfoService")
public class UserInfoController {

    @Autowired
    UserBaseInfoServiceImpl userBaseInfoService;
    @Autowired
    UserInfoServiceImpl userInfoService;

    @RequestMapping(value = "/checkSameUsername",method = RequestMethod.GET)
    public MsgEntity<String> checkSameUsername(@RequestParam String username) {
        UserEntity userEntity = userBaseInfoService.getById(username);
        if (userEntity == null) {
            return new MsgEntity<>("SUCCESS", "1", "没有检测到重复用户名");
        } else {
            throw new BusinessException("-2", "用户名不得重复");
        }
    }
    
    @RequestMapping(value = "/getUserLimitedInfo",method = RequestMethod.GET)
    public MsgEntity<UserEntity> getUserLimitedInfo(@RequestParam String username) {
        return new MsgEntity<>("SUCCESS", "1", userInfoService.getUserInfo(username, UserInfoLevel.LIMITED));
    }

    @RequestMapping(value = "/getUserBaseInfo",method = RequestMethod.GET)
    public MsgEntity<UserEntity> getUserBaseInfo(@AuthUser AuthUserEntity authUser) {
        return new MsgEntity<>("SUCCESS", "1",
                userInfoService.getUserInfo(authUser.getUsername(), UserInfoLevel.BASE));
    }

    @RequestMapping(value = "/getUserAllInfo",method = RequestMethod.GET)
    public MsgEntity<UserEntity> getUserAllInfo(@AuthUser AuthUserEntity authUser) {
        return new MsgEntity<>("SUCCESS", "1",
                userInfoService.getUserInfo(authUser.getUsername(), UserInfoLevel.ALL));
    }

    @PostMapping("/updateUserInfo")
    /**
     * Update user personal information. Only non-null fields will be updated.
     * Includes comprehensive validation for security and data integrity.
     * @param authUser Authenticated user (from token)
     * @param updateDTO Fields to update (validated)
     * @return Success message or error
     */
    public MsgEntity<String> updateUserInfo(@AuthUser AuthUserEntity authUser, @Valid @RequestBody UserInfoUpdateDTO updateDTO) {
        try {
            // Additional security validation beyond annotation validation
            validateSecurityConstraints(updateDTO);
            
            userInfoService.updateUserInfo(authUser.getUsername(), updateDTO);
            return new MsgEntity<>("SUCCESS", "1", "User info updated successfully");
        } catch (Exception e) {
            log.error("Failed to update user info for {}: {}", authUser.getUsername(), e.getMessage(), e);
            throw new BusinessException("-1", "Failed to update user info: " + e.getMessage());
        }
    }

    /**
     * Validates security constraints for all string fields in the DTO
     * Checks for HTML tags, SQL injection, and XSS attempts
     * 
     * @param updateDTO DTO to validate
     * @throws BusinessException if validation fails
     */
    private void validateSecurityConstraints(UserInfoUpdateDTO updateDTO) {
        // Validate all string fields for security threats
        if (!ValidationUtil.isSafeInput(updateDTO.getNickname())) {
            throw new BusinessException("-5", "昵称包含不允许的字符或标签");
        }
        if (!ValidationUtil.isSafeInput(updateDTO.getRealname())) {
            throw new BusinessException("-5", "真实姓名包含不允许的字符或标签");
        }
        if (!ValidationUtil.isSafeInput(updateDTO.getAvatar())) {
            throw new BusinessException("-5", "头像URL包含不允许的字符或标签");
        }
        if (!ValidationUtil.isSafeInput(updateDTO.getCollege())) {
            throw new BusinessException("-5", "学院名称包含不允许的字符或标签");
        }
        if (!ValidationUtil.isSafeInput(updateDTO.getMajor())) {
            throw new BusinessException("-5", "专业名称包含不允许的字符或标签");
        }
        if (!ValidationUtil.isSafeInput(updateDTO.getPersonalSignature())) {
            throw new BusinessException("-5", "个人签名包含不允许的字符或标签");
        }
        
        // Additional email validation (beyond annotation)
        if (!ValidationUtil.isValidEmail(updateDTO.getEmail())) {
            throw new BusinessException("-6", "邮箱格式不正确");
        }
        
        // Additional phone validation (beyond annotation) 
        if (!ValidationUtil.isValidPhoneNumber(updateDTO.getPhoneNum())) {
            throw new BusinessException("-7", "手机号格式不正确");
        }
    }
}
