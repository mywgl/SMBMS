package com.service.role;

import com.dao.BaseDao;
import com.dao.role.RoleDao;
import com.dao.role.RoleDaoImp;
import com.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @Description
 * @Date 2021/1/13 10:56
 * @Version 1.0
 */
public class RoleServiceImp implements RoleService{
   private RoleDao roleDao;
    
    public RoleServiceImp() {
        this.roleDao = new RoleDaoImp();
    }
    
    @Override
    public List<Role> getRoleList() {
        Connection conn = null;
        List<Role> list = null;
        try {
            conn = BaseDao.getConnection();
            list = roleDao.getRoleList(conn);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResource(conn,null,null);
        }
        
        return list;
    }
}
