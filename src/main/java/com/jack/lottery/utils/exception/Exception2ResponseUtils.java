package com.jack.lottery.utils.exception;

import com.jack.lottery.vo.CommonResponose;
import com.jack.lottery.vo.ResponseCode;
import org.apache.ibatis.annotations.Param;

import java.text.ParseException;

public class Exception2ResponseUtils {
    public static CommonResponose getResponse(Exception e) {
        CommonResponose resp = null;
        if (e instanceof ParseException) {
            resp = new CommonResponose(ResponseCode.PARAM_ERROR, null, e.getMessage());
        } else if (e instanceof DBException) {
            resp = new CommonResponose(ResponseCode.DB_ERROR, null, e.getMessage());
        } else if (e instanceof InterfaceException) {
            resp = new CommonResponose(ResponseCode.INTERFACE_ERROR, null, e.getMessage());
        } else if (e instanceof ParamException) {
            resp = new CommonResponose(ResponseCode.PARAM_ERROR, null, e.getMessage());
        } else {
            resp = new CommonResponose(ResponseCode.UNKNOW_ERROR, null, e.getMessage());
        }
        return resp;
    }
}
