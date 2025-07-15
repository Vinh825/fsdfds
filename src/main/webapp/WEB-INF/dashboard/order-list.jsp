<%-- 
    Document   : dashboar-order-list
    Created on : Jun 11, 2025, 1:27:27 PM
    Author     : ADMIN
--%>

<%@page import="java.util.List"%>
<%@page import="shop.dao.OrderDAO"%>
<%@page import="shop.model.Customer"%>
<%@page import="shop.model.Order"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>
<c:set var="isSearch" value="${not empty param.customer_name}" />


<main class="admin-main">

    <div class="table-header">
        <h2 class="table-title">Manage Orders</h2>
    </div>

    <section class="admin-header">
        <div class="admin-header-top">
            <form action="<%= request.getContextPath()%>/manage-orders" method="POST" style="display: flex; margin-left: auto;" class="search-filter-wrapper">
                <input type="hidden" name="action" value="search" />
                <input type="text" name="customer_name" class="search-input" placeholder="Enter customer name...">
                <button type="submit" class="search-btn">Search</button>
            </form>
        </div>

    </section>



    <section class="admin-table-wrapper">
        <div class="table-responsive shadow-sm rounded overflow-hidden">
            <table class="table table-dark table-bordered table-hover align-middle mb-0">
                <thead class="table-light text-dark">
                    <tr>
                        <th>ID</th>
                        <th>Customer Name</th>
                        <th>Order Date</th>
                        <th>Total Amount</th>
                        <th>Status</th>
                        <th style="text-align: center;">Action</th>
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
                        <td><%
                            String status = order.getStatus();
                            int statusLevel = 0;
                            if (status.equals("Pending"))
                                statusLevel = 1;
                            else if (status.equals("Confirmed"))
                                statusLevel = 2;
                            else if (status.equals("Shipped"))
                                statusLevel = 3;
                            else if (status.equals("Delivered"))
                                statusLevel = 4;
                            else if (status.equals("Canceled"))
                                statusLevel = 5;
                            %>

                            <form action="manage-orders" method="post">
                                <input type="hidden" name="action" value="update" />
                                <input type="hidden" name="orderId" value="<%= order.getOrderId()%>" />
                                <select name="status" class="admin-filter-select" onchange="this.form.submit()">
                                    <option value="Pending"
                                            <%= status.equals("Pending") ? "selected" : ""%>
                                            <%= statusLevel > 1 ? "disabled" : ""%>>Pending</option>

                                    <option value="Confirmed"
                                            <%= status.equals("Confirmed") ? "selected" : ""%>
                                            <%= statusLevel > 2 ? "disabled" : ""%>>Confirmed</option>

                                    <option value="Shipped"
                                            <%= status.equals("Shipped") ? "selected" : ""%>
                                            <%= statusLevel > 3 ? "disabled" : ""%>>Shipped</option>

                                    <option value="Delivered"
                                            <%= status.equals("Delivered") ? "selected" : ""%>
                                            <%= statusLevel > 4 ? "disabled" : ""%>>Delivered</option>

                                    <option value="Canceled"
                                            <%= status.equals("Canceled") ? "selected" : ""%>
                                            <%= statusLevel > 5 ? "disabled" : ""%>>Canceled</option>
                                </select>
                            </form>
                        </td>

                        <td>
                            <div class="table-actions-center">
                                <button class="btn-action btn-details"
                                        onclick="location.href = '<%= request.getContextPath()%>/manage-orders?view=details&customer_id=<%= order.getCustomerId()%>&order_id=<%= order.getOrderId()%>'">
                                    Details
                                </button>
                                <button class="btn-action btn-history" onclick="location.href = '<%= request.getContextPath()%>/manage-customers?view=details&id=<%= order.getCustomerId()%>'">
                                    Customer Details</button>
                            </div>
                        </td>
                    </tr>
                    <%}%>
                </tbody>
            </table>
        </div>
    </section>

    <!-- FORM PHÂN TRANG -->
    <form id="paginationForm" method="POST" action="<%= request.getContextPath()%>/manage-orders">
        <input type="hidden" name="action" value="search" />
        <input type="hidden" name="customer_name" value="${param.customer_name}" />
        <input type="hidden" name="page" id="pageInput" value="${currentPage}" />
    </form>

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

    <script>
        function submitPage(pageNumber) {
            document.getElementById('pageInput').value = pageNumber;
            document.getElementById('paginationForm').submit();
        }
    </script>



</main>

<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>