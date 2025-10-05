/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author QUYDAM
 */
public class Cart {
    private final List<CartItem> items = new ArrayList<>();
    private double totalAmount;

    public Cart() {
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    // Thêm sản phẩm vào giỏ
    public void addItem(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return;
        }

        CartItem existing = findItem(product.getProductID());
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem(
                    product.getProductID(),
                    product.getProductName(),
                    product.getUnitPrice(),
                    quantity
            );
            items.add(newItem);
        }
        calculateTotal();
    }

    // Cập nhật số lượng của một sản phẩm trong giỏ
    public void updateItem(int productID, int quantity) {
        CartItem item = findItem(productID);
        if (item == null) {
            return;
        }
        if (quantity <= 0) {
            // Nếu số lượng <= 0 thì xóa sản phẩm khỏi giỏ
            removeItem(productID);
        } else {
            item.setQuantity(quantity);
            calculateTotal();
        }
    }

    // Xóa một sản phẩm khỏi giỏ
    public void removeItem(int productID) {
        items.removeIf(i -> i.getProductID() == productID);
        calculateTotal();
    }

    // Xóa toàn bộ giỏ hàng
    public void clearCart() {
        items.clear();
        totalAmount = 0;
    }

    // Tính tổng tiền giỏ hàng
    public void calculateTotal() {
        double sum = 0;
        for (CartItem item : items) {
            sum += item.getTotalPrice();
        }
        totalAmount = sum;
    }

    // Tìm item theo productID
    private CartItem findItem(int productID) {
        for (CartItem item : items) {
            if (item.getProductID() == productID) {
                return item;
            }
        }
        return null;
    }
}
