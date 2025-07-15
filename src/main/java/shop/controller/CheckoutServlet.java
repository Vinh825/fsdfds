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
import java.util.logging.Level;
import java.util.logging.Logger;
import shop.dao.CartDAO;
import shop.dao.CheckoutDAO;
import shop.dao.ProductDAO;
import shop.dao.VouchersDAO;
import shop.model.Checkout;
import shop.model.Customer;
import shop.model.Product;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

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
            out.println("<title>Servlet CheckoutServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CheckoutServlet at " + request.getContextPath() + "</h1>");
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
        Customer customer = (Customer) session.getAttribute("currentCustomer");
        String view = request.getParameter("view");
        if (view == null || view.isEmpty() || view.equals("single")) {
            if (customer.getAddress() == null || customer.getAddress().isEmpty() || customer.getEmail() == null || customer.getEmail().isEmpty() || customer.getPhone() == null || customer.getPhone().isEmpty()) {
                request.getRequestDispatcher("/WEB-INF/home/profile.jsp")
                        .forward(request, response);
            }

            String idtemp = request.getParameter("id");
            int id = Integer.parseInt(idtemp);
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            CheckoutDAO CD = new CheckoutDAO();
            try {
                Checkout temp = CD.getInfoToCheckout(id);
                temp.setQuantity(quantity);
                double price = (temp.getSale_price() == 0.0) ? temp.getPrice() : temp.getSale_price();
                double total = price * temp.getQuantity();
                ArrayList<Checkout> pro = new ArrayList<>();
                pro.add(temp);
                session.setAttribute("checkout", pro);
                session.setAttribute("totalAmount", total);
                session.setAttribute("status", "single");
                request.getRequestDispatcher("/WEB-INF/home/checkout.jsp").forward(request, response);
            } catch (SQLException ex) {
                Logger.getLogger(CheckoutServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("currentCustomer");
        String action = request.getParameter("action");
        if (action.equals("fromcart")) {
            if (customer.getAddress() == null || customer.getAddress().isEmpty() || customer.getEmail() == null || customer.getEmail().isEmpty() || customer.getPhone() == null || customer.getPhone().isEmpty()) {
                request.getRequestDispatcher("/WEB-INF/home/profile.jsp")
                        .forward(request, response);
            }
            String[] productId = request.getParameterValues("productIds");
            String[] quantity = request.getParameterValues("quantities");
            double total = Double.parseDouble(request.getParameter("totalAmount"));
            ArrayList<Checkout> temp = new ArrayList<>();
            CheckoutDAO CD = new CheckoutDAO();
            try {
                for (int i = 0; i < productId.length; i++) {
                    int id = Integer.parseInt(productId[i]);
                    int qty = Integer.parseInt(quantity[i]);

                    Checkout c = CD.getInfoToCheckout(id);
                    if (c != null) {
                        c.setQuantity(qty);
                        temp.add(c);
                    }
                }

                session.setAttribute("checkout", temp);
                session.setAttribute("status", "list");
                session.setAttribute("totalAmount", total);
                request.getRequestDispatcher("/WEB-INF/home/checkout.jsp").forward(request, response);
            } catch (SQLException ex) {
                Logger.getLogger(CheckoutServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (action.equals("order")) {
            String[] ids = request.getParameterValues("productId");
            String[] quantities = request.getParameterValues("quantity");
            String[] prices = request.getParameterValues("price");
            String payment = request.getParameter("paymentMethod");
            String totalStr = request.getParameter("total");
            int customerId = Integer.parseInt(request.getParameter("customerId"));
            String customerAddress = request.getParameter("customerAddress");
            String voucherCode = request.getParameter("vouchercode");
            double total = Double.parseDouble(totalStr);
            VouchersDAO VD = new VouchersDAO();
            CheckoutDAO CD = new CheckoutDAO();
            Integer voucherID = null;
            try {
                //get voucherID
                if (voucherCode != null && !voucherCode.trim().isEmpty() && !"null".equalsIgnoreCase(voucherCode.trim())) {
                    voucherID = VD.getVoucherIdByCode(voucherCode);
                } else {
                    voucherID = null;
                }
                if (CD.writeOrderDetails(CD.writeOrderIntoDb(customerId, voucherID, total, payment, customerAddress),
                        ids, quantities, prices) != 0) {
                    CartDAO cartDAO = new CartDAO();
                    cartDAO.deleteCartAfterBuy(customerId, ids);
                    int cartCount = cartDAO.countCartItems(customerId);
                    session.setAttribute("cartCount", cartCount);
                    session.setAttribute("buysucces", "You have successfully placed your order.");
                    response.sendRedirect(request.getContextPath() + "/home");
                }

            } catch (SQLException ex) {
                Logger.getLogger(CheckoutServlet.class.getName()).log(Level.SEVERE, null, ex);
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
