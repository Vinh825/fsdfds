/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import shop.db.DBContext;
import shop.model.Brand;

/**
 *
 * @author HoangSang
 */
public class BrandDAO extends DBContext{
    public List<Brand> getAllBrands() throws SQLException {
        List<Brand> brands = new ArrayList<>();
        String sql = "SELECT brand_id, brand_name, country FROM brand ORDER BY brand_name";
        try (
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Brand brand = new Brand();
                brand.setBrandId(rs.getInt("brand_id"));
                brand.setBrandName(rs.getString("brand_name"));
                brand.setCountry(rs.getString("country"));
                brands.add(brand);
            }
        }
        return brands;
    }
}
