package com.app.pharmacy.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomResponseException extends RuntimeException {
    public CustomResponseException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    private ErrorCode errorCode;
}
