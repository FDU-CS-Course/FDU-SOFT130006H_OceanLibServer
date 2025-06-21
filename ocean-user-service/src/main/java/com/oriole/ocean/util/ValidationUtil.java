package com.oriole.ocean.util;

import java.util.regex.Pattern;

/**
 * Utility class for data validation including security checks
 * 
 * @author Ocean Team
 */
public class ValidationUtil {
    
    // Regex patterns for security validation
    private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<[^>]*>");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9+\\-\\s()]{10,20}$");
    
    // SQL injection patterns
    private static final Pattern[] SQL_INJECTION_PATTERNS = {
        Pattern.compile("(\\b(SELECT|INSERT|UPDATE|DELETE|DROP|CREATE|ALTER|EXEC|UNION|SCRIPT)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(--|/\\*|\\*/|;|'|\"|`)"),
        Pattern.compile("(\\bOR\\b|\\bAND\\b).*[=<>]", Pattern.CASE_INSENSITIVE)
    };
    
    // XSS patterns
    private static final Pattern[] XSS_PATTERNS = {
        Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
        Pattern.compile("on\\w+\\s*=", Pattern.CASE_INSENSITIVE),
        Pattern.compile("data:text/html", Pattern.CASE_INSENSITIVE),
        Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE)
    };

    /**
     * Validates input for security threats (HTML tags, SQL injection, XSS)
     * 
     * @param input Input string to validate
     * @return true if input is safe, false otherwise
     */
    public static boolean isSafeInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return true; // Allow empty values
        }
        
        // Check for HTML tags
        if (HTML_TAG_PATTERN.matcher(input).find()) {
            return false;
        }
        
        // Check for SQL injection patterns
        for (Pattern pattern : SQL_INJECTION_PATTERNS) {
            if (pattern.matcher(input).find()) {
                return false;
            }
        }
        
        // Check for XSS patterns
        for (Pattern pattern : XSS_PATTERNS) {
            if (pattern.matcher(input).find()) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Validates email format
     * 
     * @param email Email string to validate
     * @return true if valid email format, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return true; // Allow empty email (optional field)
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates phone number format
     * 
     * @param phoneNum Phone number to validate
     * @return true if valid phone format, false otherwise
     */
    public static boolean isValidPhoneNumber(String phoneNum) {
        if (phoneNum == null || phoneNum.trim().isEmpty()) {
            return true; // Allow empty phone (optional field)
        }
        return PHONE_PATTERN.matcher(phoneNum.trim()).matches();
    }

    /**
     * Validates string length
     * 
     * @param input String to validate
     * @param maxLength Maximum allowed length
     * @return true if within length limit, false otherwise
     */
    public static boolean isValidLength(String input, int maxLength) {
        if (input == null) {
            return true;
        }
        return input.length() <= maxLength;
    }

    /**
     * Validates that integer is within specified range
     * 
     * @param value Integer value to validate
     * @param min Minimum allowed value (inclusive)
     * @param max Maximum allowed value (inclusive)
     * @return true if within range, false otherwise
     */
    public static boolean isValidRange(Integer value, int min, int max) {
        if (value == null) {
            return true;
        }
        return value >= min && value <= max;
    }
} 