<%--
    Document   : product-details
    Created on : Jun 23, 2025, 09:35:00 PM
    Author     : HoangSang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="shop.model.Product, shop.model.GameDetails, shop.model.ProductAttribute, shop.model.Customer, java.util.List, java.text.NumberFormat, java.util.Locale, java.math.BigDecimal, java.text.SimpleDateFormat" %>

<%@include file="/WEB-INF/include/home-header.jsp" %>

<%    Product product = (Product) request.getAttribute("product");
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
    Customer currentCustomer = (Customer) session.getAttribute("currentCustomer");
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title><%= (product != null ? product.getName() : "Product Details")%></title>


        <style>
            body{
                margin-top: 5%;
            }
            .details-section {
                padding: 2rem 0;

            }

            .product-card {
                background-color: #1f2937;
                padding: 1.5rem;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.05);
            }

            .purchase-info-sticky {
                position: sticky;
                top: 2rem;
            }

            .main-image-container {
                border-radius: 8px;
                overflow: hidden;
                border: 1px solid #dee2e6;
            }
            .main-image-container img {
                width: 100%;
                height: auto;
                aspect-ratio: 16 / 9;
                object-fit: cover;
            }
            .thumbnail-container {
                display: grid;
                grid-template-columns: repeat(6, 1fr);
                gap: 0.75rem;
                padding-top: 1rem;
                width: 100%;
            }
            .thumbnail-container .thumb {
                width: 100%;
                aspect-ratio: 16/10;
                object-fit: cover;
                border-radius: 4px;
                cursor: pointer;
                border: 2px solid transparent;
                transition: border-color 0.2s;
            }
            .thumbnail-container .thumb.active,
            .thumbnail-container .thumb:hover {
                border-color: #007bff;
            }

            .product-title {
                font-size: 2rem;
                font-weight: bold;
                line-height: 1.2;
                color: white;
            }
            .product-brand {
                font-size: 0.9rem;
                color: white;
                text-transform: uppercase;
                margin-bottom: 0.5rem;
            }
            .product-price {
                font-size: 1.75rem;
                font-weight: bold;
                color: #10b981;
            }
            .old-price {
                font-size: 1.1rem;
                color: #dc3545;
                font-weight: normal;
            }

            .info-section-title {
                font-size: 1.5rem;
                font-weight: 600;
                margin-bottom: 1rem;
                padding-bottom: 0.5rem;
                border-bottom: 1px solid #ccc;
                color: white;
            }

            .product-specifications-card dt {
                font-weight: 600;
            }

            .description-content {
                line-height: 1.7;
                color: white;
            }
            .info-accessory{
                color: white;
            }
            .btn_1{
                background-color: #10b981;
                padding: 2%;
                border-radius: 8px;
            }
            /* === CSS CHO SAO === */
            .product-rating {
                display: flex;
                gap: 3px;
                margin: 0.75rem 0;
                font-size: 1.5rem;
            }
            .star-icon.full {
                color: #ffc107;
            }
            .star-icon.empty {
                color: #4b5563;
            }
            /* === KẾT THÚC CSS CHO SAO === */


        </style>
    </head>

    <body>
        <section class="section details-section">
            <div class="container-fluid px-4">
                <% if (product != null) { %>

                <div class="row g-4">

                    <div class="col-lg-7">
                        <div class="product-card h-100">
                            <%-- Thư viện ảnh --%>
                            <%
                                List<String> imageUrls = product.getImageUrls();
                                if (imageUrls != null && !imageUrls.isEmpty()) {
                            %>
                            <div class="main-image-container mb-3">
                                <img src="<%= request.getContextPath()%>/assets/img/<%= imageUrls.get(0)%>" alt="<%= product.getName()%>" id="mainProductImage">
                            </div>

                            <% if (imageUrls.size() > 1) { %>
                            <div class="thumbnail-scroller">
                                <div class="thumbnail-container" id="thumbnailContainer">
                                    <% for (int i = 0; i < imageUrls.size(); i++) {%>
                                    <img src="<%= request.getContextPath()%>/assets/img/<%= imageUrls.get(i)%>"
                                         class="thumb <%= (i == 0) ? "active" : ""%>"
                                         onclick="changeMainImage(this)">
                                    <% } %>
                                </div>
                            </div>
                            <% } %>
                            <% } else {%>
                            <div class="main-image-container mb-3">
                                <img src="<%= request.getContextPath()%>/assets/img/default-product.png" alt="No Image" id="mainProductImage">
                            </div>
                            <% }%>

                            <%-- Mô tả sản phẩm --%>
                            <hr class="my-4">
                            <h3 class="info-section-title">Description</h3>
                            <div class="description-content">
                                <p><%= (product.getDescription() != null && !product.getDescription().isEmpty()) ? product.getDescription().replace("\n", "<br>") : "No description available."%></p>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-5">
                        <div class="purchase-info-sticky">
                            <%-- Thông tin mua hàng --%>
                            <div class="product-card">
                                <div class="product-brand"><%= (product.getBrandName() != null ? product.getBrandName() : "N/A")%></div>
                                <h1 class="product-title"><%= product.getName()%></h1>

                                <div class="product-price my-3">
                                    <% if (product.getSalePrice() != null && product.getSalePrice().compareTo(BigDecimal.ZERO) > 0) {%>
                                    <%= currencyFormatter.format(product.getSalePrice())%> <span class="old-price"><s><%= currencyFormatter.format(product.getPrice())%></s></span>
                                            <% } else {%>
                                            <%= currencyFormatter.format(product.getPrice())%>
                                            <% }%>
                                </div>
                                <%-- BẮT ĐẦU: Hiển thị đánh giá sao --%>
                                <div class="product-rating">
                                    <%
                                        long roundedStars = Math.round(product.getAverageStars());
                                        for (int i = 1; i <= 5; i++) {
                                            if (i <= roundedStars) {
                                    %>
                                    <span class="star-icon full">&#9733;</span>
                                    <%
                                    } else {
                                    %>
                                    <span class="star-icon empty">&#9733;</span>
                                    <%
                                            }
                                        }
                                    %>
                                </div>
                                <%-- KẾT THÚC: Hiển thị đánh giá sao --%>

                                <%-- FORM MỚI VỚI LOGIC CỦA BẠN --%>
                                <form action="${pageContext.servletContext.contextPath}/cart" method="POST" class="mt-4 product-buttons d-grid gap-2">
                                    <input type="hidden" name="action" value="add">
                                    <input type="hidden" name="page" value="cart">
                                    <input type="hidden" name="username" value="<%= currentCustomer != null ? currentCustomer.getUsername() : ""%>">
                                    <input type="hidden" name="productId" value="<%= product.getProductId()%>">
                                    <input type="hidden" name="quantity" value="1">
                                    <button type="submit" class="btn_1 btn btn-danger btn-lg">Add to Cart</button>
                                    <button type="button" class="btn_1 btn btn-primary btn-lg" onclick="location.href = '<%= request.getContextPath()%>/checkout?view=single&id=<%= product.getProductId()%>&quantity=1'">Buy Now</button>
                                </form>
                            </div>

                            <%-- Thông số kỹ thuật --%>
                            <div class="product-card mt-4">
                                <h3 class="info-section-title">Specifications</h3>
                                <dl class="info-accessory row">
                                    <dt class="col-sm-4">Category</dt><dd class="col-sm-8"><%= product.getCategoryName()%></dd>
                                    <dt class="col-sm-4">In Stock</dt><dd class="col-sm-8"><%= product.getQuantity()%></dd>
                                    <% if (product.getGameDetails() != null) {
                                            GameDetails details = product.getGameDetails();%>
                                    <dt class="col-sm-4">Developer</dt><dd class="col-sm-8"><%= details.getDeveloper()%></dd>
                                    <dt class="col-sm-4">Genre</dt><dd class="col-sm-8"><%= details.getGenre()%></dd>
                                    <dt class="col-sm-4">Release Date</dt><dd class="col-sm-8"><%= new SimpleDateFormat("dd MMM, yyyy").format(details.getReleaseDate())%></dd>
                                    <% } %>
                                    <% if (product.getAttributes() != null && !product.getAttributes().isEmpty()) {%>
                                    <% if (product.getBrandName() != null && !product.getBrandName().isEmpty()) {%>
                                    <dt class="col-sm-4">Brand</dt><dd class="col-sm-8"><%= product.getBrandName()%></dd>
                                    <% } %>
                                    <% for (ProductAttribute attr : product.getAttributes()) {%>
                                    <dt class="col-sm-4"><%= attr.getAttributeName()%></dt><dd class="col-sm-8"><%= attr.getValue()%></dd>
                                        <% }
                                            }%>
                                </dl>
                            </div>
                        </div>
                    </div>
                </div>

                <% } else {%>
                <div class="text-center py-5">
                    <h1>Product Not Found</h1>
                    <p class="lead">The product you are looking for does not exist or has been removed.</p>
                    <a href="<%= request.getContextPath()%>/home" class="btn btn-primary">Back to Homepage</a>
                </div>
                <% }%>
            </div>
        </section>

        <script>
            function changeMainImage(thumbnail) {
                var mainImage = document.getElementById('mainProductImage');
                if (mainImage) {
                    mainImage.src = thumbnail.src;
                }

                var thumbnails = document.querySelectorAll('.thumb');
                thumbnails.forEach(function (thumb) {
                    thumb.classList.remove('active');
                });
                thumbnail.classList.add('active');
            }
        </script>

        <%@include file="/WEB-INF/include/home-footer.jsp" %>
    </body>
</html>