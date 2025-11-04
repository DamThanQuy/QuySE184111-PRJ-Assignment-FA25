
package DAO;

import Models.Supplier;
import Utils.DbUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    public static List<Supplier> getAllSuppliers() throws Exception {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT SupplierID, CompanyName, Address, Phone FROM Suppliers ORDER BY CompanyName";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Supplier s = new Supplier();
                s.setSupplierID(rs.getInt("SupplierID"));
                s.setCompanyName(rs.getString("CompanyName"));
                s.setAddress(rs.getString("Address"));
                s.setPhone(rs.getString("Phone"));
                list.add(s);
            }
        }
        return list;
    }
}
