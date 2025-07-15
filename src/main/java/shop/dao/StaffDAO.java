/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import shop.db.DBContext;
import shop.model.Staff;
import java.util.logging.Logger;
import shop.controller.StaffServlet;

/**
 *
 * @author VICTUS
 */
public class StaffDAO extends DBContext {

    public ArrayList<Staff> getList(int currentPage) {
        ArrayList<Staff> staffs = new ArrayList<>();
        String query = "SELECT * FROM staff ORDER BY staff_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        Object[] params = {
            (currentPage - 1) * StaffServlet.PAGE_SIZE,
            StaffServlet.PAGE_SIZE
        };

        try ( ResultSet rs = execSelectQuery(query, params)) {
            while (rs.next()) {
                staffs.add(new Staff(
                        rs.getInt("staff_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("gender"),
                        rs.getString("phone"),
                        rs.getString("role"),
                        rs.getDate("date_of_birth"),
                        rs.getString("avatar_url")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StaffDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return staffs;
    }

    public int create(Staff staff) {
        try {
            String sql = "INSERT INTO staff (full_name, email, password_hash, gender, phone, role, date_of_birth, avatar_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            Object[] params = {
                staff.getFullName(),
                staff.getEmail(),
                staff.getPasswordHash(),
                staff.getGender(),
                staff.getPhone(),
                staff.getRole(),
                staff.getDateOfBirth(),
                staff.getAvatarUrl()
            };
            return execQuery(sql, params);
        } catch (SQLException ex) {
            Logger.getLogger(StaffDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public Staff getOneById(int id) {
        try {
            String query = "SELECT * FROM staff WHERE staff_id = ?";
            Object[] params = {id};
            ResultSet rs = execSelectQuery(query, params);

            if (rs.next()) {
                return new Staff(
                        rs.getInt("staff_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("gender"),
                        rs.getString("phone"),
                        rs.getString("role"),
                        rs.getDate("date_of_birth"),
                        rs.getString("avatar_url")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(StaffDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Staff getOneByEmail(String email) {
        try {
            String query = "SELECT * FROM staff WHERE email = ?";
            Object[] params = {email};
            ResultSet rs = execSelectQuery(query, params);

            if (rs.next()) {
                return new Staff(
                        rs.getInt("staff_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("gender"),
                        rs.getString("phone"),
                        rs.getString("role"),
                        rs.getDate("date_of_birth"),
                        rs.getString("avatar_url")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(StaffDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int update(Staff staff) {
        String sql = "UPDATE staff SET full_name = ?, email = ?, password_hash = ?, gender = ?, phone = ?, role = ?, date_of_birth = ?, avatar_url = ? WHERE staff_id = ?";
        Object[] params = {
            staff.getFullName(),
            staff.getEmail(),
            staff.getPasswordHash(),
            staff.getGender(),
            staff.getPhone(),
            staff.getRole(),
            staff.getDateOfBirth(),
            staff.getAvatarUrl(),
            staff.getId()
        };

        try {
            return execQuery(sql, params);
        } catch (SQLException ex) {
            Logger.getLogger(StaffDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int delete(int id) {
        String sql = "DELETE FROM staff WHERE staff_id = ?";
        Object[] params = {id};

        try {
            return execQuery(sql, params);
        } catch (SQLException ex) {
            return 0;
        }
    }

    public ArrayList<Staff> searchByName(String keyword) throws SQLException {
        ArrayList<Staff> staffs = new ArrayList<>();
        String query = "SELECT * FROM staff WHERE full_name LIKE ?";
        Object[] params = {"%" + keyword + "%"};
        ResultSet rs = execSelectQuery(query, params);

        while (rs.next()) {
            staffs.add(new Staff(
                    rs.getInt("staff_id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password_hash"),
                    rs.getString("gender"),
                    rs.getString("phone"),
                    rs.getString("role"),
                    rs.getDate("date_of_birth"),
                    rs.getString("avatar_url")
            ));
        }

        return staffs;
    }

    public int countStaffs() {
        try {
            String query = "SELECT COUNT(*) FROM staff";
            ResultSet rs = execSelectQuery(query);
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(StaffDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public boolean isEmailTakenByOthers(String email, int id) {
        try {
            String query = "SELECT COUNT(*) FROM staff \n"
                    + "WHERE (email = ?) AND staff_id != ?";
            Object[] params = {email, id};
            ResultSet rs = execSelectQuery(query, params);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public int updateProfileAdmin(Staff staff) {
        try {
            String query = "UPDATE staff\n"
                    + "SET full_name = ?,\n"
                    + "	email = ?,\n"
                    + "	phone = ?,\n"
                    + "	gender = ?,\n"
                    + "	avatar_url = ?,\n"
                    + "	date_of_birth = ?\n"
                    + "WHERE staff_id = ?";
            Object[] params = {
                staff.getFullName(),
                staff.getEmail(),
                staff.getPhone(),
                staff.getGender(),
                staff.getAvatarUrl(),
                staff.getDateOfBirth(),
                staff.getId()
            };
            return execQuery(query, params);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int updateAdminPassword(Staff staff) {
        try {
            String query = "UPDATE staff\n"
                    + "SET password_hash = ?\n"
                    + "WHERE email = ?";
            Object[] params = {
                staff.getPasswordHash(),
                staff.getEmail()
            };
            return execQuery(query, params);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
