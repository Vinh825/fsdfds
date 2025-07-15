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
import shop.model.OperatingSystem;

/**
 *
 * @author HoangSang
 */
public class OperatingSystemDAO extends DBContext {

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

    public List<OperatingSystem> getAllOperatingSystems() throws SQLException {
        List<OperatingSystem> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT os_id, os_name FROM operating_system WHERE game_details_id IS NULL ORDER BY os_name";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new OperatingSystem(rs.getInt("os_id"), rs.getString("os_name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Error fetching all master Operating Systems: " + e.getMessage(), e);
        } finally {
            closeResources(rs, ps, conn);
        }
        return list;
    }

    public String getOsNameById(int osId) throws SQLException {
        String osName = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT os_name FROM operating_system WHERE os_id = ? AND game_details_id IS NULL";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, osId);
            rs = ps.executeQuery();

            if (rs.next()) {
                osName = rs.getString("os_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Error fetching Operating System name by ID: " + e.getMessage(), e);
        } finally {
            closeResources(rs, ps, conn);
        }
        return osName;
    }

    public List<OperatingSystem> findByGameDetailsId(int gameDetailsId) throws SQLException {
        List<OperatingSystem> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT os_id, os_name, game_details_id FROM operating_system WHERE game_details_id = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, gameDetailsId);
            rs = ps.executeQuery();
            while (rs.next()) {
                OperatingSystem os = new OperatingSystem();
                os.setOsId(rs.getInt("os_id"));
                os.setOsName(rs.getString("os_name"));
                os.setGameDetailsId(rs.getInt("game_details_id"));
                list.add(os);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Error finding Operating Systems by GameDetails ID: " + e.getMessage(), e);
        } finally {
            closeResources(rs, ps, conn);
        }
        return list;
    }

    public void deleteByGameDetailsId(int gameDetailsId) throws SQLException {
        PreparedStatement ps = null;
        String query = "DELETE FROM operating_system WHERE game_details_id = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, gameDetailsId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Error deleting Operating Systems by GameDetails ID: " + e.getMessage(), e);
        } finally {
            closeResources(null, ps, conn);
        }
    }

    public int getMasterOsIdByName(String osName) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT os_id FROM operating_system WHERE os_name = ? AND game_details_id IS NULL";
        int masterOsId = -1; 

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, osName);
            rs = ps.executeQuery();

            if (rs.next()) {
                masterOsId = rs.getInt("os_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Error fetching master OS ID by name: " + e.getMessage(), e);
        } finally {
            closeResources(rs, ps, conn);
        }
        return masterOsId;
    }
}
