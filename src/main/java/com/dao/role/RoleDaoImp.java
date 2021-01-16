package com.dao.role;

import com.dao.BaseDao;
import com.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Date 2021/1/13 10:48
 * @Version 1.0
 */
public class RoleDaoImp implements RoleDao {
    @Override
    public List<Role> getRoleList(Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Role> roles = null;
        try {
            String sql = "select * from smbms_role";
            rs = BaseDao.executeQuery(conn, ps, sql, null);
            roles = new ArrayList<>();
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleCode(rs.getString("roleCode"));
                role.setRoleName(rs.getString("roleName"));
                role.setCreatedBy(rs.getInt("createdBy"));
                role.setCreationDate(rs.getTimestamp("creationDate"));
                role.setModifyBy(rs.getInt("modifyBy"));
                role.setModifyDate(rs.getTimestamp("modifyDate"));
                roles.add(role);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(null, ps, rs);
        }
        
        return roles;
    }
}
