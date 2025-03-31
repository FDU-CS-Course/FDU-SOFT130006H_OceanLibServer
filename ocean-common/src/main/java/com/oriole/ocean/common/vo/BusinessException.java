package com.oriole.ocean.common.vo;

import com.oriole.ocean.common.enumerate.ResultCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Setter
public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -7480022450501760611L;

    /**
     * 异常码
     */
    @Getter
    private String code;

    /**
     * 异常提示信息
     */
    private String message;

    public BusinessException() {
    }

    public BusinessException(ResultCode resultCode) {
        this.code = resultCode.getCode().toString();
        this.message = resultCode.getMsg();
    }

    public BusinessException(String code, String msg) {
        this.code = code;
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
