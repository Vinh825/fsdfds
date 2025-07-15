/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import shop.model.Staff;

/**
 *
 * @author Le Anh Khoa - CE190449
 */
public class SessionUtil {

    public static boolean isCustomerLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Get session without creating a new one
        return session != null && session.getAttribute("currentCustomer") != null; // Return true if session exists and user is set
    }

    public static boolean isStaffLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Get session without creating a new one
        if (session != null) {
            Object currentEmployee = session.getAttribute("currentEmployee");
            // Ensure currentEmployee is not null and has a 'role' field of type String
            if (currentEmployee != null) {
                String role = ((Staff) currentEmployee).getRole();
                return "staff".equals(role);
            }
        }
        return false;
    }
    
    public static boolean isAdminLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Get session without creating a new one
        if (session != null) {
            Object currentEmployee = session.getAttribute("currentEmployee");
            // Ensure currentEmployee is not null and has a 'role' field of type String
            if (currentEmployee != null) {
                String role = ((Staff) currentEmployee).getRole();
                return "admin".equals(role);
            }
        }
        return false;
    }
}
