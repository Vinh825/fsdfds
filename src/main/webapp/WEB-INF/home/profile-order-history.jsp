<%@page import="java.util.List"%>
<%@page import="shop.dao.ProductDAO"%>
<%@page import="shop.model.Customer"%>
<%@page import="shop.model.Order"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/include/home-header.jsp" %>
<style>
    .avatar-display-container {
        margin-bottom: 15px;
        text-align: center;
    }

    .avatar-display {
        width: 100px;
        height: 100px;
        border-radius: 50%;
        object-fit: cover;
        border: 3px solid #007bff;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
        transition: transform 0.3s ease, box-shadow 0.3s ease;
    }

    .avatar-display:hover {
        transform: scale(1.1);
        box-shadow: 0 6px 15px rgba(0, 0, 0, 0.3);
    }

    .change-avatar-btn {
        font-size: 16px;
        padding: 10px 20px;
        background-color: #007bff;
        border-color: #007bff;
        color: white;
        border-radius: 5px;
        transition: background-color 0.3s ease, transform 0.3s ease;
        display: block;
        margin-top: 10px;
        width: 100%;
    }

    .change-avatar-btn:hover {
        background-color: #0056b3;
        border-color: #004085;
        transform: scale(1.05);
    }

    .avatar-input {
        display: none;
    }

    .avatar-modal {
        display: none;
        padding: 10px;
        background-color: #004085;
        border-radius: 8px;
        transition: opacity 0.3s ease, transform 0.3s ease;
        opacity: 0;
        transform: translateY(-20px);
    }

    .avatar-modal.show {
        display: block;
        opacity: 1;
        transform: translateY(0);
    }

    .avatar-modal .modal-dialog {
        max-width: 700px;
        margin: 0 auto;
    }

    .avatar-selection-container {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
        gap: 15px;
        justify-items: center;
    }

    .avatar-item {
        text-align: center;
        border-radius: 8px;
        padding: 10px;
        transition: transform 0.3s ease, box-shadow 0.3s ease;
    }

    .avatar-item:hover {
        transform: scale(1.1);
    }

    .avatar-option {
        width: 80px;
        height: 80px;
        object-fit: cover;
        border-radius: 50%;
        transition: transform 0.3s ease;
    }

    .avatar-option:hover {
        transform: scale(1.2);
    }

    #openAvatarModalBtn {
        font-size: 16px;
        padding: 8px 12px;
        background-color: #007bff;
        border-color: #007bff;
        color: white;
        border-radius: 5px;
        transition: background-color 0.3s ease;
    }

    #openAvatarModalBtn:hover {
        background-color: #0056b3;
        border-color: #004085;
    }

    .modal-body {
        padding: 20px;
        max-height: 60vh;
        overflow-y: auto;
    }

    .modal-header {
        color: #00c8ff;
        border-bottom: 1px solid #00c8ff;
    }

    .modal-title {
        font-size: 20px;
        font-weight: bold;
    }

    .modal-footer {
        padding: 10px;
    }

    .closeBtnModal {
        color: white;
    }

    .closeBtnModal:hover {
        color: #007bff;
    }

    .modal-backdrop {
        background-color: rgba(0, 0, 0, 0.5);
    }

    /* Add focus effect to the select dropdown */
    .form__input:focus {
        border-color: #007bff;
        background-color: #333;
        box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
    }

    /* Style for the dropdown options */
    .form__input option {
        background-color: #1a1a1a;
        color: #fff;
    }

    /* Styling for the selected option */
    .form__input option:checked {
        background-color: #007bff;
        color: #fff;
    }

    /* Style for the date input icon */
    .form__input[type="date"]::-webkit-calendar-picker-indicator {
        background-color: #007bff;
        border-radius: 5px;
        padding: 5px;
        cursor: pointer;
        transition: background-color 0.3s ease;
    }

    /* Hover effect for the date picker icon */
    .form__input[type="date"]::-webkit-calendar-picker-indicator:hover {
        background-color: #0056b3;
    }

    /* For Firefox */
    .form__input[type="date"]::-moz-calendar-picker-indicator {
        background-color: #007bff;
        border-radius: 5px;
        padding: 5px;
        cursor: pointer;
    }

    .form__input[type="date"]::-moz-calendar-picker-indicator:hover {
        background-color: #0056b3;
    }
