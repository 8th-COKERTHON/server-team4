package com.team4.hackerton.global.apiPayload.exception;

import com.team4.hackerton.global.apiPayload.code.BaseErrorCode;
import lombok.Getter;

import java.util.Map;

@Getter
public class AppException extends RuntimeException {

    private final BaseErrorCode code;
    private final Map<String, String> bind;

    public AppException(BaseErrorCode code) {
        this.code = code;
        this.bind = null;
    }

    public AppException(BaseErrorCode code, Map<String, String> bind) {
        this.code = code;
        this.bind = bind;
    }
}
