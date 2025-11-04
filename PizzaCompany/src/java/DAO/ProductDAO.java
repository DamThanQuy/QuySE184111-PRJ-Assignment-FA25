package DAO;

import Models.Product;
import Utils.DbUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {


    public static List<Product> getAllProducts() throws Exception {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.CategoryName FROM Products p JOIN Categories c ON p.CategoryID = c.CategoryID WHERE p.isActive = 1";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Product p = new Product();
                p.setProductID(rs.getInt("ProductID"));
                p.setProductName(rs.getString("ProductName"));
                p.setSupplierID(rs.getInt("SupplierID"));
                p.setCategoryID(rs.getInt("CategoryID"));
                p.setQuantityPerUnit(rs.getString("QuantityPerUnit"));
                p.setUnitPrice(rs.getDouble("UnitPrice"));
                p.setProductImage(rs.getString("ProductImage"));
                p.setIsActive(rs.getBoolean("isActive"));
                // joined fields
                try {
                    p.setCategoryName(rs.getString("CategoryName"));
                } catch (Exception ignore) {}
                try {
                    p.setDescription(rs.getString("Description"));
                } catch (Exception ignore) {}
                list.add(p);
            }
            return list;
        }
    }

    public static List<Product> getAllProductsForAdmin() throws Exception {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.CategoryName FROM Products p JOIN Categories c ON p.CategoryID = c.CategoryID";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Product p = new Product();
                p.setProductID(rs.getInt("ProductID"));
                p.setProductName(rs.getString("ProductName"));
                p.setSupplierID(rs.getInt("SupplierID"));
                p.setCategoryID(rs.getInt("CategoryID"));
                p.setQuantityPerUnit(rs.getString("QuantityPerUnit"));
                p.setUnitPrice(rs.getDouble("UnitPrice"));
                p.setProductImage(rs.getString("ProductImage"));
                p.setIsActive(rs.getBoolean("isActive"));
                // joined fields
                try {
                    p.setCategoryName(rs.getString("CategoryName"));
                } catch (Exception ignore) {}
                try {
                    p.setDescription(rs.getString("Description"));
                } catch (Exception ignore) {}
                list.add(p);
            }
            return list;
        }
    }

    public static Product getProductByID(int id) throws Exception {
        String sql = "SELECT p.*, c.CategoryName, s.CompanyName as SupplierName " +
                "FROM Products p " +
                "JOIN Categories c ON p.CategoryID = c.CategoryID " +
                "JOIN Suppliers s ON p.SupplierID = s.SupplierID " +
                "WHERE p.ProductID = ?";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Product p = new Product();
                    p.setProductID(rs.getInt("ProductID"));
                    p.setProductName(rs.getString("ProductName"));
                    p.setSupplierID(rs.getInt("SupplierID"));
                    p.setCategoryID(rs.getInt("CategoryID"));
                    p.setQuantityPerUnit(rs.getString("QuantityPerUnit"));
                    p.setUnitPrice(rs.getDouble("UnitPrice"));
                    p.setProductImage(rs.getString("ProductImage"));
                    p.setIsActive(rs.getBoolean("isActive"));
                    // joined fields
                    try {
                        p.setCategoryName(rs.getString("CategoryName"));
                    } catch (Exception ignore) {}
                    try {
                        p.setSupplierName(rs.getString("SupplierName"));
                    } catch (Exception ignore) {}
                    try {
                        p.setDescription(rs.getString("Description"));
                    } catch (Exception ignore) {}
                    return p;
                }
            }
        }
        return null;
    }

    public static List<Product> searchProducts(String keyword, Double minPrice, Double maxPrice, Integer categoryID) throws Exception {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.CategoryName FROM Products p JOIN Categories c ON p.CategoryID = c.CategoryID WHERE 1=1";
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += " AND p.ProductName LIKE ?";
            params.add("%" + keyword.trim() + "%");
        }
        if (minPrice != null) {
            sql += " AND p.UnitPrice >= ?";
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql += " AND p.UnitPrice <= ?";
            params.add(maxPrice);
        }
        if (categoryID != null) {
            sql += " AND p.CategoryID = ?";
            params.add(categoryID);
        }

        try (Connection conn = DbUtils.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setProductID(rs.getInt("ProductID"));
                    p.setProductName(rs.getString("ProductName"));
                    p.setSupplierID(rs.getInt("SupplierID"));
                    p.setCategoryID(rs.getInt("CategoryID"));
                    p.setQuantityPerUnit(rs.getString("QuantityPerUnit"));
                    p.setUnitPrice(rs.getDouble("UnitPrice"));
                    p.setProductImage(rs.getString("ProductImage"));
                    p.setIsActive(rs.getBoolean("isActive"));
                    // joined fields
                    try {
                        p.setCategoryName(rs.getString("CategoryName"));
                    } catch (Exception ignore) {}
                    try {
                        p.setDescription(rs.getString("Description"));
                    } catch (Exception ignore) {}
                    list.add(p);
                }
            }
        }
        return list;
    }


    public static int insertProduct(String productName, Double unitPrice, int categoryID, String quantityPerUnit, String productImage, int supplierID, boolean isActive) throws Exception {
        String sql = "INSERT INTO Products (ProductName, SupplierID, CategoryID, QuantityPerUnit, UnitPrice, ProductImage, isActive) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbUtils.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, productName);
            st.setInt(2, supplierID);
            st.setInt(3, categoryID);
            st.setString(4, quantityPerUnit);
            st.setDouble(5, unitPrice);
            st.setString(6, productImage);
            st.setBoolean(7, isActive);
            return st.executeUpdate(); // Trả về số hàng inserted
        }
    }

    public static int updateProduct(int productID, String productName, Double unitPrice, int categoryID, String quantityPerUnit, String productImage, int supplierID, boolean isActive) throws Exception {
        String sql = "UPDATE Products SET ProductName = ?, SupplierID = ?, CategoryID = ?, QuantityPerUnit = ?, UnitPrice = ?, ProductImage = ?, isActive = ? WHERE ProductID = ?";

        try (Connection conn = DbUtils.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, productName);
            st.setInt(2, supplierID);
            st.setInt(3, categoryID);
            st.setString(4, quantityPerUnit);
            st.setDouble(5, unitPrice);
            st.setString(6, productImage);
            st.setBoolean(7, isActive);
            st.setInt(8, productID);
            return st.executeUpdate(); // Trả về số hàng affected
        }
    }

    public static int deleteProduct(int productID) throws Exception {
        String sql = "UPDATE Products SET isActive = 0 WHERE ProductID = ?";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, productID);
            return st.executeUpdate(); // số hàng được cập nhật (set isActive = 0)
        }
    }

}
