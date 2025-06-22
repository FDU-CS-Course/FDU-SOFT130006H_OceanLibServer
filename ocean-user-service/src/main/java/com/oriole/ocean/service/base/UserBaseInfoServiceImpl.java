package com.oriole.ocean.service.base;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriole.ocean.common.vo.BusinessException;
import com.oriole.ocean.common.vo.MsgEntity;
import com.oriole.ocean.dao.UserDao;
import com.oriole.ocean.common.po.mysql.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@Transactional
public class UserBaseInfoServiceImpl extends ServiceImpl<UserDao, UserEntity> {

    public UserEntity getUserInfoByUsernameAndPassword(String username, String password) {
        UserEntity userEntity = getById(username);
        if (userEntity == null || !userEntity.getPassword().equals(password)) {
            throw new BusinessException("-2", "用户名或密码错误");
        }
        return userLoginHandler(userEntity);
    }

    private UserEntity userLoginHandler(UserEntity userEntity) {
        if (userEntity.getIsValid().equals((byte) -1)) {
            throw new BusinessException("-3", "账号已锁定或被封禁，请联系管理员处理");
        }
        if (userEntity.getIsValid().equals((byte) 0)) {
            throw new BusinessException("-4", "账号需等待管理员审核");
        }
        return userEntity;
    }

    public MsgEntity<String> registerUser(String username, String password, String nickname, String phoneNum, String email) {
        // Check if username already exists
        UserEntity existingUser = getById(username);
        if (existingUser != null) {
            throw new BusinessException("-5", "Username already exists");
        }
        
        // Create new user
        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setNickname(nickname);
        newUser.setPhoneNum(phoneNum);
        newUser.setEmail(email);
        newUser.setIsValid((byte) 0); // Set to pending review
        newUser.setRegDate(new java.util.Date());
        newUser.setRole("USER");
        newUser.setLevelGrade(0);
        
        // Save user to database
        boolean saved = save(newUser);
        if (saved) {
            return new MsgEntity<>("SUCCESS", "1", "Registration successful, waiting for admin review");
        } else {
            throw new BusinessException("-6", "Registration failed, please try again later");
        }
    }
}
