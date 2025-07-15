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
import shop.dao.CustomerDAO;
import shop.model.Customer;
import shop.util.PasswordUtils;

/**
 *
 * @author Le Anh Khoa - CE190449
 */
@WebServlet(name = "ResetPasswordServlet", urlPatterns = {"/reset-password"})
public class ResetPasswordServlet extends HttpServlet {

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
        request.getRequestDispatcher("/WEB-INF/home/reset-password.jsp")
                .forward(request, response);
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

        HttpSession session = request.getSession(false);
        Customer forgotCustomer = (session != null) ? (Customer) session.getAttribute("currentForgotCustomer") : null;

        if (forgotCustomer != null) {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");

            CustomerDAO cDAO = new CustomerDAO();
            Customer customer = cDAO.getAccountByEmail(email);

            boolean isNewPasswordExists = PasswordUtils.checkPassword(password, customer.getPasswordHash());
            // Password is different from customer current password
            if (!isNewPasswordExists) {
                String hashedPassword = PasswordUtils.hashPassword(password);
                // Update customer password
                if (cDAO.updateCustomerPassword(new Customer(email, hashedPassword)) > 0) {
                    // Invalidate session after password reset
                    if (session != null) {
                        session.removeAttribute("currentForgotCustomer");
                    }
                    request.setAttribute("message", "Password reset successfully! Please log in.");
                    request.getRequestDispatcher("/WEB-INF/home/login.jsp").forward(request, response);
                } else {
                    request.setAttribute("email", email);
                    request.setAttribute("password", password);
                    request.setAttribute("confirmPassword", confirmPassword);
                    request.setAttribute("message", "Something went wrong. Please try again.");
                    request.getRequestDispatcher("/WEB-INF/home/reset-password.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("email", email);
                request.setAttribute("password", password);
                request.setAttribute("confirmPassword", confirmPassword);
                request.setAttribute("message", "Your new password must be different from your current password.");
                request.getRequestDispatcher("/WEB-INF/home/reset-password.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("message", "Your session has expired. Please go through the OTP process again.");
            request.getRequestDispatcher("/WEB-INF/home/forgot-password.jsp").forward(request, response);
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
