package com.dao.user;

import com.dao.BaseDao;
import com.mysql.jdbc.StringUtils;
import com.pojo.User;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Description
 * @Date 2021/1/11 11:04
 * @Version 1.0
 */
public class UserDaoImp implements UserDao {
    
    @Override
    public User getLoginUser(Connection conn, String userCode, String userPassword) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        try {
            String sql = "select * from smbms_user where userCode=? and userPassword=?";
            Object[] params = {userCode, userPassword};
            rs = BaseDao.executeQuery(conn, ps, sql, params);
            while (rs.next()) {
                user = getUser(rs);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(null, ps, rs);
        }
        return user;
    }
    
    @Override
    public boolean updatePassword(Connection conn, int id, String password) {
        
        PreparedStatement ps = null;
        boolean result = false;
        try {
            Object[] params = {password, id};
            String sql = "update smbms_user set userPassword=? where id=?";
            result = BaseDao.executeUpdate(conn, ps, sql, params);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(null, ps, null);
        }
        return result;
    }
    
    @Override
    public int queryUserCount(Connection conn, String userName, int userRole) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) total from smbms_user u,smbms_role r where u.userRole=r.id");
        ArrayList<Object> params = new ArrayList<>();
        if (!StringUtils.isNullOrEmpty(userName)) {
            sql.append(" and userName like ?");
            params.add("%" + userName + "%");
        }
        if (userRole > 0) {
            sql.append(" and userRole = ?");
            params.add(userRole);
        }
        int total = 0;
        try {
            rs = BaseDao.executeQuery(conn, ps, sql.toString(), params.toArray());
            while (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(null, ps, rs);
        }
        return total;
    }
    
    @Override
    public List<User> queryUserList(Connection conn, String userName, int userRole, int currentPage, int pageSize) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder();
        sql.append("select u.*,r.roleName from smbms_user u,smbms_role r where u.userRole=r.id");
        ArrayList<Object> params = new ArrayList<>();
        if (!StringUtils.isNullOrEmpty(userName)) {
            sql.append(" and userName like ?");
            params.add("%" + userName + "%");
        }
        if (userRole > 0) {
            sql.append(" and userRole = ?");
            params.add(userRole);
        }
        sql.append(" order by creationDate desc limit ?,?");
        params.add(currentPage);
        params.add(pageSize);
        List<User> users = new ArrayList<>();
        try {
            rs = BaseDao.executeQuery(conn, ps, sql.toString(), params.toArray());
            while (rs.next()) {
                User user = getUser(rs);
                user.setUserRoleName(rs.getString("roleName"));
                users.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(null, ps, rs);
        }
        return users;
        
    }
    
    @Override
    public boolean queryUserCode(Connection conn, String userCode) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            String sql = "select userCode from smbms_user where userCode=?";
            Object[] params = {userCode};
            rs = BaseDao.executeQuery(conn, ps, sql, params);
            result = rs.next() ? true : false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(null, ps, rs);
        }
        return result;
    }
    
    @Override
    public boolean addUser(Connection conn, User user) {
        PreparedStatement ps = null;
        boolean rs = true;
        try {
            String sql = "insert into smbms_user(userCode,userName,userPassword,gender,birthday,phone,address,userRole,createdBy,creationDate) values(?,?,?,?,?,?,?,?,?,?) ";
            List<Object> params = new ArrayList<>();
            getParams(user, params);
            rs = BaseDao.executeUpdate(conn, ps, sql, params.toArray());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            
        } finally {
            BaseDao.closeResource(null, ps, null);
        }
        return rs;
    }
    
    @Override
    public boolean delUser(Connection conn, int id) {
        PreparedStatement ps = null;
        boolean rs = true;
        try {
            String sql = "delete from smbms_user where id=?";
            Object[] params = {id};
            rs = BaseDao.executeUpdate(conn, ps, sql, params);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(null, ps, null);
        }
        return rs;
    }
    
    @Override
    public boolean updateUser(Connection conn, User user) {
        PreparedStatement ps = null;
        boolean rs = true;
        List<Object> params = new ArrayList<>();
        getParams(user,params);
        // 通过反射封装的参数数组，id顺序调整到最后一位与sql参数位置对应
        Object remove = params.remove(0);
        params.add(params.size(),remove);
        try {
            String sql =
                    "update smbms_user set userName=?,gender=?,birthday=?,phone=?,address=?,userRole=? where id=? ";
            rs = BaseDao.executeUpdate(conn, ps, sql, params.toArray());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResource(null,ps,null);
        }
        return rs;
    }
    
    @Override
    public User getUserById(Connection conn, int id) {
        User user = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select u.*,r.roleName from smbms_user u,smbms_role r where u.userRole=r.id having id=? ";
            Object[] params = {id};
            rs = BaseDao.executeQuery(conn, ps, sql, params);
            while (rs.next()){
                user = getUser(rs);
                user.setUserRoleName(rs.getString("roleName"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally{
            BaseDao.closeResource(null,ps,rs);
        }
        return user;
    }
    
    private void getParams(User user, List<Object> params) {
        Field[] declaredFields = user.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            try {
                Object value = declaredField.get(user);
                if (Objects.nonNull(value))
                    params.add(value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    
    private User getUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUserName(rs.getString("userName"));
        user.setUserCode(rs.getString("userCode"));
        user.setUserPassword(rs.getString("userPassword"));
        user.setGender(rs.getInt("gender"));
        user.setBirthday(rs.getDate("birthday"));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));
        user.setUserRole(rs.getInt("userRole"));
        user.setCreatedBy(rs.getInt("createdBy"));
        user.setCreationDate(rs.getTimestamp("creationDate"));
        user.setModifyBy(rs.getInt("modifyBy"));
        user.setModifyDate(rs.getTimestamp("modifyDate"));
        return user;
    }
}
