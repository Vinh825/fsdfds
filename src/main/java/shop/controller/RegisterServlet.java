/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shop.dao.CustomerDAO;
import shop.util.PasswordUtils;
import shop.model.Customer;

/**
 *
 * @author CE190449 - Le Anh Khoa
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

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
        request.getRequestDispatcher("/WEB-INF/home/register.jsp").forward(request, response);
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

        String fullName = request.getParameter("fullName");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String avatarUrl = request.getContextPath() + "/assets/img/avatar/avatar1.png";

        CustomerDAO cDAO = new CustomerDAO();
        String hashedPassword = PasswordUtils.hashPassword(password);

        // If email already exists
        if (!cDAO.isUsernameOrEmailTaken(username, email)) {
            // Register the customer
            if (cDAO.createCustomer(new Customer(fullName, username, email, hashedPassword, avatarUrl)) > 0) {
                // Success: redirect to login with success message in session (flash-style)
                request.setAttribute("fullName", fullName);
                request.setAttribute("username", username);
                request.setAttribute("email", email);
                request.setAttribute("password", password);
                request.setAttribute("confirmPassword", confirmPassword);
                request.setAttribute("message", "Registration successful! Please log in.");
                request.getRequestDispatcher("/WEB-INF/home/login.jsp").forward(request, response);
            } else {
                // DB insert failed
                request.setAttribute("fullName", fullName);
                request.setAttribute("username", username);
                request.setAttribute("email", email);
                request.setAttribute("password", password);
                request.setAttribute("confirmPassword", confirmPassword);
                request.setAttribute("message", "We couldn't complete your registration at the moment. Please try again later.");
                request.getRequestDispatcher("/WEB-INF/home/register.jsp").forward(request, response);
            }
        } else {
            // Email Or Username exists
            request.setAttribute("fullName", fullName);
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("password", password);
            request.setAttribute("confirmPassword", confirmPassword);
            request.setAttribute("message", "The email or username already exists. Please try again.");
            request.getRequestDispatcher("/WEB-INF/home/register.jsp").forward(request, response);
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
