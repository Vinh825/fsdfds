/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import shop.db.DBContext;

/**
 *
 * @author HoangSang
 */
public class AttributeDAO extends DBContext{
    public int getAttributeIdByName(String name) throws SQLException {
        String sql = "SELECT attribute_id FROM attribute WHERE name = ?";
        try (
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("attribute_id");
                }
            }
        }
        throw new SQLException("Attribute not found: " + name);
    }
   
}
