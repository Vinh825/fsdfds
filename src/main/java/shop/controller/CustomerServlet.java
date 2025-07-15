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
import shop.dao.CustomerDAO;
import shop.dao.OrderDAO;
import shop.model.Customer;
import shop.model.Order;

/**
 *
 * @author Le Anh Khoa - CE190449
 */
@WebServlet(name = "CustomerServlet", urlPatterns = {"/manage-customers"})
public class CustomerServlet extends HttpServlet {

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
            // Get message
            HttpSession session = request.getSession();
            String msg = (String) session.getAttribute("message");
            if (msg != null) {
                request.setAttribute("message", msg);
                session.removeAttribute("message");
            }

            CustomerDAO cDAO = new CustomerDAO();
            int currentPage = 1;
            int pageSize = 6;

            if (request.getParameter("page") != null) {
                try {
                    currentPage = Integer.parseInt(request.getParameter("page"));
                } catch (NumberFormatException e) {
                    currentPage = 1;
                }
            }

            String search = request.getParameter("searchName");
            List<Customer> paginatedList;
            int totalCustomers;

            if (search != null && !search.trim().isEmpty()) {
                paginatedList = cDAO.getPaginatedCustomersBySearch(search.trim(), currentPage, pageSize);
                totalCustomers = cDAO.getTotalCustomerCountBySearch(search.trim());
            } else {
                paginatedList = cDAO.getPaginatedCustomerList(currentPage, pageSize);
                totalCustomers = cDAO.getTotalCustomerCount();
            }

            int totalPages = (int) Math.ceil((double) totalCustomers / pageSize);

            request.setAttribute("paginatedList", paginatedList);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("searchName", search);

            request.getRequestDispatcher("/WEB-INF/dashboard/customer-list.jsp").forward(request, response);
        } else if (view.equals("edit")) {
            int id = Integer.parseInt(request.getParameter("id"));

            CustomerDAO cDAO = new CustomerDAO();
            Customer thisCustomer = cDAO.getAccountById(id);

            request.setAttribute("thisCustomer", thisCustomer);
            request.getRequestDispatcher("/WEB-INF/dashboard/customer-edit.jsp").forward(request, response);
        } else if (view.equals("delete")) {
            int id = Integer.parseInt(request.getParameter("id"));

            CustomerDAO cDAO = new CustomerDAO();
            Customer thisCustomer = cDAO.getAccountById(id);

            request.setAttribute("thisCustomer", thisCustomer);
            request.getRequestDispatcher("/WEB-INF/dashboard/customer-delete.jsp").forward(request, response);
        } else if (view.equals("details")) {
            int id = Integer.parseInt(request.getParameter("id"));

            CustomerDAO cDAO = new CustomerDAO();
            Customer thisCustomer = cDAO.getAccountById(id);

            request.setAttribute("thisCustomer", thisCustomer);
            request.getRequestDispatcher("/WEB-INF/dashboard/customer-details.jsp").forward(request, response);
        }else if (view.equals("history")) {
            int id = Integer.parseInt(request.getParameter("id"));
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
                ArrayList<Order> orders = OD.getOrderById(id);
                int totalOrders = orders.size();

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
                        ? orders.subList(startIndex, endIndex)
                        : new ArrayList<>());

                request.setAttribute("orderlist", paginatedOrders);
                request.setAttribute("currentPage", currentPage);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("id", id); // giữ lại id để phân trang tiếp
                request.getRequestDispatcher("/WEB-INF/dashboard/customer-order-history.jsp").forward(request, response);
            } catch (SQLException ex) {
                Logger.getLogger(OrderServlet.class.getName()).log(Level.SEVERE, null, ex);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi truy xuất đơn hàng.");
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
        String action = request.getParameter("action");
        if (action.equals("edit")) {
            int id = Integer.parseInt(request.getParameter("id"));
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            CustomerDAO cDAO = new CustomerDAO();
            Customer thisCustomer = cDAO.getAccountById(id);

            if (thisCustomer != null) {
                if (!cDAO.isUsernameOrEmailTakenByOthers(id, username, email)) {
                    if (cDAO.updateCustomer(new Customer(id, username, email, fullName, phone, address)) > 0) {
                        Customer newCustomer = cDAO.getAccountById(id);
                        request.setAttribute("thisCustomer", newCustomer);
                        request.setAttribute("successMessage", "Update customer successfully!");
                        request.getRequestDispatcher("/WEB-INF/dashboard/customer-edit.jsp").forward(request, response);
                    } else {
                        request.setAttribute("thisCustomer", thisCustomer);
                        request.setAttribute("errorMessage", "Update Customer to the database failed");
                        request.getRequestDispatcher("/WEB-INF/dashboard/customer-edit.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("thisCustomer", thisCustomer);
                    request.setAttribute("errorMessage", "Username or Email already exists.");
                    request.getRequestDispatcher("/WEB-INF/dashboard/customer-edit.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("errorMessage", "This Customer Id does not exist.");
                request.getRequestDispatcher("/WEB-INF/dashboard/customer-delete.jsp").forward(request, response);
            }
        } else if (action.equals("delete")) {
        
            int id = Integer.parseInt(request.getParameter("id"));

            CustomerDAO cDAO = new CustomerDAO();
            Customer thisCustomer = cDAO.getAccountById(id);

            if (thisCustomer != null) {
                if (cDAO.deleteCustomer(id) > 0) {
                    response.sendRedirect(request.getContextPath() + "/manage-customers");
                } else {
                    request.setAttribute("thisCustomer", thisCustomer);
                    request.setAttribute("errorMessage", "Delete Customer from the database failed");
                    request.getRequestDispatcher("/WEB-INF/dashboard/customer-delete.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("errorMessage", "This Customer Id does not exist.");
                request.getRequestDispatcher("/WEB-INF/dashboard/customer-delete.jsp").forward(request, response);
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
