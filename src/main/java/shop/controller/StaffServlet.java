/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import shop.dao.StaffDAO;
import shop.model.Staff;
import shop.util.PasswordUtils;

/**
 *
 * @author VICTUS
 */
@WebServlet(name = "StaffServlet", urlPatterns = {"/manage-staffs"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class StaffServlet extends HttpServlet {

    public static final int PAGE_SIZE = 10;

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
            int page = 1;
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }

            StaffDAO sDAO = new StaffDAO();
            ArrayList<Staff> sList = sDAO.getList(page);
            request.setAttribute("s", sList);

            int numOfPage = (int) Math.ceil((double) sDAO.countStaffs() / PAGE_SIZE);
            request.setAttribute("numOfPage", numOfPage);

            request.setAttribute("s", sList);
            request.getRequestDispatcher("/WEB-INF/dashboard/staff-list.jsp").forward(request, response);

        } else if (view.equals("create")) {
            request.getRequestDispatcher("/WEB-INF/dashboard/staff-create.jsp").forward(request, response);

        } else if (view.equals("edit")) {
            try {
                String idParam = request.getParameter("id");

                int id = Integer.parseInt(idParam);
                StaffDAO sDAO = new StaffDAO();
                Staff oneStaff = sDAO.getOneById(id);

                if (oneStaff == null) {
                    response.sendRedirect("/WEB-INF/dashboard/staff-list.jsp");
                    return;
                }

                request.setAttribute("s", oneStaff);
                request.getRequestDispatcher("/WEB-INF/dashboard/staff-edit.jsp").forward(request, response);

            } catch (NumberFormatException e) {
                System.err.println("Lỗi định dạng ID: " + e.getMessage());
                response.sendRedirect("/WEB-INF/dashboard/staff-list.jsp");
            } catch (Exception e) {
                System.err.println("Lỗi không mong muốn: " + e.getMessage());
                response.sendRedirect("/WEB-INF/dashboard/staff-list.jsp");
            }
        } else if (view.equals("delete")) {
            String idParam = request.getParameter("id");

            if (idParam == null || idParam.trim().isEmpty() || !idParam.matches("\\d+")) {
                response.sendRedirect("/WEB-INF/dashboard/staff-list.jsp");
                return;
            }

            int id = Integer.parseInt(idParam);
            StaffDAO sDAO = new StaffDAO();
            Staff oneStaff = sDAO.getOneById(id);

            if (oneStaff == null) {
                response.sendRedirect("/WEB-INF/dashboard/staff-list.jsp");
                return;
            }

            request.setAttribute("s", oneStaff);

            request.getRequestDispatcher("/WEB-INF/dashboard/staff-delete.jsp").forward(request, response);

        } else if (view.equals("details")) {
            try {
                String idParam = request.getParameter("id");

                int id = Integer.parseInt(idParam);
                StaffDAO sDAO = new StaffDAO();
                Staff oneStaff = sDAO.getOneById(id);

                if (oneStaff == null) {
                    response.sendRedirect("/WEB-INF/dashboard/staff-list.jsp");
                    return;
                }

                request.setAttribute("s", oneStaff);
                request.getRequestDispatcher("/WEB-INF/dashboard/staff-details.jsp").forward(request, response);

            } catch (NumberFormatException e) {
                System.err.println("Lỗi định dạng ID: " + e.getMessage());
                response.sendRedirect("/WEB-INF/dashboard/staff-list.jsp");
            } catch (Exception e) {
                System.err.println("Lỗi không mong muốn: " + e.getMessage());
                response.sendRedirect("/WEB-INF/dashboard/staff-list.jsp");
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
        response.setContentType("text/html;charset=UTF-8");
        String act = request.getParameter("action");

        if (act != null) {
            StaffDAO sDAO = new StaffDAO();
            switch (act) {
                case "create":
    try ( PrintWriter out = response.getWriter()) {
                    String fullName = request.getParameter("username"); 
                    String email = request.getParameter("email");
                    String password = request.getParameter("password");
                    String phone = request.getParameter("phone");
                    String role = request.getParameter("role");
                    String gender = request.getParameter("gender"); 
                    String dobStr = request.getParameter("date_of_birth");

                    Date dateOfBirth = null;
                    if (dobStr != null && !dobStr.isEmpty()) {
                        dateOfBirth = Date.valueOf(dobStr);
                    }

                    Part img = request.getPart("avatar_url");
                    String filename = Paths.get(img.getSubmittedFileName()).getFileName().toString();
                    String avatarUrl = "/CosmoraCelestica/assets/img/avatar/" + filename;

                    String realPath = request.getServletContext().getRealPath("/assets/img/avatar");

                    if (realPath == null) {
                        out.println("<h2>Error: No valid upload path found.</h2>");
                        return;
                    }

                    File uploadDir = new File(realPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }

                    File fileToSave = new File(uploadDir, filename);
                    img.write(fileToSave.getAbsolutePath());

                    String hashedPassword = PasswordUtils.hashPassword(password);

                    // ✅ Tạo Staff với fullName và gender
                    Staff staff = new Staff(fullName, email, hashedPassword, gender, phone, role, dateOfBirth, avatarUrl);
                    int isCreated = sDAO.create(staff);

                    if (isCreated == 1) {
                        response.sendRedirect(request.getContextPath() + "/manage-staffs");
                    } else {
                        out.println("<h2>Error: Can't add staff into database.</h2>");
                    }

                } catch (Exception e) {
                    response.getWriter().println("<h2>Upload failed!</h2>");
                    e.printStackTrace(response.getWriter());
                }
                break;

                case "edit":
    try ( PrintWriter out = response.getWriter()) {

                    String idParam = request.getParameter("id");
                    int id = Integer.parseInt(idParam);

                    String fullName = request.getParameter("username");
                    String email = request.getParameter("email");
                    String password = request.getParameter("password");
                    String phone = request.getParameter("phone");
                    String role = request.getParameter("role");
                    String gender = request.getParameter("gender"); 
                    String dobStr = request.getParameter("date_of_birth");

                    Date dateOfBirth = null;
                    if (dobStr != null && !dobStr.isEmpty()) {
                        dateOfBirth = Date.valueOf(dobStr);
                    }

                    // Lấy staff cũ để giữ avatar nếu người dùng không chọn ảnh mới
                    Staff oldStaff = sDAO.getOneById(id);

                    Part img = request.getPart("avatar_url");
                    String filename = Paths.get(img.getSubmittedFileName()).getFileName().toString();
                    String avatarUrl;

                    String realPath = request.getServletContext().getRealPath("/assets/img/avatar");
                    File uploadDir = new File(realPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }

                    // Nếu có file mới
                    if (filename != null && !filename.isEmpty()) {
                        File fileToSave = new File(uploadDir, filename);
                        img.write(fileToSave.getAbsolutePath());

                        avatarUrl = "/CosmoraCelestica/assets/img/avatar/" + filename;
                    } else {
                        avatarUrl = oldStaff.getAvatarUrl(); // Dùng lại avatar cũ
                    }

                    String hashedPassword = PasswordUtils.hashPassword(password);

                   
                    Staff updatedStaff = new Staff(id, fullName, email, hashedPassword, gender, phone, role, dateOfBirth, avatarUrl);

                    int isUpdate = sDAO.update(updatedStaff);
                    if (isUpdate == 1) {
                        response.sendRedirect(request.getContextPath() + "/manage-staffs");
                    } else {
                        out.println("<h2>Error: Can't update staff in database.</h2>");
                    }

                } catch (Exception e) {
                    response.getWriter().println("<h2>Upload failed!</h2>");
                    e.printStackTrace(response.getWriter());
                }
                break;

                case "delete":
                    String idParam = request.getParameter("id");
                    int id = Integer.parseInt(idParam);

                    if (sDAO.delete(id) == 1) {
                        response.sendRedirect(request.getContextPath() + "/manage-staffs");
                    }
                    break;

                case "search":

                    String keyWord = request.getParameter("keyWord");

                    ArrayList<Staff> sList;
                    try {
                        sList = sDAO.searchByName(keyWord);
                        request.setAttribute("s", sList);

                        request.getRequestDispatcher("/WEB-INF/dashboard/staff-list.jsp").forward(request, response);
                    } catch (SQLException ex) {
                        Logger.getLogger(StaffServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    break;

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
