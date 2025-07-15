/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import shop.db.DBContext;
import shop.model.Checkout;

/**
 *
 * @author ADMIN
 */
public class CheckoutDAO extends DBContext {

    public Checkout getInfoToCheckout(int productid) throws SQLException {
        Checkout temp = null;
        String query = "SELECT \n"
                + "	p.product_id,\n"
                + "    p.name AS product_name,\n"
                + "    p.price,\n"
                + "    p.sale_price,"
                + "    c.name AS category_name,\n"
                + "    i.image_url\n"
                + "FROM \n"
                + "    product p\n"
                + "JOIN \n"
                + "    category c ON p.category_id = c.category_id\n"
                + "OUTER APPLY (\n"
                + "    SELECT TOP 1 image_url \n"
                + "    FROM image \n"
                + "    WHERE product_id = p.product_id \n"
                + "    ORDER BY image_id\n"
                + ") i\n"
                + "WHERE \n"
                + "    p.product_id = ?;";
        Object[] params = {productid};
        ResultSet rs = execSelectQuery(query, params);
        while (rs.next()) {
            temp = new Checkout(rs.getInt("product_id"), rs.getString("image_url"), rs.getString("product_name"), rs.getString("category_name"), rs.getDouble("price"), rs.getDouble("sale_price"));
        }
        return temp;
    }

    public int writeOrderIntoDb(int customerId, Integer voucherId, double total, String paymentMethod, String address) throws SQLException {
      
        int orderId = 0;
        String query = "INSERT INTO [order] (\n"
                + "    customer_id, voucher_id, total_amount, payment_method, \n"
                + "    shipping_address, order_date, status, staff_id\n"
                + ")\n"
                + "VALUES (\n"
                + "    ?, ?, ?, ?, ?, GETDATE(), 'Pending', NULL\n"
                + ");SELECT SCOPE_IDENTITY();";
       
        Object[] params = {customerId, voucherId, total, paymentMethod, address};
                
        ResultSet rs = execSelectQuery(query, params);
        while (rs.next()) {
            orderId = rs.getInt(1);
        }
        return orderId;
        
    }

    public int writeOrderDetails(int orderId, String[] productId, String[] quantity, String[] price) throws SQLException {
        StringBuilder query = new StringBuilder("INSERT INTO order_detail (order_id, product_id, quantity, price) VALUES ");
        Object[] params = new Object[productId.length * 4];
        int index = 0;
        for (int i = 0; i < productId.length; i++) {
            query.append("(?, ?, ?, ?)");
            if (i < productId.length - 1) {
                query.append(", ");
            }
            int proId = Integer.parseInt(productId[i]);
            int quan = Integer.parseInt(quantity[i]);
            double pri = Double.parseDouble(price[i]);
            params[index++] = orderId;
            params[index++] = proId;
            params[index++] = quan;
            params[index++] = pri;

        }
        return execQuery(query.toString(), params);
    }

 

}
