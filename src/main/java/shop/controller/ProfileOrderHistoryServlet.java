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
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shop.dao.OrderDAO;
import shop.dao.ProductDAO;
import shop.model.Customer;
import shop.model.Order;
import shop.model.OrderDetails;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "ProfileOrderHistoryServlet", urlPatterns = {"/profile-order-history"})
public class ProfileOrderHistoryServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProfileOrderHistoryServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProfileOrderHistoryServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

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
        HttpSession session = request.getSession();
        Customer temp = (Customer) session.getAttribute("currentCustomer");
        OrderDAO OD = new OrderDAO();

        try {
            ArrayList<Order> fullOrderList = OD.getOrderById(temp.getCustomerId());
             //check order is reviewed
            ProductDAO pd = new ProductDAO();
            for (Order order : fullOrderList) {
                
                if (pd.isOrderReviewed(order.getOrderId(), temp.getCustomerId())) {
                    order.setIsReviewed(true);
                }
            }

            // Lấy tham số trang từ request
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
            int totalOrders = fullOrderList.size();
            int totalPages = (totalOrders == 0) ? 1 : (int) Math.ceil((double) totalOrders / itemsPerPage);
            if (currentPage < 1) {
                currentPage = 1;
            }
            if (currentPage > totalPages) {
                currentPage = totalPages;
            }
            int startIndex = (currentPage - 1) * itemsPerPage;
            int endIndex = Math.min(startIndex + itemsPerPage, totalOrders);

            List<Order> paginatedOrders = (List<Order>) ((startIndex < endIndex)
                    ? fullOrderList.subList(startIndex, endIndex)
                    : new ArrayList<>());
            request.setAttribute("order", paginatedOrders);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);

           
            request.getRequestDispatcher("/WEB-INF/home/profile-order-history.jsp")
                    .forward(request, response);

        } catch (SQLException ex) {
            Logger.getLogger(ProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        String action = request.getParameter("action");

        if (action.equals("details")) {
            OrderDAO OD = new OrderDAO();
            int orderid = Integer.parseInt(request.getParameter("order_id"));

            try {
                ArrayList<OrderDetails> orderDetails = OD.getOrderDetailForCus(orderid);
                Order order = OD.getOneOrder(orderid);
                request.setAttribute("order", order);
                request.setAttribute("orderdetails", orderDetails);
                request.getRequestDispatcher("/WEB-INF/home/profile-order-details.jsp").forward(request, response);

            } catch (SQLException ex) {
                Logger.getLogger(ProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action.equals("review")) {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            int value = 0;
            String ratingStr = request.getParameter("rating");
            if (ratingStr == null || ratingStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/profile");
                return;
            }
            value = Integer.parseInt(ratingStr);

            HttpSession session = request.getSession();
            Customer temp = (Customer) session.getAttribute("currentCustomer");
            OrderDAO OD = new OrderDAO();
            ProductDAO PD = new ProductDAO();
            try {
                int[] productId = OD.getProIdByOrderId(orderId);
                if (PD.writeReviewIntoDb(productId, temp.getCustomerId(), value, orderId) != 0) {
                    response.sendRedirect(request.getContextPath() + "/profile");

                }
            } catch (SQLException ex) {
                Logger.getLogger(ProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
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
