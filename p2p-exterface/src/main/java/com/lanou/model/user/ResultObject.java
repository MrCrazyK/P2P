package com.lanou.model.user;

import java.io.Serializable;

/**
 * ClassName : ResultObject
 * PackageName : com.lanou.model.user
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/8/20 9:22
 * @Version : 1.0
 */
public class ResultObject implements Serializable{

    /**
     * 错误码SUCCESS|FAIL
     */
    private String errorCode;

    /**
     * 错误消息
     */
    private String errorMessage;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
