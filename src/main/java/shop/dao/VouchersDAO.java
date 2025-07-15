/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import shop.db.DBContext;
import shop.model.Voucher;

/**
 *
 * @author PC
 */
public class VouchersDAO extends DBContext {

    public ArrayList<Voucher> getList() {
        ArrayList<Voucher> codes = new ArrayList<>();
        try {
            String query = "SELECT * from voucher  ";

            ResultSet rs = execSelectQuery(query);
            while (rs.next()) {
                Voucher voucher = new Voucher(
                        rs.getInt("voucher_id"),
                        rs.getString("code"),
                        rs.getBigDecimal("value"),
                        rs.getInt("usage_limit"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getInt("active"),
                        rs.getString("description"),
                        rs.getBigDecimal("min_order_value")
                );
                codes.add(voucher);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VouchersDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return codes;
    }

    public Voucher getOne(int id) {
        try {
            String query = "SELECT * FROM voucher WHERE voucher_id = ?;";
            Object[] params = {id};
            ResultSet rs = execSelectQuery(query, params);

            if (rs.next()) {
                return new Voucher(
                        rs.getInt("voucher_id"),
                        rs.getString("code"),
                        rs.getBigDecimal("value"),
                        rs.getInt("usage_limit"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getInt("active"),
                        rs.getString("description"),
                        rs.getBigDecimal("min_order_value")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(VouchersDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean isDuplicateCodeForOtherVoucher(String code, int id) {
        try {
            String query = "SELECT COUNT(*) FROM voucher WHERE code = ? AND voucher_id != ?";
            Object[] params = {code, id};
            ResultSet rs = execSelectQuery(query, params);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(VouchersDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean isDuplicateCodeForOtherVoucherOfTheCreate(String code) {
        try {
            String query = "SELECT COUNT(*) FROM voucher WHERE code = ?";
            Object[] params = {code};
            ResultSet rs = execSelectQuery(query, params);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(VouchersDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public int editVoucherCode(Voucher code) {
        try {
            String query
                    = "UPDATE voucher\n"
                    + "SET code = ?,\n"
                    + "    value = ?,\n"
                    + "    usage_limit = ?,\n"
                    + "    start_date = ?,\n"
                    + "    end_date = ?,\n"
                    + "    active = ?,\n"
                    + "    description = ?,\n"
                    + "    min_order_value = ?\n"
                    + "WHERE voucher_id = ?;";
            Object[] params = {code.getCode(), code.getValue(), code.getUsageLimit(), code.getStartDate(), code.getEndDate(), code.getActive(), code.getDescription(), code.getMinOrderValue(), code.getVoucherId()};
            return execQuery(query, params);
        } catch (SQLException ex) {
            Logger.getLogger(VouchersDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int createVoucherCode(Voucher code) {
        try {
            String query = "INSERT INTO voucher (code, value, usage_limit, start_date, end_date, active, description, min_order_value) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            Object[] params = {
                code.getCode(),
                code.getValue(),
                code.getUsageLimit(),
                code.getStartDate(),
                code.getEndDate(),
                code.getActive(),
                code.getDescription(),
                code.getMinOrderValue()
            };
            return execQuery(query, params);
        } catch (SQLException ex) {
            Logger.getLogger(VouchersDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public ArrayList<Voucher> searchVoucherByCode(String keyword) {
        ArrayList<Voucher> vouchers = new ArrayList<>();
        String query = "SELECT * FROM voucher WHERE LOWER(code) LIKE LOWER(?)";

        try ( PreparedStatement stmt = this.getConnection().prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Voucher v = new Voucher(
                        rs.getInt("voucher_id"),
                        rs.getString("code"),
                        rs.getBigDecimal("value"),
                        rs.getInt("usage_limit"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getInt("active"),
                        rs.getString("description"),
                        rs.getBigDecimal("min_order_value")
                );
                vouchers.add(v);
            }
        } catch (SQLException e) {
            Logger.getLogger(VouchersDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return vouchers;
    }

    public int deleteVoucher(int id) {
        try {
            String query = "DELETE FROM voucher WHERE voucher_id = ?";
            Object[] params = {id};
            return execQuery(query, params);
        } catch (SQLException ex) {
            Logger.getLogger(VouchersDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public String[] checkCodeIsValid(String code) throws SQLException {
        String[] check = new String[3];
        String query = "SELECT \n"
                + "    CASE \n"
                + "        WHEN EXISTS (\n"
                + "            SELECT 1 \n"
                + "            FROM Voucher \n"
                + "            WHERE code = ? \n"
                + "              AND active = 1 \n"
                + "              AND GETDATE() BETWEEN start_date AND end_date\n"
                + "        ) THEN 'Valid'\n"
                + "        ELSE 'Invalid'\n"
                + "    END AS VoucherStatus,\n"
                + "    \n"
                + "    -- Lấy thêm value và min_order_value nếu hợp lệ, ngược lại để NULL\n"
                + "    (SELECT value \n"
                + "     FROM Voucher \n"
                + "     WHERE code = ? \n"
                + "       AND active = 1 \n"
                + "       AND GETDATE() BETWEEN start_date AND end_date) AS value,\n"
                + "\n"
                + "    (SELECT min_order_value \n"
                + "     FROM Voucher \n"
                + "     WHERE code = ? \n"
                + "       AND active = 1 \n"
                + "       AND GETDATE() BETWEEN start_date AND end_date) AS min_order_value;";
        Object[] params = {code, code, code};
        ResultSet rs = execSelectQuery(query, params);
        while (rs.next()) {
            check[0] = rs.getString("VoucherStatus");
            check[1] = rs.getString("value");
            check[2] = rs.getString("min_order_value");
        }
        return check;
    }

    public int getVoucherIdByCode(String code) throws SQLException {
        int id = 0;
        String query = "select voucher_id \n"
                + "from voucher\n"
                + "where code = ?;";
        Object[] params = {code};
        ResultSet rs = execSelectQuery(query, params);
        while (rs.next()) {
            id = rs.getInt(1);
        }
        return id;
    }

    public int decreaseLimit(int voucherId) throws SQLException {
        String query = "UPDATE voucher\n"
                + "SET usage_limit = usage_limit - 1\n"
                + "WHERE voucher_id = ? AND usage_limit > 0;";
        Object[] params = {voucherId};
        return execQuery(query, params);

    }

}
