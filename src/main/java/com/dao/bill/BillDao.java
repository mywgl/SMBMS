package com.dao.bill;

import com.pojo.Bill;
import java.sql.Connection;
import java.util.List;

/**
 * @Description
 * @Date 2021/1/14 21:37
 * @Version 1.0
 */
public interface BillDao {
    List<Bill> getAllBill(Connection conn, String ProductName, int ProviderId, int isPayment, int currentPage, int pageSize);
    
    int getCountTotal(Connection conn, String ProductName, int ProviderId, int isPayment);
    
    boolean addBill(Connection conn, Bill bill);
    
    boolean delBill(Connection conn, int id);
    
    boolean updateBill(Connection conn, Bill bill);
    
    Bill getBillById(Connection conn, int id);
}
