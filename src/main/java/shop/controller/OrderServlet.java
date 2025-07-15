/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shop.dao.OrderDAO;
import shop.model.Customer;
import shop.model.Order;
import shop.model.OrderDetails;
import shop.model.Product;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/manage-orders"})
public class OrderServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String view = request.getParameter("view");
        if (view == null || view.isEmpty() || view.equals("list")) {
            OrderDAO OD = new OrderDAO();
            String pageParam = request.getParameter("page");
            int currentPage = 1;
            if (pageParam != null) {
                try {
                    currentPage = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    currentPage = 1;
                }
            }

            int itemsPerPage = 15;

            try {
                ArrayList<Order> allOrders = OD.getallOrder();
                int totalOrders = allOrders.size();

                int totalPages = (totalOrders == 0) ? 1 : (int) Math.ceil((double) totalOrders / itemsPerPage);

                // Đảm bảo currentPage nằm trong phạm vi hợp lệ
                if (currentPage < 1) {
                    currentPage = 1;
                }
                if (currentPage > totalPages) {
                    currentPage = totalPages;
                }

                int startIndex = (currentPage - 1) * itemsPerPage;
                int endIndex = Math.min(startIndex + itemsPerPage, totalOrders);

                List<Order> paginatedOrders = (List<Order>) ((startIndex < endIndex)
                        ? allOrders.subList(startIndex, endIndex)
                        : new ArrayList<>());

                request.setAttribute("orderlist", paginatedOrders);
                request.setAttribute("currentPage", currentPage);
                request.setAttribute("totalPages", totalPages);
                request.getRequestDispatcher("/WEB-INF/dashboard/order-list.jsp").forward(request, response);
            } catch (SQLException ex) {
                Logger.getLogger(OrderServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (view.equals("details")) {
            OrderDAO OD = new OrderDAO();
            String o_id = request.getParameter("order_id");
            int order_id = Integer.parseInt(o_id);
            try {
                ArrayList<OrderDetails> orderDetails = OD.getOrderDetail(order_id);
                Order order = OD.getOneOrder(order_id);
                request.setAttribute("order", order);
                request.setAttribute("orderdetails", orderDetails);
                request.getRequestDispatcher("/WEB-INF/dashboard/order-details.jsp").forward(request, response);

            } catch (SQLException ex) {
                Logger.getLogger(OrderServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        String action = request.getParameter("action");
        if (action.equals("update")) {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String status = request.getParameter("status");
            OrderDAO OD = new OrderDAO();
            try {
                if (OD.updateOrderStatus(status, orderId) == 1) {
                    response.sendRedirect(request.getContextPath() + "/manage-orders");

                }

            } catch (SQLException ex) {
                Logger.getLogger(OrderServlet.class.getName()).log(Level.SEVERE, null, ex);

            }
        } else if (action.equals("search")) {
    String cusName = request.getParameter("customer_name");
    OrderDAO OD = new OrderDAO();

    String pageParam = request.getParameter("page");
    int currentPage = 1;
    if (pageParam != null) {
        try {
            currentPage = Integer.parseInt(pageParam);
        } catch (NumberFormatException e) {
            currentPage = 1;
        }
    }

    int itemsPerPage = 15;
    try {
        ArrayList<Order> allOrders = OD.searchOrders(cusName);
        int totalOrders = allOrders.size();

        int totalPages = (totalOrders == 0) ? 1 : (int) Math.ceil((double) totalOrders / itemsPerPage);

        // Đảm bảo currentPage nằm trong phạm vi hợp lệ
        if (currentPage < 1) currentPage = 1;
        if (currentPage > totalPages) currentPage = totalPages;

        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalOrders);

        List<Order> paginatedOrders = (List<Order>) ((startIndex < endIndex)
                ? allOrders.subList(startIndex, endIndex)
                : new ArrayList<>());

        request.setAttribute("orderlist", paginatedOrders);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("customer_name", cusName); // để giữ giá trị khi quay lại form
        request.getRequestDispatcher("/WEB-INF/dashboard/order-list.jsp").forward(request, response);

    } catch (SQLException ex) {
        Logger.getLogger(OrderServlet.class.getName()).log(Level.SEVERE, null, ex);
    }
}

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
