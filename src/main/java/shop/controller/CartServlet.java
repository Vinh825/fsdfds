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
import java.util.List;
import shop.dao.CartDAO;
import shop.model.Cart;
import shop.model.CartItem;
import shop.model.Customer;

/**
 *
 * @author VICTUS
 */
@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

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
            HttpSession session = request.getSession(false);

            if (session == null || session.getAttribute("currentCustomer") == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            Customer customer = (Customer) session.getAttribute("currentCustomer");
            int customerId = customer.getCustomerId();

            try {
                CartDAO cartDAO = new CartDAO();
                List<CartItem> cartItems = cartDAO.getCartItemsByCustomerId(customerId);

                request.setAttribute("cartItems", cartItems);
                request.getRequestDispatcher("/WEB-INF/home/cart-list.jsp").forward(request, response);

            } catch (SQLException e) {
                throw new ServletException("Lỗi khi lấy danh sách giỏ hàng", e);
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
        String act = request.getParameter("action");

        if (act != null) {

            switch (act) {
                case "add":
                try ( PrintWriter out = response.getWriter()) {
                    String username = request.getParameter("username");
                    int productId = Integer.parseInt(request.getParameter("productId"));
                    int quantity = Integer.parseInt(request.getParameter("quantity"));

                    HttpSession session = request.getSession(false);

                    Customer customer = (Customer) session.getAttribute("currentCustomer");
                    int customerId = customer.getCustomerId();

                    // 2. Kiểm tra sản phẩm đã tồn tại trong giỏ chưa
                    CartDAO cartDAO = new CartDAO();
                    Cart existingCart = cartDAO.findCartItem(customerId, productId);

                    if (existingCart != null) {
                        // Nếu đã có -> Cập nhật số lượng
                        existingCart.setQuantity(existingCart.getQuantity() + quantity);
                        cartDAO.updateCart(existingCart);
                    } else {
                        // Nếu chưa có -> Thêm mới
                        Cart newCart = new Cart();
                        newCart.setCustomerId(customerId);
                        newCart.setProductId(productId);
                        newCart.setQuantity(quantity);
                        cartDAO.insertCart(newCart);
                    }

                    int cartCount = cartDAO.countCartItems(customerId);
                    session.setAttribute("cartCount", cartCount);

                    // 3. Chuyển hướng về trang sản phẩm hoặc giỏ hàng
                    String page = request.getParameter("page");

                    response.sendRedirect(request.getContextPath() + "/" + page);

                }

                break;

                case "decrease":
                try ( PrintWriter out = response.getWriter()) {
                    String username = request.getParameter("username");
                    int productId = Integer.parseInt(request.getParameter("productId"));
                    int quantityToDecrease = Integer.parseInt(request.getParameter("quantity"));

                    HttpSession session = request.getSession(false);
                    Customer customer = (Customer) session.getAttribute("currentCustomer");
                    int customerId = customer.getCustomerId();

                    CartDAO cartDAO = new CartDAO();
                    Cart existingCart = cartDAO.findCartItem(customerId, productId);

                    if (existingCart != null) {
                        int currentQuantity = existingCart.getQuantity();

                        if (currentQuantity <= quantityToDecrease || currentQuantity == 1) {
                            // Nếu quantity hiện tại <= quantity cần giảm, hoặc = 1 -> Xóa luôn
                            cartDAO.delete(productId, customerId);
                        } else {
                            // Nếu quantity > 1 -> giảm số lượng
                            existingCart.setQuantity(currentQuantity - quantityToDecrease);
                            cartDAO.updateCart(existingCart);
                        }
                    }
                    int cartCount = cartDAO.countCartItems(customerId);
                    session.setAttribute("cartCount", cartCount);
                    response.sendRedirect(request.getContextPath() + "/cart");
                }
                break;

                case "delete":
    try ( PrintWriter out = response.getWriter()) {
                    int productId = Integer.parseInt(request.getParameter("productId"));

                    HttpSession session = request.getSession(false);
                    Customer customer = (Customer) session.getAttribute("currentCustomer");
                    int customerId = customer.getCustomerId();

                    CartDAO cDAO = new CartDAO();

                    int rowsDeleted = cDAO.delete(productId, customerId);

                    if (rowsDeleted == 1) {
                        int cartCount = cDAO.countCartItems(customerId);
                        session.setAttribute("cartCount", cartCount);
                        response.sendRedirect(request.getContextPath() + "/cart");
                    }
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
