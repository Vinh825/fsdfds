/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import shop.db.DBContext;
import shop.model.Customer;
import shop.model.Order;
import shop.model.OrderDetails;
import shop.model.Product;

/**
 *
 * @author ADMIN
 */
public class OrderDAO extends DBContext {

    public ArrayList<Order> getallOrder() throws SQLException {
        ArrayList<Order> order = new ArrayList<>();
        String query = "SELECT \n"
                + "    o.*, \n"
                + "    c.full_name\n"
                + "FROM \n"
                + "    [order] o\n"
                + "JOIN \n"
                + "    customer c ON o.customer_id = c.customer_id \n"
                + "ORDER BY o.order_date DESC;";
        ResultSet rs = execSelectQuery(query);
        while (rs.next()) {
            order.add(new Order(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getBigDecimal(4), rs.getString(5), rs.getString(6), rs.getObject("order_date", LocalDateTime.class),
                    rs.getString(8), rs.getInt(9), rs.getString(10)));
        }
        return order;

    }

    public ArrayList<OrderDetails> getOrderDetail(int orderId) throws SQLException {
        ArrayList<OrderDetails> temp = new ArrayList<>();
        String query = "SELECT \n"
                + "    od.*, \n"
                + "    p.name AS product_name\n"
                + "FROM \n"
                + "    Order_Detail od\n"
                + "JOIN \n"
                + "    Product p ON od.product_id = p.product_id\n"
                + "WHERE \n"
                + "    od.order_id = ?;";
        Object[] params = {orderId};
        ResultSet rs = execSelectQuery(query, params);
        while (rs.next()) {
            temp.add(new OrderDetails(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getBigDecimal(5), rs.getString(6)));
        }
        return temp;

    }

