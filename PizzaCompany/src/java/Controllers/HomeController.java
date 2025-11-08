package Controllers;

import DAO.ProductDAO;
import DAO.CategoryDAO;
import Models.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author QUYDAM
 */
public class HomeController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
       
        }
    } 

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            //Lấy tham số tìm kiếm từ request
            String keyword = request.getParameter("keyword");
            String minPriceStr = request.getParameter("minPrice");
            String maxPriceStr = request.getParameter("maxPrice");
            // Chuyển đổi chuỗi giá trị từ request sang Integer
            Double minPrice = (minPriceStr != null && !minPriceStr.trim().isEmpty()) ? Double.parseDouble(minPriceStr.trim()) : null;
            Double maxPrice = (maxPriceStr != null && !maxPriceStr.trim().isEmpty()) ? Double.parseDouble(maxPriceStr.trim()) : null;
            // Lấy tham số categoryID từ navbar nếu user ko search
            String categoryStr = request.getParameter("category");
            Integer categoryID = null;
            if (categoryStr != null && !categoryStr.trim().isEmpty()) {
                try { categoryID = Integer.parseInt(categoryStr.trim()); } 
                catch (NumberFormatException ignore) {}
            }
            
            List<Product> products;
            // Xác định có tìm kiếm hay không
            boolean hasSearch = (keyword != null && !keyword.trim().isEmpty()) || minPrice != null || maxPrice != null || categoryID != null;
            // Gọi ProductDAO để lấy danh sách sản phẩm
            if (hasSearch) {
                products = ProductDAO.searchProducts(keyword, minPrice, maxPrice, categoryID);
            } else {
                products = ProductDAO.getAllProducts();
            }
          
            // nạp categories cho navbar
            request.setAttribute("categoriesMenu", CategoryDAO.getAllCategories());

            request.setAttribute("products", products);
            request.getRequestDispatcher("home.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    } 


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    // Hàm main để test riêng
//    public static void main(String[] args) {
//        try {
//            List<Product> products = ProductDAO.getAllProducts();
//            System.out.println("Danh sách sản phẩm:");
//            for (Product p : products) {
//                System.out.println("ID: " + p.getProductID() + ", Name: " + p.getProductName() + ", Price: " + p.getUnitPrice() + ", Category: " + p.getCategoryName());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
