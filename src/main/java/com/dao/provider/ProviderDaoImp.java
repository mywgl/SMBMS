package com.dao.provider;

import com.dao.BaseDao;
import com.mysql.jdbc.StringUtils;
import com.pojo.Provider;

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
 * @Date 2021/1/14 21:39
 * @Version 1.0
 */
public class ProviderDaoImp implements ProviderDao {
    @Override
    public List<Provider> getAllProvider(Connection conn, String proCode, String proName, int currentPage, int pageSize) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder();
        sql.append("select * from smbms_provider ");
        ArrayList<Object> params = addSqlAndParams(proCode, proName, sql);
        sql.append(" limit ?,?");
        params.add(currentPage);
        params.add(pageSize);
        List<Provider> providers = new ArrayList<>();
        try {
            rs = BaseDao.executeQuery(conn, ps, sql.toString(), params.toArray());
            while (rs.next()) {
                providers.add(getProvider(rs));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(null, ps, rs);
        }
        return providers;
    }
    
    @Override
    public int getCountTotal(Connection conn, String proCode, String proName) {
        int countTotal = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) total from smbms_provider");
        ArrayList<Object> params = addSqlAndParams(proCode, proName, sql);
        List<Provider> providers = new ArrayList<>();
        try {
            rs = BaseDao.executeQuery(conn, ps, sql.toString(), params.toArray());
            if (rs.next()) {
                countTotal = rs.getInt("total");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(null, ps, rs);
        }
        return countTotal;
    }
    
    @Override
    public boolean addProvider(Connection conn, Provider provider) {
        PreparedStatement ps = null;
        boolean rs = true;
        try {
            String sql = "insert into smbms_provider(proCode,proName,proDesc,proContact,proPhone,proAddress,proFax,createdBy,creationDate) values(?,?,?,?,?,?,?,?,?)";
            List<Object> params = new ArrayList<>();
            getFields(provider, params);
            rs = BaseDao.executeUpdate(conn, ps, sql, params.toArray());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            
        } finally {
            BaseDao.closeResource(null, ps, null);
        }
        return rs;
    }
    
    @Override
    public boolean delProvider(Connection conn, int id) {
        PreparedStatement ps = null;
        boolean rs = true;
        try {
            String sql = "delete from smbms_provider where id=?";
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
    public boolean updateProvider(Connection conn, Provider provider) {
        PreparedStatement ps = null;
        boolean rs = true;
        try {
            String sql = "update smbms_provider set proCode=?, proName=?,proDesc=?,proContact=?,proPhone=?,proAddress=?,proFax=?,modifyDate=?,modifyBy=? where id=?";
            List<Object> params = new ArrayList<>();
            getFields(provider, params);
            // 通过反射封装的参数数组，id顺序调整到最后一位与sql参数位置对应
            Object remove = params.remove(0);
            params.add(params.size(), remove);
            rs = BaseDao.executeUpdate(conn, ps, sql, params.toArray());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(null, ps, null);
        }
        return rs;
    }
    
    @Override
    public Provider getProviderById(Connection conn, int id) {
        Provider provider = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select * from smbms_provider where id=? ";
            Object[] params = {id};
            rs = BaseDao.executeQuery(conn, ps, sql, params);
            if (rs.next()) {
                provider = getProvider(rs);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(null, ps, rs);
        }
        return provider;
    }
    
    @Override
    public List<Provider> getProviderList(Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Provider> providers = new ArrayList<>();
        try {
            String sql = "select * from smbms_provider ";
            rs = BaseDao.executeQuery(conn, ps, sql,null);
            while (rs.next()) {
                providers.add(getProvider(rs));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(null, ps, rs);
        }
        return providers;
    }
    
    // 通过反射把对象属性转换成数组
    private void getFields(Provider provider, List<Object> params) {
        Field[] declaredFields = provider.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            try {
                Object value = declaredField.get(provider);
                if (Objects.nonNull(value))
                    params.add(value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    
    // 动态添加sql和参数
    private ArrayList<Object> addSqlAndParams(String proCode, String proName, StringBuilder sql) {
        ArrayList<Object> params = new ArrayList<>();
        if (!StringUtils.isNullOrEmpty(proCode) && !StringUtils.isNullOrEmpty(proName)) {
            sql.append(" where proCode like ? and  proName like ?");
            params.add("%" + proCode + "%");
            params.add("%" + proName + "%");
        } else if (!StringUtils.isNullOrEmpty(proCode)) {
            sql.append(" where proCode like ?");
            params.add("%" + proCode + "%");
        } else if (!StringUtils.isNullOrEmpty(proName)) {
            sql.append(" where proName like ?");
            params.add("%" + proName + "%");
        }
        return params;
    }
    
    private Provider getProvider(ResultSet rs) throws SQLException {
        Provider provider = new Provider();
        provider.setId(rs.getInt("id"));
        provider.setProCode(rs.getString("proCode"));
        provider.setProName(rs.getString("proName"));
        provider.setProDesc(rs.getString("proDesc"));
        provider.setProContact(rs.getString("proContact"));
        provider.setProPhone(rs.getString("proPhone"));
        provider.setProAddress(rs.getString("proAddress"));
        provider.setProFax(rs.getString("proFax"));
        provider.setCreatedBy(rs.getInt("createdBy"));
        provider.setCreationDate(rs.getTimestamp("creationDate"));
        provider.setModifyDate(rs.getTimestamp("modifyDate"));
        provider.setModifyBy(rs.getInt("modifyBy"));
        return provider;
    }
}