    public ArrayList<OrderDetails> getOrderDetailForCus(int orderId) throws SQLException {
        ArrayList<OrderDetails> temp = new ArrayList<>();
        String query = "SELECT \n"
                + "    od.product_id,\n"
                + "    od.quantity,\n"
                + "    od.price,\n"
                + "    p.name AS product_name,\n"
                + "    img.image_URL,\n"
                + "    c.name AS category_name,\n"
                + "    gk.key_code\n"
                + "FROM order_detail od\n"
                + "JOIN product p ON od.product_id = p.product_id\n"
                + "\n"
                + "OUTER APPLY (\n"
                + "    SELECT TOP 1 image_URL\n"
                + "    FROM image\n"
                + "    WHERE image.product_id = p.product_id\n"
                + "    ORDER BY image_id\n"
                + ") AS img\n"
                + "\n"
                + "LEFT JOIN category c ON p.category_id = c.category_id\n"
                + "LEFT JOIN game_details gd ON p.game_details_id = gd.game_details_id\n"
                + "\n"
                + "-- Chỉ lấy 1 game key (nếu có) với OUTER APPLY\n"
                + "OUTER APPLY (\n"
                + "    SELECT TOP 1 key_code\n"
                + "    FROM game_key\n"
                + "    WHERE game_key.game_details_id = gd.game_details_id\n"
                + "    ORDER BY game_key_id\n"
                + ") AS gk\n"
                + "\n"
                + "WHERE od.order_id =?;";
        Object[] params = {orderId};
        ResultSet rs = execSelectQuery(query, params);
        while (rs.next()) {
            if (rs.getString(6).equalsIgnoreCase("game")) {
                temp.add(new OrderDetails(rs.getInt(1), rs.getInt(2), rs.getBigDecimal(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
            } else {
                temp.add(new OrderDetails(rs.getInt(1), rs.getInt(2), rs.getBigDecimal(3), rs.getString(4), rs.getString(5), rs.getString(6)));
            }
        }
        return temp;

    }

    public Order getOneOrder(int orderId) throws SQLException {
        Order temp = new Order();
        String query = "SELECT \n"
                + "    o.*, \n"
                + "    c.full_name, \n"
                + "    c.email\n"
                + "FROM \n"
                + "    [Order] o\n"
                + "JOIN \n"
                + "    Customer c ON o.customer_id = c.customer_id\n"
                + "WHERE \n"
                + "    o.order_id = ?;";
        Object[] params = {orderId};
        ResultSet rs = execSelectQuery(query, params);
        while (rs.next()) {
            temp.setOrderId(rs.getInt(1));
            temp.setTotalAmount(rs.getBigDecimal(4));
            temp.setOrderDate(rs.getObject("order_date", LocalDateTime.class));
            temp.setStatus(rs.getString(8));
            temp.setShippingAddress(rs.getString(6));
            temp.setCustomerName(rs.getString(10));
            temp.setCustomerEmail(rs.getString(11));
        }
        return temp;
    }

    public int updateOrderStatus(String status, int orderId) throws SQLException {
        String query = "UPDATE [order]\n"
                + "SET status = ?\n"
                + "WHERE order_id = ?;";
        Object[] params = {status, orderId};
        return execQuery(query, params);
    }

    public ArrayList<Order> searchOrders(String customer_name) throws SQLException {
        ArrayList<Order> temp = new ArrayList<>();
        String query = "SELECT o.*, c.full_name\n"
                + "FROM [Order] o\n"
                + "JOIN Customer c ON o.customer_id = c.customer_id\n"
                + "WHERE c.full_name LIKE ? \n"
                + "ORDER BY o.order_date DESC;";
        Object[] params = {"%" + customer_name + "%"};
        ResultSet rs = execSelectQuery(query, params);
        while (rs.next()) {
            temp.add(new Order(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getBigDecimal(4), rs.getString(5), rs.getString(6), rs.getObject("order_date", LocalDateTime.class),
                    rs.getString(8), rs.getInt(9), rs.getString(10)));
        }
        return temp;

    }

    public ArrayList<Order> getOrderById(int customerId) throws SQLException {
        ArrayList<Order> order = new ArrayList<>();
        String query = "SELECT \n"
                + "    o.*, \n"
                + "    c.full_name\n"
                + "FROM \n"
                + "    [order] o\n"
                + "JOIN \n"
                + "    customer c ON o.customer_id = c.customer_id \n" // thêm khoảng trắng hoặc xuống dòng
                + "WHERE o.customer_id = ? \n"
                + "ORDER BY o.order_date DESC;";

        Object[] params = {customerId};
        ResultSet rs = execSelectQuery(query, params);
        while (rs.next()) {
            order.add(new Order(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getBigDecimal(4), rs.getString(5), rs.getString(6), rs.getObject("order_date", LocalDateTime.class),
                    rs.getString(8), rs.getInt(9), rs.getString(10)));
        }
        return order;

    }

    public int[] getProIdByOrderId(int orderid) throws SQLException {
        List<Integer> tempList = new ArrayList<>();

        String query = "SELECT product_id FROM order_detail WHERE order_id = ?";
        Object[] params = {orderid};
        ResultSet rs = execSelectQuery(query, params);

        while (rs.next()) {
            tempList.add(rs.getInt("product_id"));
        }

        int[] proId = new int[tempList.size()];
        for (int i = 0; i < tempList.size(); i++) {
            proId[i] = tempList.get(i);
        }

        return proId;
    }

//   --- SangNH----
    public int getTotalProductsInStock() {
        String sql = "SELECT SUM(quantity) AS total_stock FROM product";
        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total_stock");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTotalCustomers() {
        String sql = "SELECT COUNT(customer_id) AS total_customers FROM customer";
        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total_customers");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Map<String, Object> getSummaryStats(String period) {
        Map<String, Object> stats = new HashMap<>();
        String whereClause = "";

        switch (period) {
            case "today":
                whereClause = "WHERE CAST(o.order_date AS DATE) = CAST(GETDATE() AS DATE)";
                break;
            case "week":
                whereClause = "WHERE DATEPART(week, o.order_date) = DATEPART(week, GETDATE()) AND DATEPART(year, o.order_date) = DATEPART(year, GETDATE())";
                break;
            case "month":
                whereClause = "WHERE DATEPART(month, o.order_date) = DATEPART(month, GETDATE()) AND DATEPART(year, o.order_date) = DATEPART(year, GETDATE())";
                break;
            case "year":
                whereClause = "WHERE DATEPART(year, o.order_date) = DATEPART(year, GETDATE())";
                break;
        }

        String revenueSql = "SELECT ISNULL(SUM(total_amount), 0) FROM [order] o " + whereClause;
        String soldSql = "SELECT ISNULL(SUM(od.quantity), 0) FROM order_detail od JOIN [order] o ON od.order_id = o.order_id " + whereClause;

        try {
            try ( PreparedStatement ps = conn.prepareStatement(revenueSql);  ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.put("totalRevenue", rs.getDouble(1));
                }
            }
            try ( PreparedStatement ps = conn.prepareStatement(soldSql);  ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.put("productsSold", rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            stats.put("totalRevenue", 0.0);
            stats.put("productsSold", 0);
        }
        return stats;
    }

    public List<Product> getTopSellingProductsDetails(int limit) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT TOP (?) p.product_id, p.name, p.price, SUM(od.quantity) AS total_sold, "
                + "(SELECT TOP 1 i.image_URL FROM image i WHERE i.product_id = p.product_id ORDER BY i.image_id) AS image_url "
                + "FROM order_detail od "
                + "JOIN product p ON od.product_id = p.product_id "
                + "GROUP BY p.product_id, p.name, p.price "
                + "ORDER BY total_sold DESC";

        try (
                 PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setProductId(rs.getInt("product_id"));
                    p.setName(rs.getString("name"));
                    p.setPrice(rs.getBigDecimal("price"));
                    p.setQuantity(rs.getInt("total_sold"));

                    List<String> imageUrls = new ArrayList<>();
                    String imageUrl = rs.getString("image_url");
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        imageUrls.add(imageUrl);
                    }
                    p.setImageUrls(imageUrls);

                    productList.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    public Map<String, Object> getRevenueTrend(String period) {
        Map<String, Object> result = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();
        String sql;

        try {
            switch (period) {
                case "today":
                    sql = "SELECT DATEPART(hour, order_date) AS hour_of_day, SUM(total_amount) AS revenue "
                            + "FROM [order] WHERE CAST(order_date AS DATE) = CAST(GETDATE() AS DATE) "
                            + "GROUP BY DATEPART(hour, order_date)";

                    Map<Integer, Double> hourlyRevenue = new HashMap<>();
                    try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            hourlyRevenue.put(rs.getInt("hour_of_day"), rs.getDouble("revenue"));
                        }
                    }
                    for (int i = 0; i < 24; i++) {
                        labels.add(i + "h");
                        data.add(hourlyRevenue.getOrDefault(i, 0.0));
                    }
                    break;

                case "week":
                    sql = "SELECT DATEPART(weekday, order_date) AS day_of_week, SUM(total_amount) AS revenue "
                            + "FROM [order] WHERE DATEPART(week, order_date) = DATEPART(week, GETDATE()) AND DATEPART(year, order_date) = DATEPART(year, GETDATE()) "
                            + "GROUP BY DATEPART(weekday, order_date)";

                    Map<Integer, Double> dailyRevenue = new HashMap<>();
                    try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            dailyRevenue.put(rs.getInt("day_of_week"), rs.getDouble("revenue"));
                        }
                    }
                    String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
                    for (int i = 1; i <= 7; i++) {
                        labels.add(dayNames[i - 1]);
                        data.add(dailyRevenue.getOrDefault(i, 0.0));
                    }
                    break;

                case "month":
                    sql = "SELECT (DATEPART(day, order_date) - 1) / 7 + 1 AS week_of_month, SUM(total_amount) AS revenue "
                            + "FROM [order] WHERE DATEPART(month, order_date) = DATEPART(month, GETDATE()) AND DATEPART(year, order_date) = DATEPART(year, GETDATE()) "
                            + "GROUP BY (DATEPART(day, order_date) - 1) / 7 + 1";

                    Map<Integer, Double> weeklyRevenue = new HashMap<>();
                    try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            weeklyRevenue.put(rs.getInt("week_of_month"), rs.getDouble("revenue"));
                        }
                    }
                    Calendar cal = Calendar.getInstance();
                    int weeksInMonth = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
                    for (int i = 1; i <= weeksInMonth; i++) {
                        labels.add("Week " + i);
                        data.add(weeklyRevenue.getOrDefault(i, 0.0));
                    }
                    break;

                case "year":
                default:
                    sql = "SELECT MONTH(order_date) AS month_num, SUM(total_amount) AS revenue "
                            + "FROM [order] WHERE YEAR(order_date) = YEAR(GETDATE()) "
                            + "GROUP BY MONTH(order_date) ORDER BY month_num";

                    double[] monthlyRevenues = new double[12];
                    try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            int month = rs.getInt("month_num");
                            monthlyRevenues[month - 1] = rs.getDouble("revenue");
                        }
                    }
                    String[] monthNamesArr = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                    for (int i = 0; i < 12; i++) {
                        labels.add(monthNamesArr[i]);
                        data.add(monthlyRevenues[i]);
                    }
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        result.put("labels", labels);
        result.put("data", data);
        return result;
    }

    public Map<String, List<?>> getStockByCategory() {
        Map<String, List<?>> chartData = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Integer> data = new ArrayList<>();
        String sql = "SELECT c.name, SUM(p.quantity) AS total_stock "
                + "FROM product p JOIN category c ON p.category_id = c.category_id "
                + "GROUP BY c.name HAVING SUM(p.quantity) > 0 ORDER BY total_stock DESC";

        try (
                 PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                labels.add(rs.getString("name"));
                data.add(rs.getInt("total_stock"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        chartData.put("labels", labels);
        chartData.put("data", data);
        return chartData;
    }
}
