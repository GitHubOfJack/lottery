package com.jack.lottery.utils.exception;

import com.jack.lottery.vo.CommonResponose;
import com.jack.lottery.vo.ResponseCode;
import org.apache.ibatis.annotations.Param;

import java.text.ParseException;

public class Exception2ResponseUtils {
    //错误统一输出格式    错误内容(直接页面输出内容)|错误详情
    public static CommonResponose getResponse(Exception e) {
        String appandMsg = e.getMessage().split("\\|")[0];
        CommonResponose resp = null;
        if (e instanceof SystermException) {
            resp = new CommonResponose(ResponseCode.SYSTEM_ERROR, null, appandMsg);
        } else if (e instanceof DBException) {
            resp = new CommonResponose(ResponseCode.DB_ERROR, null, appandMsg);
        } else if (e instanceof InterfaceException) {
            resp = new CommonResponose(ResponseCode.INTERFACE_ERROR, null, appandMsg);
        } else if (e instanceof ParamException) {
            resp = new CommonResponose(ResponseCode.PARAM_ERROR, null, appandMsg);
        } else {
            resp = new CommonResponose(ResponseCode.UNKNOW_ERROR, null, appandMsg);
        }
        return resp;
    }
}
