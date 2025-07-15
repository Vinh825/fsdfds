/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.model;

import java.math.BigDecimal;

/**
 *
 * @author ADMIN
 */
public class OrderDetails {

    private int orderDetailId;
    private int orderId;
    private int productId;
    private int quantity;
    private BigDecimal price;
    private String productName;
    private String imageURL;
    private String categoryName;
    private String gameKey ;

    public OrderDetails(int orderDetailId, int orderId, int productId, int quantity, BigDecimal price, String productName) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.productName = productName;
    }

    public OrderDetails(int productId, int quantity, BigDecimal price, String productName, String imageURL, String categoryName) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.productName = productName;
        this.imageURL = imageURL;
        this.categoryName = categoryName;
    }
    public OrderDetails(int productId, int quantity, BigDecimal price, String productName, String imageURL, String categoryName, String gameKey) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.productName = productName;
        this.imageURL = imageURL;
        this.categoryName = categoryName;
        this.gameKey = gameKey;
    }

    public OrderDetails() {
    }

    public String getGameKey() {
        return gameKey;
    }

    public void setGameKey(String gameKey) {
        this.gameKey = gameKey;
    }
    

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
