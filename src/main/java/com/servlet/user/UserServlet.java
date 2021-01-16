package com.servlet.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;
import com.pojo.Role;
import com.pojo.User;
import com.service.role.RoleService;
import com.service.role.RoleServiceImp;
import com.service.user.UserService;
import com.service.user.UserServiceImp;
import com.util.Constants;
import com.util.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @Description
 * @Date 2021/1/11 22:12
 * @Version 1.0
 */
@WebServlet("/jsp/user.do")
public class UserServlet extends HttpServlet {
    private UserService userService = new UserServiceImp();
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method != null) {
            if ("verificationPwd".equals(method)) { // 验证旧密码
                verificationPwd(req, resp);
            } else if ("pwdModify".equals(method)) { //修改更新密码
                try {
                    updatePwd(req, resp);
                } catch (ServletException | IOException e) {
                    e.printStackTrace();
                }
            } else if ("query".equals(method)) {
                queryUserInfo(req, resp);
            } else if ("ucExist".equals(method)) {
                userCodeIsExist(req, resp);
            } else if ("getRoleList".equals(method)) {
                getRoleList(req, resp);
            } else if ("add".equals(method)) {
                addUser(req, resp);
            } else if ("delUser".equals(method)) {
                delUser(req, resp);
            } else if ("view".equals(method)) {
                view(req, resp);
            } else if ("modify".equals(method)) {
                modifyUser(req, resp);
            }else if ("modifyExe".equals(method)) {
                modifyExe(req, resp);
            }
        }
        
    }
    
    private void modifyExe(HttpServletRequest req, HttpServletResponse resp) {
        User user = new User();
        user.setId(Integer.parseInt(req.getParameter("uid")));
        user.setUserName(req.getParameter("userName"));
        user.setGender(Integer.parseInt(req.getParameter("gender")));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            user.setBirthday(dateFormat.parse(req.getParameter("birthday")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(req.getParameter("phone"));
        user.setAddress(req.getParameter("address"));
        user.setUserRole(Integer.parseInt(req.getParameter("userRole")));
        boolean rs = userService.updateUser(user);
        if (rs){
            queryUserInfo(req,resp);
        }
    }
    
    private void modifyUser(HttpServletRequest req, HttpServletResponse resp) {
        String uid = req.getParameter("uid");
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");
        // 从数据ku获取出来的数据
        User oldUser = userService.getUserById(Integer.parseInt(uid));
        req.setAttribute("user", oldUser);
        try {
            req.getRequestDispatcher("usermodify.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private void view(HttpServletRequest req, HttpServletResponse resp) {
        String uid = req.getParameter("uid");
        if (!StringUtils.isNullOrEmpty(uid)) {
            User user = userService.getUserById(Integer.parseInt(uid));
            req.setAttribute("user", user);
            try {
                req.getRequestDispatcher("userview.jsp").forward(req, resp);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        } else {
            req.getSession().removeAttribute(Constants.USER_SESSION);
            try {
                resp.sendRedirect(req.getContextPath() + "error.jsp");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }
    
    private void delUser(HttpServletRequest req, HttpServletResponse resp) {
        String uid = req.getParameter("uid");
        boolean result;
        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isNullOrEmpty(uid)) {
            int id = Integer.parseInt(uid);
            // 先查询用户是否存在
            User user = userService.getUserById(id);
            if (Objects.nonNull(user)) {
                result = userService.delUser(id);
                map.put("delResult", result);
            } else {
                map.put("delResult", "notExist");
            }
            respJsonMessage(resp, map, null);
        }
    }
    
    private void addUser(HttpServletRequest req, HttpServletResponse resp) {
        User user = new User();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        user.setUserCode(req.getParameter("userCode"));
        user.setUserName(req.getParameter("userName"));
        user.setUserPassword(req.getParameter("userPassword"));
        user.setGender(Integer.parseInt(req.getParameter("gender")));
        try {
            user.setBirthday(dateFormat.parse(req.getParameter("birthday")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(req.getParameter("phone"));
        user.setAddress(req.getParameter("address"));
        user.setUserRole(Integer.parseInt(req.getParameter("userRole")));
        User loginUser = (User) req.getSession().getAttribute(Constants.USER_SESSION);
        user.setCreatedBy(loginUser.getUserRole());
        user.setCreationDate(new Date());
        boolean result = userService.addUser(user);
        try {
            if (result)
                req.setAttribute(Constants.SYS_MESSAGE, "添加成功！");
            else
                req.setAttribute(Constants.SYS_MESSAGE, "添加失败！");
            req.getRequestDispatcher("useradd.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private void getRoleList(HttpServletRequest req, HttpServletResponse resp) {
        RoleService roleService = new RoleServiceImp();
        List<Role> roleList = roleService.getRoleList();
        respJsonMessage(resp, null, roleList);
    }
    
    private void userCodeIsExist(HttpServletRequest req, HttpServletResponse resp) {
        String userCode = req.getParameter("userCode");
        UserService userService = new UserServiceImp();
        HashMap<String, String> map = new HashMap<>();
        if (!StringUtils.isNullOrEmpty(userCode)) {
            boolean rs = userService.queryUserCode(userCode);
            if (rs)
                map.put("userCode", "exist");
            else
                map.put("userCode", "No exist");
        } else {
            map.put("userCode", "userCode空参");
        }
        respJsonMessage(resp, map, null);
    }
    
    private void respJsonMessage(HttpServletResponse resp, Map<?, ?> mapResult, List listResult) {
        try {
            resp.setContentType("application/json;charset=utf-8");
            PrintWriter out = resp.getWriter();
            if (Objects.nonNull(mapResult))
                out.println(JSONObject.toJSONString(mapResult));
            else
                out.println(JSONArray.toJSONString(listResult));
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // 验证旧密码
    private void verificationPwd(HttpServletRequest request, HttpServletResponse response) {
        String oldpassword = request.getParameter("oldpassword");
        User user = (User) request.getSession().getAttribute(Constants.USER_SESSION);
        Map<String, String> map = new HashMap<>();
        if (user == null) {  // session失效
            map.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldpassword)) {  // 密码为空
            map.put("result", "error");
        } else {
            if (oldpassword.equals(user.getUserPassword()))// 密码验证成功
                map.put("result", "true");
            else map.put("result", "false");// 密码验证失败
        }
        response.addHeader("Content-Type", "application/json;charset=utf-8");
        try {
            PrintWriter out = response.getWriter();
            out.println(JSON.toJSONString(map));
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    // 修改密码
    private void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //  从session中获取当前用户id
        User user = (User) req.getSession().getAttribute(Constants.USER_SESSION);
        String newPassword = req.getParameter("newpassword");
        if (user != null && !StringUtils.isNullOrEmpty(newPassword)) {
            UserService userService = new UserServiceImp();
            boolean re = userService.updatePassword(user.getId(), newPassword);
            if (re) {
                req.setAttribute(Constants.SYS_MESSAGE, "修改密码成功，请您退出重新使用新密码登录！");
                //删除用户session强制用户重新登录
                req.getSession().removeAttribute(Constants.USER_SESSION);
            } else {
                req.setAttribute(Constants.SYS_MESSAGE, "密码修改失败！");
            }
        } else {
            req.setAttribute(Constants.SYS_MESSAGE, "新密码有问题！");
        }
        req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);
    }
    
    private void queryUserInfo(HttpServletRequest req, HttpServletResponse resp) {
        String userName = req.getParameter("queryName");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int userRole = 0;
        int pageSize = Constants.PAGE_SIZE;
        int currentPage = 1;
        PageSupport page = new PageSupport();
        if (!StringUtils.isNullOrEmpty(pageIndex)) {
            currentPage = Integer.parseInt(pageIndex);
        }
        
        if (!StringUtils.isNullOrEmpty(temp)) {
            userRole = Integer.parseInt(temp);
        }
        
        RoleService roleService = new RoleServiceImp();
        // 获取角色列表
        List<Role> roleList = roleService.getRoleList();
        
        UserService userService = new UserServiceImp();
        // 通过选择的用户名和用户角色返回查询的数据总量
        int totalCount = userService.queryUserCount(userName, userRole);
        // 计算分页
        page.setCurrentPageNo(currentPage);
        page.setPageSize(pageSize);
        page.setTotalCount(totalCount);
        int totalPageCount = page.getTotalPageCount();
        if (currentPage < 1) {
            currentPage = 1;
        } else if (currentPage > totalPageCount) {
            currentPage = totalPageCount;
        }
        // 根据选择条件查询用户信息
        List<User> userList = userService.queryUserList(userName, userRole, page.getCurrentPageNo(), page.getPageSize());
        
        req.setAttribute("queryUserName", userName);
        req.setAttribute("queryUserRole", userRole);
        req.setAttribute("roleList", roleList);
        req.setAttribute("userList", userList);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", currentPage);
        
        try {
            req.getRequestDispatcher("userlist.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
