package com.service.bill;

import com.dao.BaseDao;
import com.dao.bill.BillDao;
import com.dao.bill.BillDaoImp;
import com.pojo.Bill;
import com.pojo.Provider;
import com.service.provder.ProviderService;
import com.service.provder.ProviderServiceImp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BillServiceImp implements BillService {
    private BillDao billDao;
    
    public BillServiceImp() {
        this.billDao = new BillDaoImp();
    }
    
    @Override
    public List<Bill> getAllBill(String productName, int providerId, int isPayment, int currentPage, int pageSize) {
        List<Bill> list = null;
        Connection conn = null;
        try {
            conn = BaseDao.getConnection();
            list = billDao.getAllBill(conn, productName, providerId, isPayment, currentPage, pageSize);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(conn, null, null);
        }
        return list;
    }
    
    @Override
    public int getCountTotal(String productName, int providerId, int isPayment) {
        int rs = 0;
        Connection conn = null;
        try {
            conn = BaseDao.getConnection();
            rs = billDao.getCountTotal(conn, productName, providerId, isPayment);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(conn, null, null);
        }
        return rs;
    }
    
    @Override
    public boolean addBill(Bill bill) {
        Connection conn = null;
        boolean rs = true;
        try {
            conn = BaseDao.getConnection();
            rs = billDao.addBill(conn, bill);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(conn, null, null);
        }
        return rs;
    }
    
    @Override
    public boolean delBill(int id) {
        boolean rs = true;
        Connection conn = null;
        try {
            conn = BaseDao.getConnection();
            rs = billDao.delBill(conn, id);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(conn, null, null);
        }
        return rs;
    }
    
    @Override
    public boolean updateBill(Bill bill) {
        boolean rs = true;
        Connection conn = null;
        try {
            conn = BaseDao.getConnection();
            rs = billDao.updateBill(conn, bill);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(conn,null,null);
        }
        return rs;
    }
    
    @Override
    public Bill getBillById(int id) {
        Bill bill = null;
        Connection conn = null;
        try {
            conn = BaseDao.getConnection();
            bill = billDao.getBillById(conn, id);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(conn,null,null);
        }
        return bill;
    }
    
    @Override
    public List<Provider> getProviderList() {
        ProviderService providerService = new ProviderServiceImp();
        return providerService.getProviderList();
    }
}
