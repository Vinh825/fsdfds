/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shop.db.DBContext;
import shop.model.GameDetails;
import shop.model.Product;
import shop.model.ProductAttribute;

/**
 *
 * @author HoangSang
 */
public class ProductDAO extends DBContext {

    public ArrayList<Product> searchDiscountByCode(String keyword) {
        ArrayList<Product> products = new ArrayList<>();
        String query = "SELECT product_id, name, price, sale_price, active FROM product WHERE LOWER(name) LIKE LOWER(?)";

        try ( PreparedStatement stmt = this.getConnection().prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getBigDecimal("sale_price"),
                        rs.getInt("active")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            Logger.getLogger(VouchersDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return products;
    }

    public ArrayList<Product> getProductsByCategory(String categoryName) {
        ArrayList<Product> products = new ArrayList<>();
        String query = "SELECT p.product_id, "
                + "p.name, "
                + "p.price, "
                + "p.quantity, "
                + "b.brand_name, "
                + "(SELECT TOP 1 i.image_url FROM image i WHERE i.product_id = p.product_id) AS image_url "
                + "FROM product p "
                + "JOIN category c ON p.category_id = c.category_id "
                + "LEFT JOIN brand b ON p.brand_id = b.brand_id "
                + "WHERE LOWER(c.name) = LOWER(?)";

        try ( PreparedStatement stmt = this.getConnection().prepareStatement(query)) {
            stmt.setString(1, categoryName.toLowerCase());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setBrandName(rs.getString("brand_name"));

                String singleImageUrl = rs.getString("image_url");
                List<String> imageUrls = new ArrayList<>();
                if (singleImageUrl != null && !singleImageUrl.isEmpty()) {
                    imageUrls.add(singleImageUrl);
                }
                product.setImageUrls(imageUrls);

                products.add(product);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return products;
    }

    public ArrayList<Product> searchProductByName(String keyword) {
        ArrayList<Product> products = new ArrayList<>();
        String query = "SELECT p.product_id, "
                + "p.name, "
                + "p.price, "
                + "p.quantity, "
                + "b.brand_name, "
                + "(SELECT TOP 1 i.image_url FROM image i WHERE i.product_id = p.product_id) AS image_url "
                + "FROM product p "
                + "LEFT JOIN brand b ON p.brand_id = b.brand_id "
                + "WHERE LOWER(p.name) LIKE LOWER(?)";

        try ( PreparedStatement stmt = this.getConnection().prepareStatement(query)) {
            stmt.setString(1, "%" + keyword.toLowerCase() + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setBrandName(rs.getString("brand_name"));

                String singleImageUrl = rs.getString("image_url");
                List<String> imageUrls = new ArrayList<>();
                if (singleImageUrl != null && !singleImageUrl.isEmpty()) {
                    imageUrls.add(singleImageUrl);
                }
                product.setImageUrls(imageUrls);
                products.add(product);
            }
        } catch (SQLException e) {
            Logger.getLogger(VouchersDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return products;
    }

    public int editSalePriceAndActive(Product product) {
        try {
            String query = "UPDATE product SET sale_price = ?, active = ?, updated_at = GETDATE() WHERE product_id = ?;";
            Object[] params = {
                product.getSalePrice(),
                product.getActive(),
                product.getProductId()
            };
            return execQuery(query, params);
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public Product getOneDiscount(int id) {
        try {
            String query = "select * from product where product_id =?;";
            Object[] params = {id};
            ResultSet rs = execSelectQuery(query, params);

            if (rs.next()) {
                return new Product(rs.getString("name"), rs.getBigDecimal("price"), rs.getBigDecimal("sale_price"), rs.getInt("active"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ArrayList<Product> getListDicounts() {
        ArrayList<Product> products = new ArrayList<>();
        try {
            String query = "SELECT product_id, name, price, sale_price, active FROM product;";
            ResultSet rs = execSelectQuery(query);
            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getBigDecimal("sale_price"),
                        rs.getInt("active")
                );
                products.add(product);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return products;
    }

    public void addFullGameProduct(Product product, GameDetails gameDetails, List<String> imageUrls, String[] platformIds, String[] osIds, String[] newKeys) throws SQLException {
        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false);

            int gameDetailsId;
            String sqlGameDetails = "INSERT INTO game_details (developer, genre, release_date) VALUES (?, ?, ?)";
            try ( PreparedStatement ps = conn.prepareStatement(sqlGameDetails, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, gameDetails.getDeveloper());
                ps.setString(2, gameDetails.getGenre());
                ps.setDate(3, gameDetails.getReleaseDate());
                ps.executeUpdate();
                try ( ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        gameDetailsId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating game details failed, no ID obtained.");
                    }
                }
            }

            int productId;
            String sqlProduct = "INSERT INTO product (name, description, price, quantity, category_id, brand_id, game_details_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())";
            try ( PreparedStatement ps = conn.prepareStatement(sqlProduct, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, product.getName());
                ps.setString(2, product.getDescription());
                ps.setBigDecimal(3, product.getPrice());
                ps.setInt(4, product.getQuantity());
                ps.setInt(5, product.getCategoryId());
                ps.setNull(6, Types.INTEGER);
                ps.setInt(7, gameDetailsId);
                ps.executeUpdate();
                try ( ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        productId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating product failed, no ID obtained.");
                    }
                }
            }

            if (imageUrls != null && !imageUrls.isEmpty()) {
                String sqlImages = "INSERT INTO image (product_id, image_URL) VALUES (?, ?)";
                try ( PreparedStatement ps = conn.prepareStatement(sqlImages)) {
                    for (String imageUrl : imageUrls) {
                        ps.setInt(1, productId);
                        ps.setString(2, imageUrl);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }

            if (platformIds != null && platformIds.length > 0) {
                StorePlatformDAO platformDao = new StorePlatformDAO();
                String sqlPlatform = "INSERT INTO store_platform (game_details_id, store_OS_name) VALUES (?, ?)";
                try ( PreparedStatement ps = conn.prepareStatement(sqlPlatform)) {
                    for (String pIdStr : platformIds) {
                        int pId = Integer.parseInt(pIdStr);
                        String platformName = platformDao.getStorePlatformNameById(pId);
                        if (platformName != null) {
                            ps.setInt(1, gameDetailsId);
                            ps.setString(2, platformName);
                            ps.addBatch();
                        }
                    }
                    ps.executeBatch();
                }
            }

            if (osIds != null && osIds.length > 0) {
                OperatingSystemDAO osDao = new OperatingSystemDAO();
                String sqlOs = "INSERT INTO operating_system (game_details_id, os_name) VALUES (?, ?)";
                try ( PreparedStatement ps = conn.prepareStatement(sqlOs)) {
                    for (String oIdStr : osIds) {
                        int oId = Integer.parseInt(oIdStr);
                        String osName = osDao.getOsNameById(oId);
                        if (osName != null) {
                            ps.setInt(1, gameDetailsId);
                            ps.setString(2, osName);
                            ps.addBatch();
                        }
                    }
                    ps.executeBatch();
                }
            }

            if (newKeys != null && newKeys.length > 0) {
                String sqlKey = "INSERT INTO game_key (game_details_id, key_code) VALUES (?, ?)";
                try ( PreparedStatement ps = conn.prepareStatement(sqlKey)) {
                    for (String key : newKeys) {
                        if (key != null && !key.trim().isEmpty()) {
                            ps.setInt(1, gameDetailsId);
                            ps.setString(2, key.trim());
                            ps.addBatch();
                        }
                    }
                    ps.executeBatch();
                }
            }

            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            throw new SQLException("Lỗi khi thêm sản phẩm game.", e);
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public boolean isProductSold(int productId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM order_detail WHERE product_id = ?";
        // Sử dụng try-with-resources để đảm bảo kết nối được đóng đúng cách
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        // Mặc định trả về false nếu không tìm thấy hoặc có lỗi
        return false;
    }

    public void deleteProduct(int productId) throws SQLException {
        Integer gameDetailsIdToDelete = null;

        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false);

            String getGameDetailsIdSql = "SELECT game_details_id FROM product WHERE product_id = ?";
            try ( PreparedStatement ps = conn.prepareStatement(getGameDetailsIdSql)) {
                ps.setInt(1, productId);
                try ( ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        gameDetailsIdToDelete = (Integer) rs.getObject("game_details_id");
                    }
                }
            }

            String deleteAttributesSQL = "DELETE FROM product_attribute WHERE product_id = ?";
            try ( PreparedStatement ps = conn.prepareStatement(deleteAttributesSQL)) {
                ps.setInt(1, productId);
                ps.executeUpdate();
            }

            String deleteImagesSQL = "DELETE FROM image WHERE product_id = ?";
            try ( PreparedStatement ps = conn.prepareStatement(deleteImagesSQL)) {
                ps.setInt(1, productId);
                ps.executeUpdate();
            }

            String deleteProductSQL = "DELETE FROM product WHERE product_id = ?";
            try ( PreparedStatement ps = conn.prepareStatement(deleteProductSQL)) {
                ps.setInt(1, productId);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Deleting product failed, no rows affected for product_id: " + productId);
                }
            }

            if (gameDetailsIdToDelete != null) {
                String checkRefsSql = "SELECT COUNT(*) FROM product WHERE game_details_id = ?";
                int refCount = 0;
                try ( PreparedStatement ps = conn.prepareStatement(checkRefsSql)) {
                    ps.setInt(1, gameDetailsIdToDelete);
                    try ( ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            refCount = rs.getInt(1);
                        }
                    }
                }

                if (refCount == 0) {
                    String deleteKeysSql = "DELETE FROM game_key WHERE game_details_id = ?";
                    try ( PreparedStatement ps = conn.prepareStatement(deleteKeysSql)) {
                        ps.setInt(1, gameDetailsIdToDelete);
                        ps.executeUpdate();
                    }
                    String deletePlatformSql = "DELETE FROM store_platform WHERE game_details_id = ?";
                    try ( PreparedStatement ps = conn.prepareStatement(deletePlatformSql)) {
                        ps.setInt(1, gameDetailsIdToDelete);
                        ps.executeUpdate();
                    }
                    String deleteOsSql = "DELETE FROM operating_system WHERE game_details_id = ?";
                    try ( PreparedStatement ps = conn.prepareStatement(deleteOsSql)) {
                        ps.setInt(1, gameDetailsIdToDelete);
                        ps.executeUpdate();
                    }
                    String deleteGameDetailsSql = "DELETE FROM game_details WHERE game_details_id = ?";
                    try ( PreparedStatement ps = conn.prepareStatement(deleteGameDetailsSql)) {
                        ps.setInt(1, gameDetailsIdToDelete);
                        ps.executeUpdate();
                    }
                }
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.product_id, p.name, p.price, p.sale_price, p.quantity,p.active_product, c.name AS category_name, b.brand_name, "
                + "(SELECT TOP 1 i.image_URL FROM image i WHERE i.product_id = p.product_id ORDER BY i.image_id) AS image_url "
                + "FROM product p "
                + "LEFT JOIN category c ON p.category_id = c.category_id "
                + "LEFT JOIN brand b ON p.brand_id = b.brand_id "
                + "ORDER BY p.product_id";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setCategoryName(rs.getString("category_name"));
                product.setBrandName(rs.getString("brand_name"));
                product.setSalePrice(rs.getBigDecimal("sale_price"));
                product.setActiveProduct(rs.getInt("active_product"));

                String singleImageUrl = rs.getString("image_url");
                List<String> imageUrls = new ArrayList<>();
                if (singleImageUrl != null && !singleImageUrl.isEmpty()) {
                    imageUrls.add(singleImageUrl);
                }
                product.setImageUrls(imageUrls);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    private void addProductAndImages(Connection conn, Product product, List<String> imageUrls) throws SQLException {
        String sqlProduct = "INSERT INTO product (name, description, price, quantity, sale_price, category_id, brand_id, game_details_id, created_at, updated_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())";

        try ( PreparedStatement psProduct = conn.prepareStatement(sqlProduct, Statement.RETURN_GENERATED_KEYS)) {
            psProduct.setString(1, product.getName());
            psProduct.setString(2, product.getDescription());
            psProduct.setBigDecimal(3, product.getPrice());
            psProduct.setInt(4, product.getQuantity());

            if (product.getSalePrice() != null) {
                psProduct.setBigDecimal(5, product.getSalePrice());
            } else {
                psProduct.setNull(5, Types.DECIMAL);
            }
            psProduct.setInt(6, product.getCategoryId());

            if (product.getBrandId() != null) {
                psProduct.setInt(7, product.getBrandId());
            } else {
                psProduct.setNull(7, Types.INTEGER);
            }
            if (product.getGameDetailsId() != null) {
                psProduct.setInt(8, product.getGameDetailsId());
            } else {
                psProduct.setNull(8, Types.INTEGER);
            }

            psProduct.executeUpdate();

            try ( ResultSet rs = psProduct.getGeneratedKeys()) {
                if (rs.next()) {
                    product.setProductId(rs.getInt(1));
                } else {
                    throw new SQLException("Creating product failed, no ID obtained.");
                }
            }
        }

        if (imageUrls != null && !imageUrls.isEmpty()) {
            String sqlImage = "INSERT INTO image (product_id, image_URL) VALUES (?, ?)";
            try ( PreparedStatement psImage = conn.prepareStatement(sqlImage)) {
                for (String imageUrl : imageUrls) {
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        psImage.setInt(1, product.getProductId());
                        psImage.setString(2, imageUrl);
                        psImage.addBatch();
                    }
                }
                psImage.executeBatch();
            }
        }
    }

    public void addGameProduct(Product product, GameDetails details, List<String> imageUrls) throws SQLException {
        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false);

            String sqlDetails = "INSERT INTO game_details (developer, genre, release_date) VALUES (?, ?, ?)";
            try ( PreparedStatement psDetails = conn.prepareStatement(sqlDetails, Statement.RETURN_GENERATED_KEYS)) {
                psDetails.setString(1, details.getDeveloper());
                psDetails.setString(2, details.getGenre());
                psDetails.setDate(3, details.getReleaseDate());
                psDetails.executeUpdate();

                try ( ResultSet generatedKeys = psDetails.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setGameDetailsId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating game details failed, no ID obtained.");
                    }
                }
            }

            addProductAndImages(conn, product, imageUrls);

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public void addAccessoryProduct(Product product, List<ProductAttribute> attributes, List<String> imageUrls) throws SQLException {
        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false);

            addProductAndImages(conn, product, imageUrls);

            if (attributes != null && !attributes.isEmpty()) {
                String sqlAttr = "INSERT INTO product_attribute (product_id, attribute_id, value) VALUES (?, ?, ?)";
                AttributeDAO attributeDAO = new AttributeDAO();
                try ( PreparedStatement psAttr = conn.prepareStatement(sqlAttr)) {
                    for (ProductAttribute pa : attributes) {
                        if (pa != null && pa.getValue() != null && !pa.getValue().isEmpty()) {
                            int attributeId = attributeDAO.getAttributeIdByName(pa.getAttributeName());
                            if (attributeId > 0) {
                                psAttr.setInt(1, product.getProductId());
                                psAttr.setInt(2, attributeId);
                                psAttr.setString(3, pa.getValue());
                                psAttr.addBatch();
                            }
                        }
                    }
                    psAttr.executeBatch();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public void updateProduct(Product product, GameDetails gameDetails, List<ProductAttribute> attributes,
            List<String> newImageUrls, String[] platformIds, String[] osIds, String[] newKeys) throws SQLException {
        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false);

            String sqlUpdateProduct = "UPDATE product SET name=?, description=?, price=?, quantity=?, sale_price=?, category_id=?, brand_id=?, updated_at=GETDATE() WHERE product_id=?";
            try ( PreparedStatement ps = conn.prepareStatement(sqlUpdateProduct)) {
                ps.setString(1, product.getName());
                ps.setString(2, product.getDescription());
                ps.setBigDecimal(3, product.getPrice());
                ps.setInt(4, product.getQuantity());
                ps.setBigDecimal(5, product.getSalePrice());
                ps.setInt(6, product.getCategoryId());
                if (product.getBrandId() != null) {
                    ps.setInt(7, product.getBrandId());
                } else {
                    ps.setNull(7, Types.INTEGER);
                }
                ps.setInt(8, product.getProductId());
                ps.executeUpdate();
            }

            Integer gameDetailsId = product.getGameDetailsId();

            if (gameDetailsId != null && gameDetails != null) {
                String sqlUpdateGame = "UPDATE game_details SET developer=?, genre=?, release_date=? WHERE game_details_id=?";
                try ( PreparedStatement ps = conn.prepareStatement(sqlUpdateGame)) {
                    ps.setString(1, gameDetails.getDeveloper());
                    ps.setString(2, gameDetails.getGenre());
                    ps.setDate(3, gameDetails.getReleaseDate() != null ? gameDetails.getReleaseDate() : null);
                    ps.setInt(4, gameDetailsId);
                    ps.executeUpdate();
                }
                try ( PreparedStatement ps = conn.prepareStatement("DELETE FROM store_platform WHERE game_details_id = ?")) {
                    ps.setInt(1, gameDetailsId);
                    ps.executeUpdate();
                }

                if (platformIds != null && platformIds.length > 0) {
                    String sqlPlatform = "INSERT INTO store_platform (game_details_id, store_OS_name) VALUES (?, ?)";
                    try ( PreparedStatement ps = conn.prepareStatement(sqlPlatform)) {
                        StorePlatformDAO platformDao = new StorePlatformDAO();
                        for (String pIdStr : platformIds) {
                            int pId = Integer.parseInt(pIdStr);
                            String platformName = platformDao.getStorePlatformNameById(pId);
                            if (platformName != null) {
                                ps.setInt(1, gameDetailsId);
                                ps.setString(2, platformName);
                                ps.addBatch();
                            }
                        }
                        ps.executeBatch();
                    }
                }

                try ( PreparedStatement ps = conn.prepareStatement("DELETE FROM operating_system WHERE game_details_id = ?")) {
                    ps.setInt(1, gameDetailsId);
                    ps.executeUpdate();
                }

                if (osIds != null && osIds.length > 0) {
                    String sqlOs = "INSERT INTO operating_system (game_details_id, os_name) VALUES (?, ?)";
                    try ( PreparedStatement ps = conn.prepareStatement(sqlOs)) {
                        OperatingSystemDAO osDao = new OperatingSystemDAO();
                        for (String oIdStr : osIds) {
                            int oId = Integer.parseInt(oIdStr);
                            String osName = osDao.getOsNameById(oId);
                            if (osName != null) {
                                ps.setInt(1, gameDetailsId);
                                ps.setString(2, osName);
                                ps.addBatch();
                            }
                        }
                        ps.executeBatch();
                    }
                }

                if (newKeys != null && newKeys.length > 0) {
                    String sqlKey = "INSERT INTO game_key (game_details_id, key_code) VALUES (?, ?)";
                    try ( PreparedStatement ps = conn.prepareStatement(sqlKey)) {
                        for (String key : newKeys) {
                            if (key != null && !key.trim().isEmpty()) {
                                ps.setInt(1, gameDetailsId);
                                ps.setString(2, key.trim());
                                ps.addBatch();
                            }
                        }
                        ps.executeBatch();
                    }
                }

            } else {
                try ( PreparedStatement psDelete = conn.prepareStatement("DELETE FROM product_attribute WHERE product_id = ?")) {
                    psDelete.setInt(1, product.getProductId());
                    psDelete.executeUpdate();
                }

                if (attributes != null && !attributes.isEmpty()) {
                    String sqlInsertAttr = "INSERT INTO product_attribute (product_id, attribute_id, value) VALUES (?, ?, ?)";
                    try ( PreparedStatement psInsert = conn.prepareStatement(sqlInsertAttr)) {
                        AttributeDAO attributeDAO = new AttributeDAO();
                        for (ProductAttribute pa : attributes) {
                            int attributeId = attributeDAO.getAttributeIdByName(pa.getAttributeName());
                            if (attributeId != -1) {
                                psInsert.setInt(1, product.getProductId());
                                psInsert.setInt(2, attributeId);
                                psInsert.setString(3, pa.getValue());
                                psInsert.addBatch();
                            }
                        }
                        psInsert.executeBatch();
                    }
                }
            }

            if (newImageUrls != null) {
                try ( PreparedStatement ps = conn.prepareStatement("DELETE FROM image WHERE product_id = ?")) {
                    ps.setInt(1, product.getProductId());
                    ps.executeUpdate();
                }
                if (!newImageUrls.isEmpty()) {
                    String sqlInsertImage = "INSERT INTO image (product_id, image_URL) VALUES (?, ?)";
                    try ( PreparedStatement ps = conn.prepareStatement(sqlInsertImage)) {
                        for (String url : newImageUrls) {
                            ps.setInt(1, product.getProductId());
                            ps.setString(2, url);
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }
                }
            }

            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            throw new SQLException("Lỗi khi cập nhật sản phẩm.", e);
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public Product getProductById(int productId) {
        Product product = null;
        String productSql = "SELECT p.*, c.name AS category_name, b.brand_name, gd.developer, gd.genre, gd.release_date "
                + "FROM product p "
                + "LEFT JOIN category c ON p.category_id = c.category_id "
                + "LEFT JOIN brand b ON p.brand_id = b.brand_id "
                + "LEFT JOIN game_details gd ON p.game_details_id = gd.game_details_id "
                + "WHERE p.product_id = ?";
        String imagesSql = "SELECT image_URL FROM image WHERE product_id = ? ORDER BY image_id";
        String attributeSql = "SELECT a.name AS attribute_name, pa.value "
                + "FROM product_attribute pa "
                + "JOIN attribute a ON pa.attribute_id = a.attribute_id "
                + "WHERE pa.product_id = ?";

        try ( Connection conn = new DBContext().getConnection()) {
            try ( PreparedStatement psProduct = conn.prepareStatement(productSql)) {
                psProduct.setInt(1, productId);
                try ( ResultSet rs = psProduct.executeQuery()) {
                    if (rs.next()) {
                        product = mapResultSetToProduct(rs);
                        if (rs.getObject("game_details_id") != null) {
                            product.setGameDetailsId(rs.getInt("game_details_id"));
                            GameDetails details = mapResultSetToGameDetails(rs);
                            product.setGameDetails(details);
                        }
                    }
                }
            }

            if (product != null) {
                List<String> imageUrls = new ArrayList<>();
                try ( PreparedStatement psImages = conn.prepareStatement(imagesSql)) {
                    psImages.setInt(1, productId);
                    try ( ResultSet rsImages = psImages.executeQuery()) {
                        while (rsImages.next()) {
                            imageUrls.add(rsImages.getString("image_URL"));
                        }
                    }
                }
                product.setImageUrls(imageUrls);
                List<ProductAttribute> attributes = new ArrayList<>();
                try ( PreparedStatement psAttr = conn.prepareStatement(attributeSql)) {
                    psAttr.setInt(1, productId);
                    try ( ResultSet rsAttr = psAttr.executeQuery()) {
                        while (rsAttr.next()) {
                            ProductAttribute attr = new ProductAttribute();
                            attr.setAttributeName(rsAttr.getString("attribute_name"));
                            attr.setValue(rsAttr.getString("value"));
                            attributes.add(attr);
                        }
                    }
                }
                product.setAttributes(attributes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return product;
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setQuantity(rs.getInt("quantity"));
        product.setSalePrice(rs.getBigDecimal("sale_price"));
        product.setCategoryId(rs.getInt("category_id"));
        product.setBrandId((Integer) rs.getObject("brand_id"));
        product.setCategoryName(rs.getString("category_name"));
        product.setBrandName(rs.getString("brand_name"));
        product.setCreatedAt(rs.getTimestamp("created_at"));
        product.setUpdatedAt(rs.getTimestamp("updated_at"));
        product.setActiveProduct(rs.getInt("active_product"));
        return product;
    }

    private GameDetails mapResultSetToGameDetails(ResultSet rs) throws SQLException {
        GameDetails details = new GameDetails();
        details.setGameDetailsId(rs.getInt("game_details_id"));
        details.setDeveloper(rs.getString("developer"));
        details.setGenre(rs.getString("genre"));
        details.setReleaseDate(rs.getDate("release_date"));
        return details;
    }

    public List<Product> getAccessoryProducts() {
        List<Product> productList = new ArrayList<>();
        // Thêm "AND p.active_product = 1" vào sau điều kiện WHERE
        String sql = "SELECT p.product_id, p.name, p.price, p.sale_price, p.quantity, c.name AS category_name, b.brand_name, "
                + "(SELECT TOP 1 i.image_URL FROM image i WHERE i.product_id = p.product_id ORDER BY i.image_id) AS image_url "
                + "FROM product p "
                + "LEFT JOIN category c ON p.category_id = c.category_id "
                + "LEFT JOIN brand b ON p.brand_id = b.brand_id "
                + "WHERE p.game_details_id IS NULL AND p.active_product = 1 " // <-- THÊM VÀO ĐÂY
                + "ORDER BY p.product_id";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                productList.add(mapProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    public List<Product> getGameProducts() {
        List<Product> productList = new ArrayList<>();
        // Thêm "AND p.active_product = 1" vào sau điều kiện WHERE
        String sql = "SELECT p.product_id, p.name, p.price, p.sale_price, p.quantity, gd.developer AS brand_name, "
                + "(SELECT TOP 1 i.image_URL FROM image i WHERE i.product_id = p.product_id ORDER BY i.image_id) AS image_url "
                + "FROM product p "
                + "LEFT JOIN game_details gd ON p.game_details_id = gd.game_details_id "
                + "WHERE p.game_details_id IS NOT NULL AND p.active_product = 1 " // <-- THÊM VÀO ĐÂY
                + "ORDER BY p.product_id";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                productList.add(mapProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    private Product mapProductFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getBigDecimal("price"));
        if (hasColumn(rs, "sale_price")) {
            product.setSalePrice(rs.getBigDecimal("sale_price"));
        }
        product.setQuantity(rs.getInt("quantity"));
        product.setBrandName(rs.getString("brand_name"));
        String singleImageUrl = rs.getString("image_url");
        List<String> imageUrls = new ArrayList<>();
        if (singleImageUrl != null && !singleImageUrl.isEmpty()) {
            imageUrls.add(singleImageUrl);
        }
        product.setImageUrls(imageUrls);
        return product;
    }

    private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        try {
            rs.findColumn(columnName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Product> searchProductsByName(String query) throws SQLException {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.*, c.name as categoryName, b.brand_name as brandName "
                + "FROM Products p "
                + "LEFT JOIN Categories c ON p.category_id = c.category_id "
                + "LEFT JOIN Brands b ON p.brand_id = b.brand_id "
                + "WHERE LOWER(p.name) LIKE ?";

        try (
                 PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + query.toLowerCase() + "%");

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setProductId(rs.getInt("product_id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getBigDecimal("price"));
                    product.setSalePrice(rs.getBigDecimal("sale_price"));
                    product.setQuantity(rs.getInt("quantity"));
                    product.setCategoryId(rs.getInt("category_id"));
                    product.setBrandId(rs.getInt("brand_id"));
                    product.setGameDetailsId(rs.getInt("game_details_id"));
                    product.setCreatedAt(rs.getTimestamp("created_at"));
                    product.setUpdatedAt(rs.getTimestamp("updated_at"));
                    product.setActiveProduct(rs.getInt("active_product"));

                    product.setCategoryName(rs.getString("categoryName"));
                    product.setBrandName(rs.getString("brandName"));

                    productList.add(product);
                }
            }
        }
        return productList;
    }

    public int writeReviewIntoDb(int[] productId, int customerId, int value, int orderId) throws SQLException {
        StringBuilder query = new StringBuilder("INSERT INTO star_review (product_id, customer_id, value, order_id)\n"
                + "VALUES ");
        Object[] params = new Object[productId.length * 4];
        int index = 0;

        for (int i = 0; i < productId.length; i++) {
            query.append("(?, ?, ?, ?)");
            if (i < productId.length - 1) {
                query.append(", ");
            }
            params[index++] = productId[i];
            params[index++] = customerId;
            params[index++] = value;
            params[index++] = orderId; // dùng cùng một order_id cho tất cả
        }

        return execQuery(query.toString(), params);
    }

   public boolean isOrderReviewed(int orderId, int customerId) throws SQLException {
        String query = "SELECT COUNT(*) FROM star_review sr "
                + "JOIN order_detail od ON sr.product_id = od.product_id "
                + "WHERE od.order_id = ? AND sr.customer_id = ? AND od.order_id = sr.order_id";
        Object[] params = {orderId, customerId};
        ResultSet rs = execSelectQuery(query, params);
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }

    public double getAverageStarsForProduct(int productId) {
        // ISNULL (hoặc COALESCE cho các CSDL khác) để trả về 0 nếu kết quả AVG là NULL
        String sql = "SELECT ISNULL(AVG(CAST(value AS FLOAT)), 0.0) AS average_stars "
                + "FROM star_review WHERE product_id = ?";
        double averageStars = 0.0;

        // Sử dụng try-with-resources để đảm bảo kết nối được đóng đúng cách
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    averageStars = rs.getDouble("average_stars");
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy sao trung bình cho product_id " + productId);
            e.printStackTrace();
        }
        return averageStars;
    }

    /**
     * Cập nhật trạng thái HIỂN THỊ SẢN PHẨM (cột 'active_product').
     */
    public boolean updateProductVisibility(int productId, int newStatus) throws SQLException {
        String sql = "UPDATE product SET active_product = ?, updated_at = GETDATE() WHERE product_id = ?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newStatus);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        }
    }
}
