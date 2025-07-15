/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import shop.db.DBContext;
import shop.model.StorePlatform;

/**
 *
 * @author HoangSang
 */
public class StorePlatformDAO extends DBContext {

    private void closeResources(ResultSet rs, PreparedStatement ps, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<StorePlatform> getAllPlatforms() throws SQLException {
        List<StorePlatform> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null; 
        String query = "SELECT platform_id, store_OS_name FROM store_platform WHERE game_details_id IS NULL ORDER BY store_OS_name"; 
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new StorePlatform(rs.getInt("platform_id"), rs.getString("store_OS_name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Error fetching all master Store Platforms: " + e.getMessage(), e);
        } finally {
            closeResources(rs, ps, conn);
        }
        return list;
    }

    public List<StorePlatform> findByGameDetailsId(int gameDetailsId) throws SQLException {
        List<StorePlatform> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT platform_id, store_OS_name, game_details_id FROM store_platform WHERE game_details_id = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, gameDetailsId);
            rs = ps.executeQuery();
            while (rs.next()) {
                StorePlatform sp = new StorePlatform();
                sp.setPlatformId(rs.getInt("platform_id"));
                sp.setStoreOSName(rs.getString("store_OS_name"));
                sp.setGameDetailsId(rs.getInt("game_details_id"));
                list.add(sp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Error finding Store Platforms by GameDetails ID: " + e.getMessage(), e);
        } finally {
            closeResources(rs, ps, conn);
        }
        return list;
    }

    public void deleteByGameDetailsId(int gameDetailsId) throws SQLException {
        PreparedStatement ps = null;
        String query = "DELETE FROM store_platform WHERE game_details_id = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, gameDetailsId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Error deleting Store Platforms by GameDetails ID: " + e.getMessage(), e);
        } finally {
            closeResources(null, ps, conn);
        }
    }

    public String getStorePlatformNameById(int platformId) throws SQLException {
        String platformName = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT store_OS_name FROM store_platform WHERE platform_id = ? AND game_details_id IS NULL";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, platformId);
            rs = ps.executeQuery();

            if (rs.next()) {
                platformName = rs.getString("store_OS_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Error fetching Store Platform name by ID: " + e.getMessage(), e);
        } finally {
            closeResources(rs, ps, conn);
        }
        return platformName;
    }

    public int getMasterStorePlatformIdByName(String platformName) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT platform_id FROM store_platform WHERE store_OS_name = ? AND game_details_id IS NULL";
        int masterPlatformId = -1; 

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, platformName);
            rs = ps.executeQuery();

            if (rs.next()) {
                masterPlatformId = rs.getInt("platform_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Error fetching master Store Platform ID by name: " + e.getMessage(), e);
        } finally {
            closeResources(rs, ps, conn);
        }
        return masterPlatformId;
    }
}
