package com.jack.lottery.vo;

public class LoginResponse {
    /**
     * 用户编号，存在cookie中
     * */
    private long userId;
    /**
     * 用户登录凭证，存在cookie中
     * */
    private String token;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "userId=" + userId +
                ", token='" + token + '\'' +
                '}';
    }
}
