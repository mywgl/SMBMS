package com.service.user;

import com.pojo.User;

import java.util.List;

public interface UserService {
    
    User getLoginUser(String userCode, String userPassword);
    
    boolean updatePassword(int id, String userPassword);
    
    // 查询用户总数
    int queryUserCount(String userName, int userRole);
    
    List<User> queryUserList(String userName, int userRole, int currentPage, int pageSize);
    
    boolean queryUserCode(String userCode);
    
    boolean addUser(User user);
    
    boolean delUser(int id);
    
    boolean updateUser(User user);
    
    User getUserById(int id);
}
