package com.zy.mscloudgateway.exception;

import lombok.Data;

/**
 * @author: zy
 * @date: 2021/8/9 下午3:15
 * @description:
 */
@Data
public class GateWayException  extends RuntimeException {

    private String code;

    private String msg;

    public GateWayException(SystemErrorType systemErrorType) {
        super(systemErrorType.getMesg());
        this.code = systemErrorType.getCode();
    }
}
