/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author QUYDAM
 */
public class SalesReport {
    private int productID;
    private String productName;
    private int totalQuantity;
    private double totalSales;

    // Constructor rỗng
    public SalesReport() {
    }

    // Constructor đầy đủ
    public SalesReport(int productID, String productName, int totalQuantity, double totalSales) {
        this.productID = productID;
        this.productName = productName;
        this.totalQuantity = totalQuantity;
        this.totalSales = totalSales;
    }

    // Getter và Setter
    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    @Override
    public String toString() {
        return "SalesReport{" +
                "productID=" + productID +
                ", productName='" + productName + '\'' +
                ", totalQuantity=" + totalQuantity +
                ", totalSales=" + totalSales +
                '}';
    }
}
