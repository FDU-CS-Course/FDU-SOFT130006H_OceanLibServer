package com.oriole.ocean.dto;

import lombok.Data;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Data Transfer Object for updating user information.
 * Supports partial updates for both user and user_extra fields.
 * Fields left null will not be updated.
 *
 * MAY NEED REVIEW: Adjust fields as needed for your business logic.
 */
@Data
public class UserInfoUpdateDTO implements Serializable {
    // User table fields
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;
    
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;
    
    @Pattern(regexp = "^[0-9+\\-\\s()]{10,20}$", message = "手机号格式不正确")
    private String phoneNum;
    
    @Size(max = 50, message = "真实姓名长度不能超过50个字符")
    private String realname;
    
    @Size(max = 255, message = "头像URL长度不能超过255个字符")
    private String avatar;
    
    @Min(value = 0, message = "等级不能小于0")
    @Max(value = 999999, message = "等级不能大于999999")
    private Integer levelGrade;
    
    @Min(value = 1, message = "认证ID必须大于0")
    private Integer certID;

    // UserExtra table fields
    @Size(max = 100, message = "学院名称长度不能超过100个字符")
    private String college;
    
    @Size(max = 100, message = "专业名称长度不能超过100个字符")
    private String major;
    
    @Past(message = "生日必须是过去的日期")
    private Date birthday;
    
    @Min(value = 0, message = "性别值不正确")
    @Max(value = 2, message = "性别值不正确")
    private Integer sex;
    
    @Size(max = 500, message = "个人签名长度不能超过500个字符")
    private String personalSignature;
} 