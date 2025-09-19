/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import DAO.ProductDAO;
import Models.Product;

@WebServlet("/editProduct")
public class EditProductController extends HttpServlet {

  
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {

        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.trim().isEmpty()) {
                int id = Integer.parseInt(idStr);
                Product product = ProductDAO.getProductByID(id);
                if (product != null) {
                    request.setAttribute("product", product);
                    request.getRequestDispatcher("editProduct.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading product");
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy dữ liệu từ form
        String idStr = request.getParameter("id");
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

            // Load lại product để hiển thị
            try {
                int id = Integer.parseInt(idStr);
                Product product = ProductDAO.getProductByID(id);
                request.setAttribute("product", product);
            } catch (Exception e) {
                e.printStackTrace();
            }

            request.getRequestDispatcher("editProduct.jsp").forward(request, response);
            return;
        }

        // Nếu valid, update
        try {
            int productID = Integer.parseInt(idStr);
            int rows = ProductDAO.updateProduct(productID, productName, unitPrice, categoryID, quantityPerUnit, productImage, supplierID, isActive);
            if (rows > 0) {
                response.sendRedirect("AdminController");
            } else {
                request.setAttribute("errorGeneral", "Failed to update product.");
                // Load lại product
                Product product = ProductDAO.getProductByID(productID);
                request.setAttribute("product", product);
                request.getRequestDispatcher("editProduct.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorGeneral", "Error updating product.");
            // Load lại product
            try {
                int productID = Integer.parseInt(idStr);
                Product product = ProductDAO.getProductByID(productID);
                request.setAttribute("product", product);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            request.getRequestDispatcher("editProduct.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
