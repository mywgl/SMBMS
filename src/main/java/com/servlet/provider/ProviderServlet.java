package com.servlet.provider;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;
import com.pojo.Provider;
import com.pojo.User;
import com.service.provder.ProviderService;
import com.service.provder.ProviderServiceImp;
import com.util.Constants;
import com.util.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @Description
 * @Date 2021/1/14 21:43
 * @Version 1.0
 */
@WebServlet("/jsp/provider.do")
public class ProviderServlet extends HttpServlet {
    private final ProviderService providerService = new ProviderServiceImp();
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if ("query".equals(method)) {
            queryProvider(req, resp);
        } else if ("delProvider".equals(method)) {
            delProvider(req, resp);
        } else if ("view".equals(method)) {
            ProviderView(req, resp);
        } else if ("modify".equals(method)) {
            queryModifyPro(req, resp);
        } else if ("modifyExe".equals(method)) {
            modifyPro(req, resp);
        } else if ("add".equals(method)) {
            addProvider(req, resp);
        }
    }
    
    private void addProvider(HttpServletRequest req, HttpServletResponse resp) {
        Provider provider = new Provider();
        provider.setProCode(req.getParameter("proCode"));
        provider.setProName(req.getParameter("proName"));
        provider.setProContact(req.getParameter("proContact"));
        provider.setProPhone(req.getParameter("proPhone"));
        provider.setProAddress(req.getParameter("proAddress"));
        provider.setProFax(req.getParameter("proFax"));
        provider.setProDesc(req.getParameter("proDesc"));
        User user = (User) req.getSession().getAttribute(Constants.USER_SESSION);
        provider.setCreatedBy(user.getUserRole());
        provider.setCreationDate(new Date());
        
        boolean rs = providerService.addProvider(provider);
        if (rs) {
            queryProvider(req, resp);
        } else {
            try {
                resp.sendRedirect(req.getContextPath() + "/error.jsp");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }
    
    private void modifyPro(HttpServletRequest req, HttpServletResponse resp) {
        Provider provider = new Provider();
        provider.setId(Integer.parseInt(req.getParameter("proId")));
        provider.setProCode(req.getParameter("proCode"));
        provider.setProName(req.getParameter("proName"));
        provider.setProContact(req.getParameter("proContact"));
        provider.setProPhone(req.getParameter("proPhone"));
        provider.setProAddress(req.getParameter("proAddress"));
        provider.setProFax(req.getParameter("proFax"));
        provider.setProDesc(req.getParameter("proDesc"));
        User user = (User) req.getSession().getAttribute(Constants.USER_SESSION);
        provider.setModifyBy(user.getUserRole());
        provider.setModifyDate(new Date());
        boolean rs = providerService.updateProvider(provider);
        if (rs) {
            queryProvider(req, resp);
        } else {
            try {
                resp.sendRedirect(req.getContextPath() + "/error.jsp");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    // 查询要修改数据展示给前端
    private void queryModifyPro(HttpServletRequest req, HttpServletResponse resp) {
        String proId = req.getParameter("proid");
        Provider provider = providerService.getProviderById(Integer.parseInt(proId));
        req.setAttribute("provider", provider);
        try {
            req.getRequestDispatcher("providermodify.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private void ProviderView(HttpServletRequest req, HttpServletResponse resp) {
        String proId = req.getParameter("proid");
        Provider provider = providerService.getProviderById(Integer.parseInt(proId));
        req.setAttribute("provider", provider);
        try {
            req.getRequestDispatcher("providerview.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private void delProvider(HttpServletRequest req, HttpServletResponse resp) {
        String proId = req.getParameter("proId");
        if (!StringUtils.isNullOrEmpty(proId)) {
            int id = Integer.parseInt(proId);
            Provider provider = providerService.getProviderById(id);
            Map<String, Object> map = new HashMap<>();
            if (Objects.nonNull(provider)) {
                boolean rs = providerService.delProvider(id);
                map.put("delResult", rs);
            } else
                map.put("delResult", "notExist");
            respJsonMessage(resp, map, null);
        }
    }
    
    private void queryProvider(HttpServletRequest req, HttpServletResponse resp) {
        String queryProCode = req.getParameter("queryProCode");
        String queryProName = req.getParameter("queryProName");
        String pageIndex = req.getParameter("pageIndex");
        int currentPage = 1;
        int pageSize = 8;
        
        if (!StringUtils.isNullOrEmpty(pageIndex)) {
            currentPage = Integer.parseInt(pageIndex);
        }
        
        int countTotal = providerService.getCountTotal(queryProCode, queryProName);
        PageSupport page = new PageSupport();
        page.setPageSize(pageSize);
        page.setCurrentPageNo(currentPage);
        page.setTotalCount(countTotal);
        int totalPageCount = page.getTotalPageCount();
        
        if (currentPage < 1) {
            currentPage = 1;
        } else if (currentPage > totalPageCount) {
            currentPage = totalPageCount;
        }
        
        List<Provider> allProvider = providerService.getAllProvider(queryProCode, queryProName, page.getCurrentPageNo(), pageSize);
        req.setAttribute("queryProCode", queryProCode);
        req.setAttribute("queryProName", queryProName);
        req.setAttribute("providerList", allProvider);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("totalCount", countTotal);
        req.setAttribute("currentPageNo", currentPage);
        try {
            req.getRequestDispatcher("providerlist.jsp").forward(req, resp);
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
