/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import shop.dao.CartDAO;
import shop.dao.CustomerDAO;
import shop.util.PasswordUtils;
import shop.model.Customer;

/**
 *
 * @author CE190449 - Le Anh Khoa
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

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
        request.getRequestDispatcher("/WEB-INF/home/login.jsp").forward(request, response);
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

        // Retrieve form parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        CustomerDAO cDAO = new CustomerDAO();
        Customer customer = cDAO.getAccountByEmail(email);

        if (customer != null) {
            if (!customer.isIsDeactivated()) {
                // Check password match before setting session
                boolean isPasswordMatched = PasswordUtils.checkPassword(password, customer.getPasswordHash());
                if (isPasswordMatched) {
                    if (cDAO.updateLastLoginTime(customer) > 0) {
                        HttpSession session = request.getSession(true);
                        session.setAttribute("currentCustomer", customer); // Session CurrentCustomer

                        CartDAO cartDAO = new CartDAO();
                        int customerId = customer.getCustomerId();
                        int cartCount = cartDAO.countCartItems(customerId);
                        session.setAttribute("cartCount", cartCount);

                        // Redirect to the home page upon successful login
                        response.sendRedirect(request.getContextPath() + "/home");
                    } else {
                        // If password doesn't match, set error message and forward to login page
                        request.setAttribute("email", email);
                        request.setAttribute("password", password);
                        request.setAttribute("message", "We're unable to update your login time at the moment. Please try again later.");
                        request.getRequestDispatcher("/WEB-INF/home/login.jsp").forward(request, response);
                    }
                } else {
                    // If password doesn't match, set error message and forward to login page
                    request.setAttribute("email", email);
                    request.setAttribute("password", password);
                    request.setAttribute("message", "Email or password is incorrect. Try again.");
                    request.getRequestDispatcher("/WEB-INF/home/login.jsp").forward(request, response);
                }
            } else {
                // If account is deactivated, set error message and forward to login page
                request.setAttribute("email", email);
                request.setAttribute("password", password);
                request.setAttribute("message", "Your account is locked. Please contact us for more information.");
                request.getRequestDispatcher("/WEB-INF/home/login.jsp").forward(request, response);
            }
        } else {
            // If email doesn't exist, set error message and forward to login page
            request.setAttribute("email", email);
            request.setAttribute("password", password);
            request.setAttribute("message", "Email doesn't exist.");
            request.getRequestDispatcher("/WEB-INF/home/login.jsp").forward(request, response);
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
