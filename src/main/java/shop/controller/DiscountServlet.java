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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import shop.dao.ProductDAO;
import shop.dao.VouchersDAO;
import shop.model.Product;
import shop.model.Voucher;

/**
 *
 * @author PC
 */
@WebServlet(name = "DiscountServlet", urlPatterns = {"/manage-discounts"})
public class DiscountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String view = request.getParameter("view");
        ProductDAO pD = new ProductDAO();

        if (view == null || view.isEmpty() || view.equals("list")) {

            try {
                ArrayList<Product> discountList = (ArrayList) pD.getListDicounts();

                request.setAttribute("discountlist", discountList);
                request.getRequestDispatcher("/WEB-INF/dashboard/discounts-list.jsp").forward(request, response);
            } catch (Exception ex) {
                Logger.getLogger(VoucherServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (view.equals("edit")) {
            ProductDAO productDao = new ProductDAO();
            int id = Integer.parseInt(request.getParameter("id"));
            try {
                Product product = productDao.getOneDiscount(id);
                request.setAttribute("product", product);
                request.setAttribute("id", id);
                request.getRequestDispatcher("/WEB-INF/dashboard/discount-edit.jsp").forward(request, response);
            } catch (Exception ex) {
                Logger.getLogger(VoucherServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (view.equals("search")) {
            try {
                ArrayList<Product> Productlist;
                String keyword = request.getParameter("keyword");
                if (keyword != null && !keyword.trim().isEmpty()) {
                    Productlist = pD.searchDiscountByCode(keyword);
                    request.setAttribute("discountlist", Productlist);
                    System.out.print(Productlist);
                } else {
                     Productlist = pD.getListDicounts();
                    request.setAttribute("discountlist", Productlist);
                }

                request.setAttribute("keyword", keyword);
                request.getRequestDispatcher("/WEB-INF/dashboard/discounts-list.jsp").forward(request, response);

            } catch (Exception ex) {
                Logger.getLogger(VoucherServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            request.getRequestDispatcher("/WEB-INF/error/not-found.jsp").forward(request, response);
          
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        ProductDAO pD = new ProductDAO();

        if (action != null) {
            switch (action) {
                case "edit":
                     try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    String name = request.getParameter("name");
                    BigDecimal price = new BigDecimal(request.getParameter("price"));
                    BigDecimal saleprice = new BigDecimal(request.getParameter("saleprice"));
                    int active = Integer.parseInt(request.getParameter("active"));
                    Product product = new Product(id, name, price, saleprice, active);

                    if (pD.editSalePriceAndActive(product) != 0) {
                        response.sendRedirect(request.getContextPath() + "/manage-discounts?view=list");

                    } else {
                        request.setAttribute("message", "Failed to update voucher!");
                        request.setAttribute("product", product);
                        request.getRequestDispatcher("/WEB-INF/dashboard/discount-edit.jsp").forward(request, response);
                    }

                } catch (Exception ex) {
                    Logger.getLogger(VoucherServlet.class
                            .getName()).log(Level.SEVERE, null, ex);
                    request.setAttribute("error", "Invalid data error!");
                    request.getRequestDispatcher("/WEB-INF/dashboard/discount-edit.jsp").forward(request, response);
                }
                break;
            }
        }
    }
}
