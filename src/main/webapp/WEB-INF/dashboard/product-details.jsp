<%--
    Document   : product-details
    Created on : Jun 10, 2025, 10:55:00 PM
    Author     : HoangSang
--%>

<%@page import="shop.model.OperatingSystem"%>
<%@page import="shop.model.StorePlatform"%>
<%@page import="shop.model.GameKey"%>
<%@page import="shop.model.ProductAttribute"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Locale"%>
<%@page import="shop.model.Product"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" rel="stylesheet">
<link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/product-style.css">
<style>
    body {
        width: 86%;
        margin-left: 13%;
        background-color: #161922;
        margin-top: 3%;
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

<%
    Product product = (Product) request.getAttribute("product");
    NumberFormat currencyFormatter = (NumberFormat) request.getAttribute("currencyFormatter");
    SimpleDateFormat timestampFormatter = (SimpleDateFormat) request.getAttribute("timestampFormatter");
    SimpleDateFormat dateFormatter = (SimpleDateFormat) request.getAttribute("dateFormatter");
    List<GameKey> gameKeys = (List<GameKey>) request.getAttribute("gameKeys");
    List<StorePlatform> platforms = (List<StorePlatform>) request.getAttribute("platforms");
    List<OperatingSystem> operatingSystems = (List<OperatingSystem>) request.getAttribute("operatingSystems");
%>


<main class="container-fluid p-4 p-lg-5">
    <% if (product != null) {%>
    <div class="page-header">
        <h1 class="h2 fw-bold">
            <i class="fas fa-box-open me-2 text-primary" style="color: var(--accent-blue) !important;"></i>
            Product Details:
            <% if (product.getActiveProduct() == 1) { %>
            Enabled
            <% } else { %>
            Disabled
            <% }%>
        </h1>
        <div class="d-flex gap-2">
            <a href="<%= request.getContextPath()%>/manage-products?action=list" class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left me-2"></i> Back</a>
            <a href="<%= request.getContextPath()%>/manage-products?action=update&id=<%= product.getProductId()%>" class="btn btn-warning">
                <i class="fas fa-edit me-1"></i> Edit</a>
            <a href="<%= request.getContextPath()%>/manage-products?action=delete&id=<%= product.getProductId()%>" class="btn btn-danger">
                <i class="fas fa-trash me-1"></i> Delete</a>
        </div>
    </div>

    <div class="row g-4">
        <div class="col-lg-8">
            <div class="card">
                <div class="card-header">
                    <i class="fas fa-info-circle me-2"></i>General Information             
                </div>
                <div class="card-body p-4">
                    <h2 class="card-title h1 fw-bolder"><%= product.getName()%></h2>
                    <p class="text-secondary">ID: <%= product.getProductId()%></p>
                    <hr class="my-4" style="border-color: var(--border-color);">
                    <dl class="row info-list g-3">
                        <dt class="col-sm-3">
                            <i class="fas fa-dollar-sign fa-fw me-2 text-secondary"></i>Price
                        </dt>
                        <dd class="col-sm-9 fs-5"><%= currencyFormatter.format(product.getPrice())%></dd>

                        <dt class="col-sm-3">
                            <i class="fas fa-tags fa-fw me-2 text-secondary"></i>Sale Price
                        </dt>
                        <dd class="col-sm-9 fs-5">
                            <% if (product.getSalePrice() != null && product.getSalePrice().compareTo(java.math.BigDecimal.ZERO) > 0) {%>
                            <strong class="text-danger"><%= currencyFormatter.format(product.getSalePrice())%></strong>
                            <% } else { %><span class="text-secondary">N/A</span><% }%>
                        </dd>

                        <dt class="col-sm-3"><i class="fas fa-boxes-stacked fa-fw me-2 text-secondary"></i>Quantity</dt>
                        <dd class="col-sm-9 fs-5">
                            <%= product.getQuantity()%>
                            <% if (product.getQuantity() > 10) { %>
                            <span class="badge rounded-pill bg-success ms-2">In Stock</span>
                            <% } else if (product.getQuantity() > 0) { %>
                            <span class="badge rounded-pill bg-warning text-dark ms-2">Low Stock</span>
                            <% } else { %>
                            <span class="badge rounded-pill bg-danger ms-2">Out of Stock</span>
                            <% }%>
                        </dd>

                        <dt class="col-sm-3"><i class="fas fa-sitemap fa-fw me-2 text-secondary"></i>Category</dt>
                        <dd class="col-sm-9 fs-5"><%= product.getCategoryName() != null ? product.getCategoryName() : "N/A"%></dd>

                        <dt class="col-sm-3"><i class="fas fa-copyright fa-fw me-2 text-secondary"></i>Brand</dt>
                        <dd class="col-sm-9 fs-5"><%= product.getBrandName() != null ? product.getBrandName() : "N/A"%></dd>

                        <dt class="col-sm-3"><i class="fas fa-align-left fa-fw me-2 text-secondary"></i>Description</dt>
                        <dd class="col-sm-9" style="white-space: pre-wrap;"><%= product.getDescription() != null ? product.getDescription() : "N/A"%></dd>
                    </dl>
                </div>
            </div>

            <div class="card mt-4">
                <div class="card-header"><i class="fas fa-cogs me-2"></i>Specifications</div>
                <div class="card-body p-0">
                    <% if ("Game".equalsIgnoreCase(product.getCategoryName()) && product.getGameDetails() != null) {%>
                    <table class="table table-hover mb-0">
                        <tbody>
                            <tr>
                                <th style="width: 30%"><i class="fas fa-user-edit fa-fw me-2 text-secondary"></i>Developer</th><td><%= product.getGameDetails().getDeveloper()%></td></tr>
                            <tr><th><i class="fas fa-gamepad fa-fw me-2 text-secondary"></i>Genre</th><td><%= product.getGameDetails().getGenre()%></td></tr>
                            <tr><th><i class="fas fa-calendar-alt fa-fw me-2 text-secondary"></i>Release Date</th><td><%= dateFormatter.format(product.getGameDetails().getReleaseDate())%></td></tr>

                            <tr>
                                <th><i class="fas fa-desktop fa-fw me-2 text-secondary"></i>Platforms</th>
                                <td>
                                    <% if (platforms != null && !platforms.isEmpty()) { %>
                                    <% for (int i = 0; i < platforms.size(); i++) {%>
                                    <%= platforms.get(i).getStoreOSName()%><%= (i < platforms.size() - 1) ? ", " : ""%>
                                    <% } %>
                                    <% } else { %>
                                    N/A
                                    <% } %>
                                </td>
                            </tr>

                            <tr>
                                <th><i class="fab fa-windows fa-fw me-2 text-secondary"></i>Operating Systems</th>
                                <td>
                                    <% if (operatingSystems != null && !operatingSystems.isEmpty()) { %>
                                    <% for (int i = 0; i < operatingSystems.size(); i++) {%>
                                    <%= operatingSystems.get(i).getOsName()%><%= (i < operatingSystems.size() - 1) ? ", " : ""%>
                                    <% } %>
                                    <% } else { %>
                                    N/A
                                    <% } %>
                                </td>
                            </tr>

                            <tr>
                                <th><i class="fas fa-key fa-fw me-2 text-secondary"></i>Available Keys</th>
                                <td>
                                    <div class="key-list-scroller">
                                        <% if (gameKeys != null && !gameKeys.isEmpty()) { %>
                                        <ul class="list-unstyled">
                                            <% for (GameKey key : gameKeys) {%>
                                            <li class="key-item"><%= key.getKeyCode()%></li>
                                                <% } %>
                                        </ul>
                                        <% } else { %>
                                        <span class="text-secondary">No keys available</span>
                                        <% } %>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <% } else if (product.getAttributes() != null && !product.getAttributes().isEmpty()) { %>
                    <table class="table table-hover mb-0">
                        <tbody>
                            <% for (ProductAttribute attr : product.getAttributes()) {%>
                            <tr>
                                <th style="width: 30%; text-transform: capitalize;"><i class="fas fa-sliders-h fa-fw me-2 text-secondary"></i><%= attr.getAttributeName().replace('_', ' ')%></th>
                                <td><%= attr.getValue()%></td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                    <% } else { %>
                    <p class="text-secondary p-3">No specific attributes for this product.</p>
                    <% } %>
                </div>
            </div>
        </div>

        <div class="col-lg-4">
            <div class="card mb-4">
                <div class="card-header"><i class="fas fa-image me-2"></i>Product Images</div>
                <div class="card-body">
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
                        <button class="scroll-arrow left" onclick="scrollGallery(-1)"><i class="fas fa-chevron-left"></i></button>
                        <button class="scroll-arrow right" onclick="scrollGallery(1)"><i class="fas fa-chevron-right"></i></button>
                    </div>
                    <% } %>
                    <% } else { %>
                    <p class="text-secondary text-center p-3">No Image Available</p>
                    <% }%>
                </div>
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

            <div class="card">
                <div class="card-header"><i class="fas fa-database me-2"></i>Metadata</div>
                <div class="card-body">
                    <dl class="info-list">
                        <dt><i class="fas fa-plus-circle fa-fw me-2 text-secondary"></i>Date Created</dt>
                        <dd><%= product.getCreatedAt() != null ? timestampFormatter.format(product.getCreatedAt()) : "N/A"%></dd>
                        <hr class="my-3" style="border-color: var(--border-color);">
                        <dt><i class="fas fa-sync-alt fa-fw me-2 text-secondary"></i>Last Updated</dt>
                        <dd><%= product.getUpdatedAt() != null ? timestampFormatter.format(product.getUpdatedAt()) : "N/A"%></dd>
                    </dl>
                </div>
            </div>
        </div>
    </div>

    <% } else { %>
    <div class="alert alert-danger text-center">
        <h3>Product Not Found</h3>
        <p>The product you are looking for does not exist or may have been deleted.</p>
    </div>
    <% }%>
</main>

<script>
    function changeMainImage(thumbElement) {
                document.getElementById('mainProductImage').src = thumbElement.src;
                document.querySelectorAll('.thumbnail-container .thumb').forEach(thumb => {
                        thumb.classList.remove('active');
                });
                thumbElement.classList.add('active');
        }

    function scrollGallery(direction) {
                const container = document.getElementById('thumbnailContainer');
                const scrollAmount = (120 + 12) * 3 * direction;
                container.scrollBy({left: scrollAmount, behavior: 'smooth'});
        }

</script>
<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>