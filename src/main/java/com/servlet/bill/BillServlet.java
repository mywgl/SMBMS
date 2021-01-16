package com.servlet.bill;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;
import com.pojo.Bill;
import com.pojo.Provider;
import com.pojo.User;
import com.pojo.vo.Result;
import com.service.bill.BillService;
import com.service.bill.BillServiceImp;
import com.util.Constants;
import com.util.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description
 * @Date 2021/1/16 13:55
 * @Version 1.0
 */
@WebServlet("/jsp/bill.do")
public class BillServlet extends HttpServlet {
    private final BillService billService = new BillServiceImp();
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if ("query".equals(method)) {
            queryBillList(req, resp);
        } else if ("delBill".equals(method)) {
            delBill(req, resp);
        } else if ("view".equals(method)) {
            billView(req, resp);
        } else if ("modify".equals(method)) {
            queryModifyBill(req, resp);
        } else if ("modifySave".equals(method)) {
            modifyBill(req, resp);
        } else if ("getProviderList".equals(method)) {
            getProviderList(req, resp);
        } else if ("add".equals(method)) {
            addBill(req, resp);
        }
    }
    
    private void addBill(HttpServletRequest req, HttpServletResponse resp) {
        Bill bill = new Bill();
        bill.setBillCode(req.getParameter("billCode"));
        bill.setProductName(req.getParameter("productName"));
        bill.setProductDesc(req.getParameter("productDesc"));
        bill.setProductUnit(req.getParameter("productUnit"));
        bill.setProductCount(BigDecimal.valueOf(Double.parseDouble(req.getParameter("productCount"))));
        bill.setTotalPrice(BigDecimal.valueOf(Double.parseDouble(req.getParameter("totalPrice"))));
        bill.setProviderId(Integer.parseInt(req.getParameter("providerId")));
        bill.setIsPayment(Integer.parseInt(req.getParameter("isPayment")));
        User user = (User) req.getSession().getAttribute(Constants.USER_SESSION);
        bill.setCreatedBy(user.getUserRole());
        bill.setCreationDate(new Date());
        
        boolean rs = billService.addBill(bill);
        if (rs) {
            queryBillList(req, resp);
        } else {
            try {
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            } catch (IOException | ServletException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void getProviderList(HttpServletRequest req, HttpServletResponse resp) {
        List<Provider> providerList = billService.getProviderList();
        List<Result> results = new ArrayList<>();
        for (Provider provider : providerList) {
            Result result = new Result();
            result.setId(provider.getId());
            result.setProName(provider.getProName());
            results.add(result);
        }
        respJsonMessage(resp, null, results);
    }
    
    private void modifyBill(HttpServletRequest req, HttpServletResponse resp) {
        Bill bill = new Bill();
        bill.setId(Integer.parseInt(req.getParameter("id")));
        bill.setBillCode(req.getParameter("billCode"));
        bill.setProductDesc(req.getParameter("productDesc"));
        bill.setProductName(req.getParameter("productName"));
        bill.setProductUnit(req.getParameter("productUnit"));
        bill.setProductCount(BigDecimal.valueOf(Double.parseDouble(req.getParameter("productCount"))));
        bill.setTotalPrice(BigDecimal.valueOf(Double.parseDouble(req.getParameter("totalPrice"))));
        bill.setProviderId(Integer.parseInt(req.getParameter("providerId")));
        bill.setIsPayment(Integer.parseInt(req.getParameter("isPayment")));
        User user = (User) req.getSession().getAttribute(Constants.USER_SESSION);
        bill.setModifyBy(user.getUserRole());
        bill.setModifyDate(new Date());
        boolean rs = billService.updateBill(bill);
        if (rs) {
            queryBillList(req, resp);
        } else {
            try {
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void queryModifyBill(HttpServletRequest req, HttpServletResponse resp) {
        String billId = req.getParameter("billid");
        Bill bill = billService.getBillById(Integer.parseInt(billId));
        req.setAttribute("bill", bill);
        try {
            req.getRequestDispatcher("billmodify.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private void billView(HttpServletRequest req, HttpServletResponse resp) {
        String billId = req.getParameter("billid");
        Bill bill = billService.getBillById(Integer.parseInt(billId));
        req.setAttribute("bill", bill);
        try {
            req.getRequestDispatcher("billview.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private void delBill(HttpServletRequest req, HttpServletResponse resp) {
        String billId = req.getParameter("billid");
        if (!StringUtils.isNullOrEmpty(billId)) {
            int id = Integer.parseInt(billId);
            Bill bill = billService.getBillById(id);
            Map<String, Object> map = new HashMap<>();
            if (Objects.nonNull(bill)) {
                boolean rs = billService.delBill(id);
                map.put("delResult", rs);
            } else
                map.put("delResult", "notExist");
            respJsonMessage(resp, map, null);
        }
    }
    
    private void queryBillList(HttpServletRequest req, HttpServletResponse resp) {
        String productName = req.getParameter("queryProductName");
        String queryProviderId = req.getParameter("queryProviderId");
        String queryIsPayment = req.getParameter("queryIsPayment");
        String pageIndex = req.getParameter("pageIndex");
        int providerId = 0;
        int isPayment = 0;
        int currentPage = 1;
        int pageSize = 5;
        if (!StringUtils.isNullOrEmpty(queryProviderId)) {
            providerId = Integer.parseInt(queryProviderId);
        }
        
        if (!StringUtils.isNullOrEmpty(queryIsPayment)) {
            isPayment = Integer.parseInt(queryIsPayment);
        }
        
        if (!StringUtils.isNullOrEmpty(pageIndex)) {
            currentPage = Integer.parseInt(pageIndex);
        }
        
        int totalCount = billService.getCountTotal(productName, providerId, isPayment);
        PageSupport page = new PageSupport();
        page.setTotalCount(totalCount);
        page.setCurrentPageNo(currentPage);
        page.setPageSize(pageSize);
        int totalPageCount = page.getTotalPageCount();
        if (currentPage < 1) {
            currentPage = 1;
        } else if (currentPage > totalPageCount) {
            currentPage = totalPageCount;
        }
        List<Bill> billList = billService.getAllBill(productName, providerId, isPayment, page.getCurrentPageNo(), pageSize);
        List<Provider> providerList = billService.getProviderList();
        req.setAttribute("queryProductName", productName);
        req.setAttribute("providerList", providerList);
        req.setAttribute("queryProviderId", providerId);
        req.setAttribute("queryIsPayment", isPayment);
        req.setAttribute("billList", billList);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", currentPage);
        try {
            req.getRequestDispatcher("billlist.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private void respJsonMessage(HttpServletResponse resp, Map<?, ?> mapResult, List listResult) {
        try {
            resp.setContentType("application/json;charset=utf-8");
            PrintWriter out = resp.getWriter();
            if (Objects.nonNull(mapResult))
                out.println(JSONObject.toJSONString(mapResult));
            else
                out.println(JSONArray.toJSONString(listResult));
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
