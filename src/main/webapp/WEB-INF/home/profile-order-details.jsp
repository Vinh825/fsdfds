<%@page import="shop.model.OrderDetails"%>
<%@page import="shop.model.Order"%>
<%@page import="java.util.ArrayList"%>
<%@include file="../include/home-header.jsp" %>
<main class="orderdetailforcus">
    <div class="table-header">
        <h2 class="table-title">Order Detail</h2>
    </div>
    <div class="table-header">
        <h2 class="table-title">Order Detail</h2>
    </div>

    <%        Order order = (Order) request.getAttribute("order");
    %>

    <section class="admin-table-wrapper">
        <div class="table-responsive shadow-sm rounded overflow-hidden">
            <table class="table table-dark table-bordered table-hover align-middle mb-0">
                <thead class="table-light text-dark">
                    <tr>
                        <th>ID</th>
                        <th>Customer Name</th>
                        <th>Email</th>
                        <th>Order Date</th>
                        <th>Total Amount</th>
                        <th>Status</th>
                        <th>Address</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><%= request.getParameter("order_id")%></td>
                        <td><%= order.getCustomerName()%></td>
                        <td><%= order.getCustomerEmail()%></td>
                        <td><%= order.getOrderDate()%></td>
                        <td><%= order.getTotalAmount()%></td>
                        <td><span class="badge-status"><%= order.getStatus()%></span></td>
                        <td><span class="badge-staff"><%= order.getShippingAddress()%></span></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </section>

    <div class="table-header">
        <h2 class="table-title">Product in order</h2>
    </div>

    <section class="admin-table-wrapper">
        <div class="table-responsive shadow-sm rounded overflow-hidden">
            <table class="table table-dark table-bordered table-hover align-middle mb-0">
                <thead class="table-light text-dark">
                    <tr>
                        <th>Product Image</th>
                        <th>Product Name</th>
                        <th>Category</th>
                        <th>Quantity</th>
                        <th>Price</th>
                        <th>XXX</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        ArrayList<OrderDetails> orderDetails = (ArrayList) request.getAttribute("orderdetails");
                        for (OrderDetails orderdetails : orderDetails) {

                    %>
                    <tr>
                        <td><div class="cart__img">
                                <img src="<%= request.getContextPath()%>/assets/img/<%= orderdetails.getImageURL()%>" alt="">
                            </div></td>
                        <td><%= orderdetails.getProductName()%></td>
                        <td><%= orderdetails.getCategoryName()%></td>
                        <td><%= orderdetails.getQuantity()%></td>
                        <td><%= orderdetails.getPrice()%></td>
                        <% if (orderdetails.getGameKey() != null && !orderdetails.getGameKey().isEmpty() && orderdetails.getGameKey() != "") {
                        %>                            
                        <td><%= orderdetails.getGameKey() %></td>

                        <%}
                        %>
                        
                    </tr>
                    <%  }%>
                </tbody>
            </table>
        </div>
    </section>
    <a href="javascript:history.back()" class="admin-manage-back">
        <i class="fas fa-arrow-left mr-1"></i> Back
    </a>
</main>
<%@include file="../include/home-footer.jsp" %>