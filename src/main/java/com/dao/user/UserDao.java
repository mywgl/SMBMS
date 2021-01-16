package com.dao.user;

import com.pojo.User;

import java.sql.Connection;
import java.util.List;

public interface UserDao {
  //获取登录用户
  User getLoginUser(Connection conn,String userCode,String userPassword);
  
  // 修改用户密码
  boolean updatePassword(Connection conn, int id, String password);
 
  // 通过用户名或者用户角色查询相应用户总数
  int queryUserCount(Connection conn,String userName,int userRole);
  /***
   * @Description
   * 通过 用户名或者用户角色查询用户数据
   * @Date 2021/1/12 11:49
   * @param conn
   * @param userName 用户名
   * @param userRole 用户角色
   * @param pageSize 返回的数据量
   * @return 返回对应的数据
   */
  List<User> queryUserList(Connection conn,String userName,int userRole,int currentPage,int pageSize);
  
  // 查询用户账号是否存在
  boolean queryUserCode(Connection conn,String userCode);
  
  boolean addUser(Connection conn, User user);
  
  boolean delUser(Connection conn, int id);
  
  boolean updateUser(Connection conn,User user);
  
  User getUserById(Connection conn,int id);
}
