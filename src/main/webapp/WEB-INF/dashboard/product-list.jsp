<%--
    Document   : product-list
    Created on : Jun 10, 2025, 10:55:00 PM
    Author     : HoangSang
--%>

<%@page import="java.util.List"%>
<%@page import="shop.model.Product"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>

<%
    String contextPath = request.getContextPath();
    int currentPage = (Integer) request.getAttribute("currentPage");
    int totalPages = (Integer) request.getAttribute("totalPages");
    int rowNumber = (Integer) request.getAttribute("startRowNumber");
    List<Product> productList = (List<Product>) request.getAttribute("productList");

    String pageUrl = (String) request.getAttribute("pageUrl");
    String previousPageUrl = (String) request.getAttribute("previousPageUrl");
    String nextPageUrl = (String) request.getAttribute("nextPageUrl");
%>

<style>
    .table-product-img {
        width: 80px;
        height: 60px;
        object-fit: cover;
        border-radius: 4px;
    }
    .clear-search-btn{
        background-color: #ef4444;
        color: #fff;
        padding: 8px;
        border-radius: 13px;
    }
</style>

<main class="admin-main">
    <div class="table-header">
        <h2 class="table-title">Manage Products</h2>
    </div>

    <section class="admin-header">
        <div class="admin-header-top">
            <a class="btn-admin-add" href="manage-products?action=add">+ Add New Product</a>
            <div class="search-filter-wrapper">
                <form action="manage-products" method="get" class="search-form">
                    <input type="hidden" name="action" value="search">
                    <input type="text" name="query" class="search-input" placeholder="Enter product name..." value="${param.query != null ? param.query : ''}">
                    <button type="submit" class="search-btn">Search</button>
                </form>
                <a href="manage-products?action=list" class="clear-search-btn">
                    <i class="fas fa-times "></i> Clear
                </a>
            </div>
        </div>              
    </section>

    <section class="admin-table-wrapper">
        <div class="table-responsive shadow-sm rounded overflow-hidden">
            <table class="table table-dark table-bordered table-hover align-middle mb-0">
                <thead class="table-light text-dark">
                    <tr>
                        <th>No.</th>
                        <th>Product Image</th>
                        <th>Product Name</th>
                        <th>Price</th>
                        <th>Sale Price</th>
                        <th>Category</th>
                        <th style="text-align: center;">Status</th>
                        <th style="text-align: center;">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (productList != null && !productList.isEmpty()) {
                            for (Product p : productList) {
                    %>
                    <tr>
                        <td><%= rowNumber++%></td> 
                        <td>
                            <% if (p.getImageUrls() != null && !p.getImageUrls().isEmpty()) {%>
                            <img src="<%= contextPath%>/assets/img/<%= p.getImageUrls().get(0)%>" alt="<%= p.getName()%>" class="table-product-img">
                            <% } else { %>
                            <span>No Image</span>
                            <% }%>
                        </td>
                        <td><%= p.getName()%></td>
                        <td>$<%= p.getPrice()%></td>
                        <td>
                            <% if (p.getSalePrice() != null) {%>
                            $<%= p.getSalePrice()%>
                            <% } else { %>
                            N/A
                            <% }%>
                        </td>
                        <td><%= p.getCategoryName() != null ? p.getCategoryName() : "N/A"%></td>
                        <td style="text-align: center;">
                            <%-- Form sẽ tự động gửi khi người dùng thay đổi lựa chọn trong select --%>
                            <form action="manage-products" method="POST" style="margin: 0;">
                                <input type="hidden" name="action" value="updateVisibility">
                                <input type="hidden" name="id" value="<%= p.getProductId()%>">
                                <input type="hidden" name="page" value="<%= currentPage%>">

                                <%-- Thẻ select với sự kiện onchange --%>
                                <select name="newStatus" class="admin-filter-select" onchange="this.form.submit()">
                                    <option value="1" <%= (p.getActiveProduct() == 1) ? "selected" : ""%>>
                                        Enabled
                                    </option>
                                    <option value="0" <%= (p.getActiveProduct() == 0) ? "selected" : ""%>>
                                        Disabled
                                    </option>
                                </select>
                            </form>
                        </td>
                        <td>
                            <div class="table-actions-center">
                                <a class="btn-action btn-details" href="manage-products?action=details&id=<%= p.getProductId()%>">Details</a>
                                <a class="btn-action btn-edit" href="manage-products?action=update&id=<%= p.getProductId()%>">Edit</a>
                                <a class="btn-action btn-delete" href="manage-products?action=delete&id=<%= p.getProductId()%>">Delete</a>
                            </div>
                        </td>
                    </tr>
                    <%      }
                    } else {
                    %>
                    <tr>
                        <td colspan="7" class="text-center">No products found.</td>
                    </tr>
                    <%  } %>
                </tbody>
            </table>
        </div>


    </section>
    <% if (totalPages > 1) {%>
    <nav class="admin-pagination">
        <ul class="pagination">
            <li class="page-item <%= (currentPage <= 1) ? "disabled" : ""%>">
                <a class="page-link" href="<%= previousPageUrl%>" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>

            <% for (int i = 1; i <= totalPages; i++) {%>
            <li class="page-item <%= (i == currentPage) ? "active" : ""%>">
                <a class="page-link" href="<%= pageUrl + i%>"><%= i%></a>
            </li>
            <% }%>

            <li class="page-item <%= (currentPage >= totalPages) ? "disabled" : ""%>">
                <a class="page-link" href="<%= nextPageUrl%>" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        </ul>
    </nav>
    <% }%>
</main>

<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>