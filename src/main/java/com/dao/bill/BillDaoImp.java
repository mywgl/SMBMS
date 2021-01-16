package com.dao.bill;

import com.dao.BaseDao;
import com.mysql.jdbc.StringUtils;
import com.pojo.Bill;
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
 * @Date 2021/1/14 21:38
 * @Version 1.0
 */
public class BillDaoImp implements BillDao{
    @Override
    public List<Bill> getAllBill(Connection conn, String ProductName, int ProviderId, int isPayment, int currentPage, int pageSize) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder("select b.*,p.proName from smbms_bill b,smbms_provider p where b.providerId=p.id");
        ArrayList<Object> params = autoAddSqlAndParams(ProductName, ProviderId,isPayment,sql);
        sql.append(" limit ?,?");
        params.add(currentPage);
        params.add(pageSize);
        List<Bill> bills = new ArrayList<>();
        try {
            rs = BaseDao.executeQuery(conn, ps, sql.toString(), params.toArray());
            while (rs.next()) {
                Bill bill = getBill(rs);
                bill.setProviderName(rs.getString("proName"));
                bills.add(bill);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(null, ps, rs);
        }
        return bills;
    }
    
    @Override
    public int getCountTotal(Connection conn, String ProductName, int ProviderId, int isPayment) {
        int countTotal = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder("select count(*) total from smbms_bill b,smbms_provider p where b.providerId=p.id");
        ArrayList<Object> params = autoAddSqlAndParams(ProductName,ProviderId,isPayment,sql);
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
    public boolean addBill(Connection conn, Bill bill) {
        PreparedStatement ps = null;
        boolean rs = true;
        try {
            String sql = "insert into smbms_bill(billCode,productName,productDesc,productUnit,productCount,totalPrice,isPayment,createdBy,creationDate,providerId) values(?,?,?,?,?,?,?,?,?,?)";
            List<Object> params = new ArrayList<>();
            getFields(bill, params);
            rs = BaseDao.executeUpdate(conn, ps, sql, params.toArray());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(null, ps, null);
        }
        return rs;
    }
    
    @Override
    public boolean delBill(Connection conn, int id) {
        PreparedStatement ps = null;
        boolean rs = true;
        try {
            String sql = "delete from smbms_bill where id=?";
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
    public boolean updateBill(Connection conn, Bill bill) {
        PreparedStatement ps = null;
        boolean rs = true;
        try {
            String sql = "update smbms_bill set billCode=?,productName=?,productDesc=?,productUnit=?,productCount=?,totalPrice=?,isPayment=?,modifyBy=?,modifyDate=?,providerId=? where id=?";
            List<Object> params = new ArrayList<>();
            getFields(bill, params);
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
    public Bill getBillById(Connection conn, int id) {
        Bill bill = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select b.*,p.proName from smbms_bill b,smbms_provider p where b.providerId=p.id and b.id=?";
            Object[] params = {id};
            rs = BaseDao.executeQuery(conn, ps, sql, params);
            if (rs.next()) {
                bill = getBill(rs);
                bill.setProviderName(rs.getString("proName"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(null, ps, rs);
        }
        return bill;
    }
    
    private void getFields(Bill bill, List<Object> params) {
        Field[] declaredFields = bill.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            try {
                Object value = declaredField.get(bill);
                if (Objects.nonNull(value))
                    params.add(value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    
    private Bill getBill(ResultSet rs) {
        Bill bill = new Bill();
        try {
            bill.setId(rs.getInt("id"));
            bill.setBillCode(rs.getString("billCode"));
            bill.setProductName(rs.getString("productName"));
            bill.setProductDesc(rs.getString("productDesc"));
            bill.setProductUnit(rs.getString("productUnit"));
            bill.setProductCount(rs.getBigDecimal("productCount"));
            bill.setTotalPrice(rs.getBigDecimal("totalPrice"));
            bill.setIsPayment(rs.getInt("isPayment"));
            bill.setCreatedBy( rs.getInt("createdBy"));
            bill.setCreationDate(rs.getTimestamp("CreationDate"));
            bill.setModifyBy(rs.getInt("modifyBy"));
            bill.setModifyDate(rs.getTimestamp("modifyDate"));
            bill.setProviderId(rs.getInt("providerId"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return bill;
    }
    
    // 动态添加sql和参数
    private ArrayList<Object> autoAddSqlAndParams(String productName, int providerId, int isPayment, StringBuilder sql) {
        ArrayList<Object> params = new ArrayList<>();
        if (!StringUtils.isNullOrEmpty(productName)) {
            sql.append(" and productName like ?");
            params.add("%" + productName + "%");
        }
        if (providerId >0){
            sql.append(" and providerId =?");
            params.add(providerId);
        }
        if (isPayment >0){
            sql.append(" and isPayment =?");
            params.add(isPayment);
        }
        return params;
    }
}
