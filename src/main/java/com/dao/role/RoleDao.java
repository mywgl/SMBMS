package com.dao.role;

import com.pojo.Role;

import java.sql.Connection;
import java.util.List;

/**
 * @Description
 * @Date 2021/1/13 10:48
 * @Version 1.0
 */
public interface RoleDao {
    // 获取角色列表
    List<Role> getRoleList(Connection conn);
}
