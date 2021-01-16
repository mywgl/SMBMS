package com.service.provder;

import com.pojo.Provider;

import java.util.List;

public interface ProviderService {
    List<Provider> getAllProvider(String proCode, String proName, int currentPage, int pageSize);
    
    int getCountTotal(String proCode, String proName);
    
    boolean addProvider(Provider provider);
    
    boolean delProvider(int id);
    
    boolean updateProvider(Provider provider);
    
    Provider getProviderById(int id);
    
    List<Provider> getProviderList();
}
