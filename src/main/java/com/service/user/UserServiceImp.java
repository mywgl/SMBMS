package com.service.user;

import com.dao.BaseDao;
import com.dao.user.UserDao;
import com.dao.user.UserDaoImp;
import com.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Description
 * @Date 2021/1/11 11:48
 * @Version 1.0
 */
public class UserServiceImp implements UserService {
    private UserDao userDao;
    
    public UserServiceImp() {
        this.userDao = new UserDaoImp();
    }
    
    @Override
    public User getLoginUser(String userCode, String userPassword) {
        Connection conn = null;
        User loginUser = null;
        try {
            conn = BaseDao.getConnection();
            loginUser = userDao.getLoginUser(conn, userCode, userPassword);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(conn, null, null);
        }
        return loginUser;
    }
    
    @Override
    public boolean updatePassword(int id, String userPassword) {
        Connection conn = null;
        boolean rs = false;
        try {
            conn = BaseDao.getConnection();
            rs = userDao.updatePassword(conn, id, userPassword);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(conn, null, null);
        }
        return rs;
    }
    
    @Override
    public int queryUserCount(String userName, int userRole) {
        Connection conn = null;
        int count = 0;
        try {
            conn = BaseDao.getConnection();
            count = userDao.queryUserCount(conn, userName, userRole);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(conn, null, null);
        }
        return count;
    }
    
    @Override
    public List<User> queryUserList(String userName, int userRole, int currentPage, int pageSize) {
        Connection conn = null;
        List<User> list = null;
        try {
            conn = BaseDao.getConnection();
            list = userDao.queryUserList(conn, userName, userRole, currentPage, pageSize);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(conn, null, null);
        }
        return list;
    }
    
    @Override
    public boolean queryUserCode(String userCode) {
        Connection conn = null;
        boolean rs = false;
        try {
            conn = BaseDao.getConnection();
            rs = userDao.queryUserCode(conn, userCode);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(conn,null,null);
        }
        return rs;
    }
    
    @Override
    public boolean addUser(User user) {
        Connection conn = null;
        boolean rs = true;
        try {
            conn = BaseDao.getConnection();
            rs = userDao.addUser(conn, user);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(conn, null, null);
        }
        return rs;
    }
    
    @Override
    public boolean delUser(int id) {
        boolean rs = true;
        Connection conn = null;
        try {
            conn = BaseDao.getConnection();
            rs = userDao.delUser(conn,id);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(conn,null,null);
        }
        return rs;
    }
    
    @Override
    public boolean updateUser(User user) {
        boolean rs = true;
        Connection conn = null;
        try {
            conn = BaseDao.getConnection();
            rs = userDao.updateUser(conn,user);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(conn,null,null);
        }
        return rs;
    }
    
    @Override
    public User getUserById(int id) {
        User user=null;
        Connection conn = null;
        try {
            conn = BaseDao.getConnection();
            user = userDao.getUserById(conn, id);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(conn,null,null);
        }
        return user;
    }
    
    
    @Test
    public void test() {
        UserServiceImp userServiceImp = new UserServiceImp();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
    }
}
