package com.oriole.ocean.validation;

import com.oriole.ocean.dto.UserInfoUpdateDTO;
import com.oriole.ocean.util.ValidationUtil;
import junit.framework.TestCase;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Set;

/**
 * Test class for validation functionality
 * Demonstrates various validation scenarios for UserInfoUpdateDTO
 */
public class ValidationTest extends TestCase {

    private Validator validator;

    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public void testValidUserInfoUpdateDTO() {
        UserInfoUpdateDTO dto = new UserInfoUpdateDTO();
        dto.setNickname("TestUser");
        dto.setEmail("test@example.com");
        dto.setPhoneNum("1234567890");
        dto.setRealname("Test User");
        dto.setLevelGrade(100);
        dto.setCollege("Computer Science");
        dto.setMajor("Software Engineering");
        dto.setBirthday(new Date(System.currentTimeMillis() - 86400000L)); // Yesterday
        dto.setSex(1);
        dto.setPersonalSignature("Hello World");

        Set<ConstraintViolation<UserInfoUpdateDTO>> violations = validator.validate(dto);
        assertTrue("Valid DTO should have no violations", violations.isEmpty());
    }

    public void testInvalidEmail() {
        UserInfoUpdateDTO dto = new UserInfoUpdateDTO();
        dto.setEmail("invalid-email");

        Set<ConstraintViolation<UserInfoUpdateDTO>> violations = validator.validate(dto);
        assertFalse("Invalid email should cause violations", violations.isEmpty());
        assertTrue("Should contain email validation error", 
                violations.stream().anyMatch(v -> v.getMessage().contains("邮箱格式不正确")));
    }

    public void testInvalidPhoneNumber() {
        UserInfoUpdateDTO dto = new UserInfoUpdateDTO();
        dto.setPhoneNum("abc");

        Set<ConstraintViolation<UserInfoUpdateDTO>> violations = validator.validate(dto);
        assertFalse("Invalid phone should cause violations", violations.isEmpty());
        assertTrue("Should contain phone validation error", 
                violations.stream().anyMatch(v -> v.getMessage().contains("手机号格式不正确")));
    }

    public void testMaxLengthViolation() {
        UserInfoUpdateDTO dto = new UserInfoUpdateDTO();
        // Create a string longer than 50 characters
        StringBuilder longNickname = new StringBuilder();
        for (int i = 0; i < 51; i++) {
            longNickname.append("a");
        }
        dto.setNickname(longNickname.toString());

        Set<ConstraintViolation<UserInfoUpdateDTO>> violations = validator.validate(dto);
        assertFalse("Long nickname should cause violations", violations.isEmpty());
        assertTrue("Should contain length validation error", 
                violations.stream().anyMatch(v -> v.getMessage().contains("昵称长度不能超过50个字符")));
    }

    public void testFutureDateValidation() {
        UserInfoUpdateDTO dto = new UserInfoUpdateDTO();
        dto.setBirthday(new Date(System.currentTimeMillis() + 86400000L)); // Tomorrow

        Set<ConstraintViolation<UserInfoUpdateDTO>> violations = validator.validate(dto);
        assertFalse("Future birthday should cause violations", violations.isEmpty());
        assertTrue("Should contain past date validation error", 
                violations.stream().anyMatch(v -> v.getMessage().contains("生日必须是过去的日期")));
    }

    public void testInvalidSexValue() {
        UserInfoUpdateDTO dto = new UserInfoUpdateDTO();
        dto.setSex(5); // Invalid value (should be 0-2)

        Set<ConstraintViolation<UserInfoUpdateDTO>> violations = validator.validate(dto);
        assertFalse("Invalid sex value should cause violations", violations.isEmpty());
        assertTrue("Should contain sex validation error", 
                violations.stream().anyMatch(v -> v.getMessage().contains("性别值不正确")));
    }

    // Security validation tests using ValidationUtil

    public void testSafeInputValidation() {
        assertTrue("Normal text should be safe", ValidationUtil.isSafeInput("Normal text"));
        assertTrue("Empty string should be safe", ValidationUtil.isSafeInput(""));
        assertTrue("Null should be safe", ValidationUtil.isSafeInput(null));
    }

    public void testHtmlTagDetection() {
        assertFalse("HTML tags should be detected", ValidationUtil.isSafeInput("<script>alert('xss')</script>"));
        assertFalse("HTML tags should be detected", ValidationUtil.isSafeInput("Hello <b>world</b>"));
        assertFalse("HTML tags should be detected", ValidationUtil.isSafeInput("<div>content</div>"));
    }

    public void testSqlInjectionDetection() {
        assertFalse("SQL injection should be detected", ValidationUtil.isSafeInput("'; DROP TABLE users; --"));
        assertFalse("SQL injection should be detected", ValidationUtil.isSafeInput("1' OR '1'='1"));
        assertFalse("SQL injection should be detected", ValidationUtil.isSafeInput("SELECT * FROM users"));
        assertFalse("SQL injection should be detected", ValidationUtil.isSafeInput("admin'--"));
    }

    public void testXssDetection() {
        assertFalse("XSS should be detected", ValidationUtil.isSafeInput("javascript:alert('xss')"));
        assertFalse("XSS should be detected", ValidationUtil.isSafeInput("onclick=alert('xss')"));
        assertFalse("XSS should be detected", ValidationUtil.isSafeInput("data:text/html,<script>alert('xss')</script>"));
        assertFalse("XSS should be detected", ValidationUtil.isSafeInput("vbscript:msgbox('xss')"));
    }

    public void testEmailValidation() {
        assertTrue("Valid email should pass", ValidationUtil.isValidEmail("test@example.com"));
        assertTrue("Valid email should pass", ValidationUtil.isValidEmail("user.name@domain.co.uk"));
        assertTrue("Empty email should pass", ValidationUtil.isValidEmail(""));
        assertTrue("Null email should pass", ValidationUtil.isValidEmail(null));
        
        assertFalse("Invalid email should fail", ValidationUtil.isValidEmail("invalid-email"));
        assertFalse("Invalid email should fail", ValidationUtil.isValidEmail("@domain.com"));
        assertFalse("Invalid email should fail", ValidationUtil.isValidEmail("user@"));
    }

    public void testPhoneValidation() {
        assertTrue("Valid phone should pass", ValidationUtil.isValidPhoneNumber("1234567890"));
        assertTrue("Valid phone should pass", ValidationUtil.isValidPhoneNumber("+1-234-567-8900"));
        assertTrue("Valid phone should pass", ValidationUtil.isValidPhoneNumber("(123) 456-7890"));
        assertTrue("Empty phone should pass", ValidationUtil.isValidPhoneNumber(""));
        assertTrue("Null phone should pass", ValidationUtil.isValidPhoneNumber(null));
        
        assertFalse("Invalid phone should fail", ValidationUtil.isValidPhoneNumber("abc"));
        assertFalse("Invalid phone should fail", ValidationUtil.isValidPhoneNumber("123"));
        // Create a very long phone number
        StringBuilder longPhone = new StringBuilder();
        for (int i = 0; i < 25; i++) {
            longPhone.append("1");
        }
        assertFalse("Invalid phone should fail", ValidationUtil.isValidPhoneNumber(longPhone.toString()));
    }
} 