package com.service.provder;

import com.dao.BaseDao;
import com.dao.provider.ProviderDao;
import com.dao.provider.ProviderDaoImp;
import com.pojo.Provider;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @Description
 * @Date 2021/1/14 21:41
 * @Version 1.0
 */
public class ProviderServiceImp implements ProviderService {
    private ProviderDao providerDao;
    
    public ProviderServiceImp() {
        this.providerDao = new ProviderDaoImp();
    }
    
    @Override
    public List<Provider> getAllProvider(String proCode, String proName, int currentPage, int pageSize) {
        List<Provider> list = null;
        Connection conn = null;
        try {
            conn = BaseDao.getConnection();
            list = providerDao.getAllProvider(conn, proCode, proName, currentPage, pageSize);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(conn, null, null);
        }
        return list;
    }
    
    @Override
    public int getCountTotal(String proCode, String proName) {
        int rs = 0;
        Connection conn = null;
        try {
            conn = BaseDao.getConnection();
            rs = providerDao.getCountTotal(conn, proCode, proName);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(conn, null, null);
        }
        return rs;
    }
    
    @Override
    public boolean addProvider(Provider provider) {
        Connection conn = null;
        boolean rs = true;
        try {
            conn = BaseDao.getConnection();
            rs = providerDao.addProvider(conn, provider);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(conn, null, null);
        }
        return rs;
    }
    
    @Override
    public boolean delProvider(int id) {
        boolean rs = true;
        Connection conn = null;
        try {
            conn = BaseDao.getConnection();
            rs = providerDao.delProvider(conn, id);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(conn, null, null);
        }
        return rs;
    }
    
    @Override
    public boolean updateProvider(Provider provider) {
        boolean rs = true;
        Connection conn = null;
        try {
            conn = BaseDao.getConnection();
            rs = providerDao.updateProvider(conn, provider);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(conn,null,null);
        }
        return rs;
    }
    
    @Override
    public Provider getProviderById(int id) {
        Provider provider=null;
        Connection conn = null;
        try {
            conn = BaseDao.getConnection();
            provider = providerDao.getProviderById(conn, id);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(conn,null,null);
        }
        return provider;
    }
    
    @Override
    public List<Provider> getProviderList() {
        List<Provider> list = null;
        Connection conn = null;
        try {
            conn = BaseDao.getConnection();
            list = providerDao.getProviderList(conn);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(conn, null, null);
        }
        return list;
    }
    
    @Test
    public void Test() {
        ProviderServiceImp providerServiceImp = new ProviderServiceImp();
        Provider providerById = providerServiceImp.getProviderById(10);
        System.out.println(providerById);
    }
}
