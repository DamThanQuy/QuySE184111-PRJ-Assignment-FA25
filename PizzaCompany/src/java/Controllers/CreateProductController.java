package Controllers;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import DAO.ProductDAO;
import DAO.CategoryDAO;
import DAO.SupplierDAO; // Thêm import
import Models.Product;
import Models.Category;
import Models.Supplier; // Thêm import
import java.util.List;


@WebServlet("/createProduct")
public class CreateProductController extends HttpServlet {

   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy danh sách category để hiển thị trong dropdown
            List<Category> categories = CategoryDAO.getAllCategories();
            request.setAttribute("categories", categories);
            
            // Lấy danh sách supplier để hiển thị trong dropdown
            List<Supplier> suppliers = SupplierDAO.getAllSuppliers();
            request.setAttribute("suppliers", suppliers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.getRequestDispatcher("createProduct.jsp").forward(request, response);
    }

  
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy dữ liệu từ form
        String productName = request.getParameter("productName");
        String unitPriceStr = request.getParameter("unitPrice");
        String categoryIDStr = request.getParameter("categoryID");
        String quantityPerUnit = request.getParameter("quantityPerUnit");
        String productImage = request.getParameter("productImage");
        String supplierIDStr = request.getParameter("supplierID");
        String isActiveStr = request.getParameter("isActive");

        // Validation
        StringBuilder errors = new StringBuilder();
        if (productName == null || productName.trim().isEmpty()) {
            request.setAttribute("errorProductName", "Product name is required.");
            errors.append("Product name is required. ");
        }
        Double unitPrice = null;
        try {
            unitPrice = Double.parseDouble(unitPriceStr);
            if (unitPrice <= 0) {
                request.setAttribute("errorUnitPrice", "Unit price must be greater than 0.");
                errors.append("Unit price must be greater than 0. ");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorUnitPrice", "Unit price must be a valid number.");
            errors.append("Unit price must be a valid number. ");
        }
        Integer categoryID = null;
        try {
            categoryID = Integer.parseInt(categoryIDStr);
            if (categoryID <= 0) {
                request.setAttribute("errorCategoryID", "Category ID must be greater than 0.");
                errors.append("Category ID must be greater than 0. ");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorCategoryID", "Category ID must be a valid number.");
            errors.append("Category ID must be a valid number. ");
        }
        Integer supplierID = null;
        try {
            supplierID = Integer.parseInt(supplierIDStr);
            if (supplierID <= 0) {
                request.setAttribute("errorSupplierID", "Supplier ID must be greater than 0.");
                errors.append("Supplier ID must be greater than 0. ");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorSupplierID", "Supplier ID must be a valid number.");
            errors.append("Supplier ID must be a valid number. ");
        }
        boolean isActive = Boolean.parseBoolean(isActiveStr);

        // Nếu có lỗi, forward lại form với dữ liệu cũ và lỗi
        if (errors.length() > 0) {
            // Giữ lại dữ liệu đã nhập
            request.setAttribute("productName", productName);
            request.setAttribute("unitPrice", unitPriceStr);
            request.setAttribute("categoryID", categoryIDStr);
            request.setAttribute("quantityPerUnit", quantityPerUnit);
            request.setAttribute("productImage", productImage);
            request.setAttribute("supplierID", supplierIDStr);
            request.setAttribute("isActive", isActiveStr);
            
            // Truyền lại danh sách category và supplier
            try {
                List<Category> categories = CategoryDAO.getAllCategories();
                request.setAttribute("categories", categories);
                List<Supplier> suppliers = SupplierDAO.getAllSuppliers();
                request.setAttribute("suppliers", suppliers);
            } catch (Exception e) {
                e.printStackTrace();
            }

            request.getRequestDispatcher("createProduct.jsp").forward(request, response);
            return;
        }

        // Nếu valid, insert
        try {
            int rows = ProductDAO.insertProduct(productName, unitPrice, categoryID, quantityPerUnit, productImage, supplierID, isActive);
            if (rows > 0) {
                response.sendRedirect("AdminController");
            } else {
                request.setAttribute("errorGeneral", "Failed to create product.");
                request.getRequestDispatcher("createProduct.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorGeneral", "Error creating product.");
            request.getRequestDispatcher("createProduct.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
