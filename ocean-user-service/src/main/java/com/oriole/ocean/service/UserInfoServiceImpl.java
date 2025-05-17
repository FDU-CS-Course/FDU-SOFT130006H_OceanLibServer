package com.oriole.ocean.service;

import com.oriole.ocean.common.enumerate.UserInfoLevel;
import com.oriole.ocean.common.po.mysql.UserEntity;
import com.oriole.ocean.common.vo.BusinessException;
import com.oriole.ocean.common.vo.MsgEntity;
import com.oriole.ocean.service.base.UserBaseInfoServiceImpl;
import com.oriole.ocean.service.base.UserCertificationServiceImpl;
import com.oriole.ocean.service.base.UserExtraInfoServiceImpl;
import com.oriole.ocean.service.base.UserWalletServiceImpl;
import com.oriole.ocean.dto.UserInfoUpdateDTO;
import com.oriole.ocean.common.po.mysql.UserExtraEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class UserInfoServiceImpl {
    @Autowired
    UserBaseInfoServiceImpl userService;
    @Autowired
    UserExtraInfoServiceImpl userExtraService;
    @Autowired
    UserCertificationServiceImpl userCertificationService;
    @Autowired
    UserWalletServiceImpl walletService;

    public UserEntity getUserInfo(String username, UserInfoLevel userInfoLevel) {
        UserEntity userEntity = userService.getById(username);
        userEntity.setPassword(null);

        //加载认证，如果有
        Integer certID = userEntity.getCertID();
        if(certID!=null) {
            userEntity.setUserCertificationEntity(userCertificationService.getById(certID));
        }

        switch (userInfoLevel){
            case LIMITED:
                userEntity.setEmail(null);
                userEntity.setPhoneNum(null);
                userEntity.setStudentID(null);
                userEntity.setUserExtraEntity(userExtraService.getById(username));
                return userEntity;
            case ALL:
                userEntity.setWallet(walletService.getById(username));
                userEntity.setUserExtraEntity(userExtraService.getById(username));
                return userEntity;
            case BASE:
                userEntity.setWallet(walletService.getById(username));
                return userEntity;
            default:
                throw new RuntimeException("not support this userInfo level");
        }
    }

    /**
     * Update user and user_extra info for the given username. Only non-null fields in updateDTO will be updated.
     * @param username Username to update
     * @param updateDTO DTO containing fields to update
     * @throws BusinessException if user does not exist or update fails
     */
    public void updateUserInfo(String username, UserInfoUpdateDTO updateDTO) {
        UserEntity user = userService.getById(username);
        if (user == null) {
            throw new BusinessException("-2", "User not found");
        }
        boolean userChanged = false;
        // Update user fields
        if (updateDTO.getNickname() != null) { user.setNickname(updateDTO.getNickname()); userChanged = true; }
        if (updateDTO.getEmail() != null) { user.setEmail(updateDTO.getEmail()); userChanged = true; }
        if (updateDTO.getPhoneNum() != null) { user.setPhoneNum(updateDTO.getPhoneNum()); userChanged = true; }
        if (updateDTO.getRealname() != null) { user.setRealname(updateDTO.getRealname()); userChanged = true; }
        if (updateDTO.getAvatar() != null) { user.setAvatar(updateDTO.getAvatar()); userChanged = true; }
        if (updateDTO.getLevelGrade() != null) { user.setLevelGrade(updateDTO.getLevelGrade()); userChanged = true; }
        if (updateDTO.getCertID() != null) { user.setCertID(updateDTO.getCertID()); userChanged = true; }
        if (userChanged) {
            if (!userService.updateById(user)) {
                throw new BusinessException("-3", "Failed to update user base info");
            }
        }
        // Update user_extra fields
        UserExtraEntity extra = userExtraService.getById(username);
        boolean extraChanged = false;
        if (extra == null) {
            extra = new UserExtraEntity();
            extra.setUsername(username);
        }
        if (updateDTO.getCollege() != null) { extra.setCollege(updateDTO.getCollege()); extraChanged = true; }
        if (updateDTO.getMajor() != null) { extra.setMajor(updateDTO.getMajor()); extraChanged = true; }
        if (updateDTO.getBirthday() != null) { extra.setBirthday(updateDTO.getBirthday()); extraChanged = true; }
        if (updateDTO.getSex() != null) { extra.setSex(updateDTO.getSex()); extraChanged = true; }
        if (updateDTO.getPersonalSignature() != null) { extra.setPersonalSignature(updateDTO.getPersonalSignature()); extraChanged = true; }
        if (extraChanged) {
            if (!userExtraService.saveOrUpdate(extra)) {
                throw new BusinessException("-4", "Failed to update user extra info");
            }
        }
    }
}
