package com.oriole.ocean.dto;

import lombok.Data;
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
    private String nickname;
    private String email;
    private String phoneNum;
    private String realname;
    private String avatar;
    private Integer levelGrade;
    private Integer certID;

    // UserExtra table fields
    private String college;
    private String major;
    private Date birthday;
    private Integer sex;
    private String personalSignature;
} 