package com.filter;

import com.pojo.User;
import com.util.Constants;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description
 * 登录拦截
 * 解决用户登出之后直接访问主页还能进去的问题
 * @Date 2021/1/11 15:04
 * @Version 1.0
 */
@WebFilter("/jsp/*")
public class LoinFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        User user = (User)request.getSession().getAttribute(Constants.USER_SESSION);
        System.out.println("访问验证中。。。。。");
        if (user!=null){
            filterChain.doFilter(request,response);
            System.out.println("访问验证通过！！！！！");
        }else{
            response.sendRedirect(request.getContextPath()+"/error.jsp");
        }
    }

    @Override
    public void destroy() {

    }
}
