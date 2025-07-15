<%--
    Document   : product-delete
    Created on : Jun 22, 2025, 5:24:42 PM
    Author     : HoangSang
--%>

<%@page import="shop.model.Product"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>

<%
    Product product = (Product) request.getAttribute("delete");
    boolean isSold = request.getAttribute("isSold") != null && (Boolean) request.getAttribute("isSold");
%>

<main class="admin-main">
    <div class="table-header">
        <h2 class="table-title">Delete Product</h2>
    </div>

    <%
        if (product == null) {
    %>
        <div style="border: 1px solid red; background-color: #ffe6e6; color: red; padding: 15px; border-radius: 5px; margin: 1rem;">
            <strong>Error:</strong> Product not found.
        </div>
        <a href="<%= request.getContextPath()%>/manage-products?action=list" class="btn btn-secondary mt-3" style="margin: 1rem;">
            Back to Product List
        </a>
    <%
        } else if (isSold) {
    %>
        <div style="border: 1px solid red; background-color: #ffe6e6; color: red; padding: 15px; border-radius: 5px; margin: 1rem;">
            <strong>Cannot Delete Product:</strong> "<%= product.getName() %>" This product exists in the order history and cannot be removed.
        </div>
        <a href="<%= request.getContextPath()%>/manage-products?action=list" class="admin-manage-back" style="margin: 1rem;">
            <i class="fas fa-arrow-left mr-1"></i> Back
        </a>
    <%
        } else {
    %>
        <div class="mb-4 p-3 border rounded bg-light">
            <p><strong>Are you sure you want to delete?</strong> </p>
            <p><strong>Name:</strong> <%= product.getName()%></p>
            <p><strong>Category:</strong> <%= product.getCategoryName()%></p>
        </div>

        <form method="post" action="<%= request.getContextPath()%>/manage-products">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="id" value="<%= product.getProductId()%>">

            <div class="d-flex justify-content-between align-items-center mt-4">
                <a href="<%= request.getContextPath()%>/manage-products?action=list" class="admin-manage-back">
                    <i class="fas fa-arrow-left mr-1"></i> Back
                </a>

                <div>
                    <button type="submit" class="btn admin-manage-button">
                        <i class="fas fa-trash-can mr-1"></i> Delete
                    </button>
                </div>
            </div>
        </form>
    <%
        }
    %>
</main>

<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>