package com.service.bill;

import com.pojo.Bill;
import com.pojo.Provider;

import java.util.List;

/**
 * @Description
 * @Date 2021/1/14 21:42
 * @Version 1.0
 */
public interface BillService {
    List<Bill> getAllBill(String productName, int providerId, int isPayment, int currentPage, int pageSize);
    
    int getCountTotal(String productName, int providerId, int isPayment);
    
    boolean addBill(Bill bill);
    
    boolean delBill(int id);
    
    boolean updateBill(Bill bill);
    
    Bill getBillById(int id);
    
    List<Provider> getProviderList();
}
