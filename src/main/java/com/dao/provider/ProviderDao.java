package com.dao.provider;

import com.pojo.Provider;

import java.sql.Connection;
import java.util.List;

/**
 * @Description
 * @Date 2021/1/14 21:39
 * @Version 1.0
 */
public interface ProviderDao {
    List<Provider> getAllProvider(Connection conn, String proCode, String proName, int currentPage, int pageSize);
    
    int getCountTotal(Connection conn, String proCode, String proName);
    
    boolean addProvider(Connection conn, Provider provider);
    
    boolean delProvider(Connection conn, int id);
    
    boolean updateProvider(Connection conn, Provider provider);
    
    Provider getProviderById(Connection conn, int id);
    
    // 给bill调用获取provider的所有数据
    List<Provider> getProviderList(Connection conn);
}