</style>
<c:choose>
    <c:when test="${not empty requestScope.updateFailed and not empty requestScope.thisCustomer}">
        <c:set var="user" value="${requestScope.thisCustomer}" />
    </c:when>
    <c:otherwise>
        <c:set var="user" value="${sessionScope.currentCustomer}" />
    </c:otherwise>
</c:choose>

<!-- page title -->
<section class="section section--first section--last section--head" data-bg="${pageContext.servletContext.contextPath}/assets/img/bg3.png">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <div class="section__wrap">
                    <!-- section title -->
                    <h2 class="section__title">Order History</h2>
                    <!-- end section title -->

                    <!-- breadcrumb -->
                    <ul class="breadcrumb">
                        <li class="breadcrumb__item"><a href="${pageContext.servletContext.contextPath}/home">Home</a></li>
                        <li class="breadcrumb__item breadcrumb__item--active">Profile</li>
                    </ul>
                    <!-- end breadcrumb -->
                </div>
            </div>
        </div>
    </div>


</section>
<section class="section section--last">

    <div class="row">
        <div class="col-12">
            <div class="table-responsive table-responsive--border">
                <table class="profile__table">
                    <thead>
                        <tr>
                            <th>№</th>
                            <th>Total Price</th>
                            <th>Date</th>
                            <th>Status</th>
                            <th>Review</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <%  List<Order> order = (List<Order>) request.getAttribute("order");

                        for (Order od : order) {


                    %>
                    <tbody>

                        <tr>
                            <td><a href="#modal-info" class="open-modal"><%= od.getOrderId()%></a></td>
                            <td><span class="profile__price"><%= od.getTotalAmount()%></span></td>
                            <td><%= od.getOrderDate()%></td>
                            <td><span class="profile__status"><%= od.getStatus()%></span></td>
                                <% if (od.getStatus().equalsIgnoreCase("Shipped") || od.getStatus().equalsIgnoreCase("Delivered")) {%>
                            <td>
                                <%
                                    Customer currentCustomer = (Customer) session.getAttribute("currentCustomer");
                               

                                    if (od.isIsReviewed()) {
                                %>
                                <p style="color: lightgreen;">This product has been previously reviewed.</p>
                                <%
                                } else {
                                %>
                                <form action="profile-order-history" method="post">
                                    <input type="hidden" name="action" value="review">
                                    <input type="hidden" name="orderId" value="<%= od.getOrderId()%>">
                                    <style>
                                        .star-rating input[type="radio"] {
                                            display: none;
                                        }

                                        .star-rating label {
                                            font-size: 20px;
                                            color: #ccc;
                                            cursor: pointer;
                                        }

                                        .star-rating input[type="radio"]:checked ~ label {
                                            color: gold;
                                        }

                                        .star-rating label:hover,
                                        .star-rating label:hover ~ label {
                                            color: gold;
                                        }
                                    </style>

                                    <div class="star-rating" style="direction: rtl;">
                                        <% for (int i = 5; i >= 1; i--) {%>
                                        <input type="radio" id="star<%= od.getOrderId()%>_<%= i%>" name="rating" value="<%= i%>">
                                        <label for="star<%= od.getOrderId()%>_<%= i%>">&#9733;</label>
                                        <% } %>
                                    </div>

                                    <button style="color: white" type="submit">Submit</button>
                                </form>
                                <%
                                    }
                                %>

                            </td>
                            <% }else {%>
                            <<td>Order is still in transit</td><%}%>
                            <td><form action="<%= request.getContextPath()%>/profile-order-history" method="post" class="table-actions-center" style="display:inline;">
                                    <input type="hidden" name="action" value="details">
                                    <input type="hidden" name="order_id" value="<%= od.getOrderId()%>">
                                    <button type="submit" class="btn-action btn-details">Details</button>
                                </form>
                            </td>

                        </tr>

                    </tbody>
                    <%}%>
                </table>
            </div>
        </div>

        <!-- paginator -->
        <!-- FORM PHÂN TRANG -->
        <form id="paginationForm" method="GET" action="<%= request.getContextPath()%>/profile-order-history">
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
        <!-- end paginator -->
    </div>
</div>
</section>

<%@include file="/WEB-INF/include/home-footer.jsp" %>