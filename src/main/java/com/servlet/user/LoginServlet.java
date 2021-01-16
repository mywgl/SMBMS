package com.servlet.user;

import com.pojo.User;
import com.service.user.UserService;
import com.service.user.UserServiceImp;
import com.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description
 * @Date 2021/1/11 11:54
 * @Version 1.0
 */
@WebServlet("/login.do")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");
        System.out.println("进入登录验证中。。。。。");
        System.out.println("userCode->"+userCode);
        System.out.println("password->"+userPassword);
        UserService userService = new UserServiceImp();
        User user = userService.getLoginUser(userCode, userPassword);
        if (user!=null){
            req.getSession().setAttribute(Constants.USER_SESSION,user);
            resp.sendRedirect("jsp/frame.jsp");
        }else{
            req.setAttribute(Constants.SYS_MESSAGE,"用户名或密码不正确！");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
