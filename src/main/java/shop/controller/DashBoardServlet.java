/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package shop.controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import shop.dao.OrderDAO;
import shop.model.Order;
import shop.model.Product;

/**
 *
 * @author HoangSang
 */
@WebServlet(name="DashBoardServlet", urlPatterns={"/dashboard"})
public class DashBoardServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DashBoardServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DashBoardServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
         String period = request.getParameter("period");
        if (period == null || period.trim().isEmpty()) {
            period = "year";
        }
        OrderDAO dao = new OrderDAO();
        Gson gson = new Gson();
        
        // 1. Lấy các số liệu thống kê động theo period
        Map<String, Object> summaryStats = dao.getSummaryStats(period);
        request.setAttribute("summaryStats", summaryStats);
        
        // 2. Lấy các số liệu thống kê cố định
        request.setAttribute("totalStock", dao.getTotalProductsInStock());
        request.setAttribute("totalCustomers", dao.getTotalCustomers());

        // 3. Lấy dữ liệu cho biểu đồ doanh thu
        Map<String, Object> trendData = dao.getRevenueTrend(period);
        request.setAttribute("trendLabelsJson", gson.toJson(trendData.get("labels")));
        request.setAttribute("trendDataJson", gson.toJson(trendData.get("data")));
        
        // 4. Lấy dữ liệu cho biểu đồ tồn kho
        Map<String, List<?>> stockByCategoryData = dao.getStockByCategory();
        request.setAttribute("stockLabelsJson", gson.toJson(stockByCategoryData.get("labels")));
        request.setAttribute("stockDataJson", gson.toJson(stockByCategoryData.get("data")));
        
        // 5. Lấy danh sách Top 5 sản phẩm bán chạy
        List<Product> topSellingProducts = dao.getTopSellingProductsDetails(5);
        request.setAttribute("topSellingProducts", topSellingProducts);
        
        request.setAttribute("currentPeriod", period);
        request.getRequestDispatcher("/WEB-INF/dashboard/dashboard-list.jsp").forward(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
    