package com.jack.lottery.enums;

import com.jack.lottery.utils.exception.ParamException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaoma3 on 2018/1/18.
 */
public enum IdType {
    SFZ("1", "身份证"),
    JGZ("2", "军官证"),
    ;
    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private IdType (String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<String, IdType> map = new HashMap<>();

    static {
        IdType[] values = IdType.values();
        for (IdType type : values) {
            map.put(type.code, type);
        }
    }

    public static IdType getTypeByCode(String code) throws ParamException {
        IdType idType = map.get(code);
        if (null == idType) {
            throw new ParamException("证件类型不合法");
        }
        return idType;
    }
}
