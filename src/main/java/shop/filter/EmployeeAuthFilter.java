/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import shop.util.SessionUtil;

/**
 *
 * @author Le Anh Khoa - CE190449
 */
//@WebFilter(filterName = "EmployeeAuthFilter", urlPatterns = {
//    "/manage-products", "/manage-staffs", "/manage-customers", "/manage-orders", "/manage-vouchers",
//    "/manage-discounts", "/profile-dashboard", "/dashboard"})
public class EmployeeAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        if (SessionUtil.isStaffLoggedIn(req)) {
            // Staff can only access specific pages
            String requestedUrl = req.getRequestURI();
            if (requestedUrl.contains("/manage-discounts") || requestedUrl.contains("/profile-dashboard") || requestedUrl.contains("/dashboard")) {
                ((HttpServletResponse) response).sendRedirect(req.getContextPath() + "/404");
                System.out.println("Staff tried to access a restricted page! Redirecting to 404.");
            } else {
                chain.doFilter(req, response);
                System.out.println("Staff has logged in and is accessing allowed pages.");
            }
        } else if (SessionUtil.isAdminLoggedIn(req)) {
            // Admin can access all pages
            chain.doFilter(req, response);
            System.out.println("Admin has logged in and is accessing allowed pages.");
        } else {
            ((HttpServletResponse) response).sendRedirect(req.getContextPath() + "/404");
            System.out.println("Admin not logged in! Redirecting to 404.");
        }
    }
}
