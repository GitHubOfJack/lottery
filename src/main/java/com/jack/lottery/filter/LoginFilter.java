package com.jack.lottery.filter;

import com.alibaba.fastjson.JSONObject;
import com.jack.lottery.entity.User;
import com.jack.lottery.mapper.UserMapper;
import com.jack.lottery.vo.CommonResponose;
import com.jack.lottery.vo.ResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {

    private UserMapper userMapper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        userMapper = (UserMapper) webApplicationContext.getBean("userMapper");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String userId = request.getParameter("userId");
        if (StringUtils.isBlank(userId) || StringUtils.isNumeric(userId) || !checkUser(Long.parseLong(userId))) {
            returnNotLogin(response);
            return;
        }
        HttpSession session = request.getSession();
        Object loginToken = session.getAttribute("loginToken");
        if (null == loginToken) {
            returnNotLogin(response);
            return;
        }
        String loginTokenStr = (String) loginToken;
        Cookie[] cookies = request.getCookies();
        boolean isLogin = false;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("loginToken") && cookie.getValue().equals(loginTokenStr)) {
                isLogin = true;
            }
        }
        if (isLogin) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            returnNotLogin(response);
        }
    }

    @Override
    public void destroy() {

    }

    private void returnNotLogin(HttpServletResponse response) throws IOException {
        CommonResponose resp = new CommonResponose(ResponseCode.NOT_LOGIN, null);
        String json = JSONObject.toJSONString(resp);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().append(json);
    }

    private boolean checkUser(long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (null == user) {
            return false;
        }
        return true;
     }
}
