package Controllers;

import DAO.ProductDAO;
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
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet HomeController</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet HomeController at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            String keyword = request.getParameter("keyword");
            String minPriceStr = request.getParameter("minPrice");
            String maxPriceStr = request.getParameter("maxPrice");
            
            Double minPrice = (minPriceStr != null && !minPriceStr.trim().isEmpty()) ? Double.parseDouble(minPriceStr.trim()) : null;
            Double maxPrice = (maxPriceStr != null && !maxPriceStr.trim().isEmpty()) ? Double.parseDouble(maxPriceStr.trim()) : null;
            
            List<Product> products;
            if ((keyword != null && !keyword.trim().isEmpty()) || minPrice != null || maxPrice != null) {
                products = ProductDAO.searchProducts(keyword, minPrice, maxPrice);
            } else {
                products = ProductDAO.getAllProducts();
            }
          
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
