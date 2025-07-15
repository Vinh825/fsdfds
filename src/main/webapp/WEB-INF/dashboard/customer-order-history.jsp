<%-- 
    Document   : customer-order-history
    Created on : Jun 16, 2025, 10:49:47 AM
    Author     : CE190449 - Le Anh Khoa
--%>

<%@page import="shop.model.Order"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>
<c:set var="thisCustomer" value="${requestScope.thisCustomer}"/>
<main class="admin-main">
    <div class="table-header">
        <h2 class="table-title">Customer Order History</h2>
    </div>

    <div class="admin-manage-wrapper container py-4">
        <div class="mb-4">
            <a href="${pageContext.servletContext.contextPath}/manage-customers" class="admin-manage-back mb-5">
                <i class="fas fa-arrow-left mr-1"></i> Back
            </a>
        </div>
        


                <!-- General Information -->
                <table class="table table-dark table-bordered table-hover align-middle mb-0">
                    <thead class="table-light text-dark">
                        <tr>
                            <th>ID</th>
                            <th>CustomerID</th>
                            <th>Order Date</th>
                            <th>Total Amount</th>
                            <th>Status</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Order> orderlist = (List) request.getAttribute("orderlist");
                            for (Order order : orderlist) {


                        %>
                        <tr>
                            <td><%= order.getOrderId()%></td>
                            <td><%= order.getCustomerName()%></td>
                            <td><%= order.getOrderDate()%></td>
                            <td><%= order.getTotalAmount()%></td>
                            <td>
                                <form action="manage-orders" method="post">
                                    <input type="hidden" name="action" value="update" />
                                    <input type="hidden" name="orderId" value="<%= order.getOrderId()%>" />
                                    <select name="status" class="admin-filter-select" onchange="this.form.submit()">
                                        <option value="Pending" <%= order.getStatus().equals("Pending") ? "selected" : ""%>>Pending</option>
                                        <option value="Confirmed" <%= order.getStatus().equals("Confirmed") ? "selected" : ""%>>Confirmed</option>
                                        <option value="Shipped" <%= order.getStatus().equals("Shipped") ? "selected" : ""%>>Shipped</option>
                                        <option value="Delivered" <%= order.getStatus().equals("Delivered") ? "selected" : ""%>>Delivered</option>
                                    </select>
                                </form>



                            </td>
                            <td>
                                <div class="table-actions-center">
                                    <button class="btn-action btn-details"
                                            onclick="location.href = '<%= request.getContextPath()%>/manage-orders?view=details&customer_id=<%= order.getCustomerId()%>&order_id=<%= order.getOrderId()%>'">
                                        Details
                                    </button>

                                </div>
                            </td>
                        </tr>
                        <%}%>
                    </tbody>
                </table>
                <!-- Pagination -->
                <!-- FORM PHÂN TRANG -->
                <form id="paginationForm" method="GET" action="${pageContext.request.contextPath}/manage-customers">
                    <input type="hidden" name="view" value="history" />
                    <input type="hidden" name="id" value="${id}" />
                    <input type="hidden" name="page" id="pageInput" value="${currentPage}" />
                </form>
                <c:if test="${totalPages > 1}">
                    <nav class="admin-pagination">
                        <ul class="pagination">
                            <!-- Nút Previous -->
                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                <a href="#" class="page-link" onclick="submitPage(${currentPage - 1})">«</a>
                            </li>

                            <!-- Các nút số trang -->
                            <c:forEach var="i" begin="1" end="${totalPages}">
                                <li class="page-item ${i == currentPage ? 'active' : ''}">
                                    <a href="#" class="page-link" onclick="submitPage(${i})">${i}</a>
                                </li>
                            </c:forEach>

                            <!-- Nút Next -->
                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                <a href="#" class="page-link" onclick="submitPage(${currentPage + 1})">»</a>
                            </li>
                        </ul>
                    </nav>
                </c:if>

                <!-- SCRIPT PHÂN TRANG -->
                <script>
                    function submitPage(pageNumber) {
                        document.getElementById('pageInput').value = pageNumber;
                        document.getElementById('paginationForm').submit();
                    }
                </script>

                <!-- NÚT BACK -->
                <div class="d-flex justify-content-between align-items-center mt-4">
                    <a href="${pageContext.servletContext.contextPath}/manage-customers" class="admin-manage-back">
                        <i class="fas fa-arrow-left mr-1"></i> Back
                    </a>
                </div>

    </div>
</main>