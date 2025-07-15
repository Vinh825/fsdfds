/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author SangNH
 */
public class Product {

    private int productId;
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;
    private BigDecimal salePrice;
    private Integer categoryId;
    private Integer brandId;
    private Integer gameDetailsId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String categoryName;
    private String brandName;
    private GameDetails gameDetails;
    private List<ProductAttribute> attributes;
    private List<String> imageUrls;
    private double averageStars;
    private int active;
    private int activeProduct;

    public int getActiveProduct() {
        return activeProduct;
    }

    public void setActiveProduct(int activeProduct) {
        this.activeProduct = activeProduct;
    }

    // Sửa lại constructor này nếu bạn có sử dụng nó ở đâu đó
    public Product(int productId, String name, BigDecimal price, BigDecimal salePrice, int active, int activeProduct) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.salePrice = salePrice;
        this.active = active; // Giả sử 'active' dùng cho mục đích khác
        this.activeProduct = activeProduct; // Đã sửa tên tham số
    }

    public Product(int productId, String name, BigDecimal price, BigDecimal salePrice, int active) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.salePrice = salePrice;
        this.active = active;
    }

    public Product(String name, BigDecimal price, BigDecimal salePrice, int active) {
        this.name = name;
        this.price = price;
        this.salePrice = salePrice;
        this.active = active;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Product() {
    }

    public Product(int productId, String name, String description, BigDecimal price, int quantity, BigDecimal salePrice, Integer categoryId, Integer brandId, Integer gameDetailsId, Timestamp createdAt, Timestamp updatedAt, String categoryName, String brandName, GameDetails gameDetails, List<ProductAttribute> attributes, List<String> imageUrls, double averageStars) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.salePrice = salePrice;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.gameDetailsId = gameDetailsId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.categoryName = categoryName;
        this.brandName = brandName;
        this.gameDetails = gameDetails;
        this.attributes = attributes;
        this.imageUrls = imageUrls;
        this.averageStars = averageStars;
    }

    public double getAverageStars() {
        return averageStars;
    }

    public void setAverageStars(double averageStars) {
        this.averageStars = averageStars;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getGameDetailsId() {
        return gameDetailsId;
    }

    public void setGameDetailsId(Integer gameDetailsId) {
        this.gameDetailsId = gameDetailsId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public GameDetails getGameDetails() {
        return gameDetails;
    }

    public void setGameDetails(GameDetails gameDetails) {
        this.gameDetails = gameDetails;
    }

    public List<ProductAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ProductAttribute> attributes) {
        this.attributes = attributes;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

}
