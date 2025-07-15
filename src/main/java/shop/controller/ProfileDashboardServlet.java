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
import java.sql.Date;
import shop.dao.StaffDAO;
import shop.model.Staff;
import shop.util.PasswordUtils;

/**
 *
 * @author CE190449 - Le Anh Khoa
 */
@WebServlet(name = "ProfileDashboardServlet", urlPatterns = {"/profile-dashboard"})
public class ProfileDashboardServlet extends HttpServlet {

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
        request.getRequestDispatcher("/WEB-INF/dashboard/profile-dashboard.jsp")
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
        String action = request.getParameter("action");
        if (action.equals("updateProfile")) {
            int id = Integer.parseInt(request.getParameter("id"));
            String email = request.getParameter("email");
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            String genderParam = request.getParameter("gender");
            String gender = (genderParam == null || genderParam.isEmpty()) ? null : genderParam;
            String avatarUrl = request.getParameter("avatarUrl");
            String dateOfBirthParameter = request.getParameter("dateOfBirth");
            Date dateOfBirth = null;
            if (dateOfBirthParameter != null && !dateOfBirthParameter.isEmpty()) {
                dateOfBirth = Date.valueOf(dateOfBirthParameter);
            }

            HttpSession session = request.getSession(false);
            Staff currentEmployee = (session != null) ? (Staff) session.getAttribute("currentEmployee") : null;

            StaffDAO sDAO = new StaffDAO();

            if (currentEmployee != null) {
                if (!sDAO.isEmailTakenByOthers(email, id)) {
                    if (sDAO.updateProfileAdmin(new Staff(id, fullName, email, gender, phone, dateOfBirth, avatarUrl)) > 0) {
                        Staff newEmployee = sDAO.getOneById(id);
                        session.setAttribute("currentEmployee", newEmployee); // Session updated
                        request.setAttribute("message", "Update profile successfully!");
                        request.getRequestDispatcher("/WEB-INF/dashboard/profile-dashboard.jsp").forward(request, response);
                    } else {
                        request.setAttribute("currentEmployee", currentEmployee);
                        request.setAttribute("message", "Update profile unsucessfully");
                        request.getRequestDispatcher("/WEB-INF/dashboard/profile-dashboard.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("currentEmployee", currentEmployee);
                    request.setAttribute("message", "Username or Email already exists.");
                    request.getRequestDispatcher("/WEB-INF/dashboard/profile-dashboard.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("message", "Your session has expired. Please login again.");
                request.getRequestDispatcher("/WEB-INF/home/login-dashboard.jsp").forward(request, response);
            }
        } else if (action.equals("updatePassword")) {
            int id = Integer.parseInt(request.getParameter("id"));
            String oldpass = request.getParameter("oldpass");
            String newpass = request.getParameter("newpass");

            HttpSession session = request.getSession(false);
            Staff currentEmployee = (session != null) ? (Staff) session.getAttribute("currentEmployee") : null;

            StaffDAO sDAO = new StaffDAO();

            if (currentEmployee != null) {
                // Check old pass
                boolean isOldPasswordMatched = PasswordUtils.checkPassword(oldpass, currentEmployee.getPasswordHash());

                if (isOldPasswordMatched) {
                    // Check new pass already exist or not
                    boolean isNewPasswordSame = PasswordUtils.checkPassword(newpass, currentEmployee.getPasswordHash());

                    if (!isNewPasswordSame) {
                        String hashedPassword = PasswordUtils.hashPassword(newpass);
                        // Update customer password
                        if (sDAO.updateAdminPassword(new Staff(currentEmployee.getEmail(), hashedPassword)) > 0) {
                            request.setAttribute("message", "Password update successfully!");
                            request.getRequestDispatcher("/WEB-INF/dashboard/profile-dashboard.jsp").forward(request, response);
                        } else {
                            request.setAttribute("message", "Something went wrong. Please try again.");
                            request.getRequestDispatcher("/WEB-INF/dashboard/profile-dashboard.jsp").forward(request, response);
                        }
                    } else {
                        request.setAttribute("message", "Your new password must be different from your current password.");
                        request.getRequestDispatcher("/WEB-INF/dashboard/profile-dashboard.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("message", "Your old password does not match.");
                    request.getRequestDispatcher("/WEB-INF/dashboard/profile-dashboard.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("message", "Your session has expired. Please login again.");
                request.getRequestDispatcher("/WEB-INF/home/login-dashboard.jsp").forward(request, response);
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
