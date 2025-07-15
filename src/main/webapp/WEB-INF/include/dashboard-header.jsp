<%-- 
    Document   : home-header
    Created on : Jun 10, 2025, 10:20:25 AM
    Author     : Ainzle
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="user" value="${sessionScope.currentEmployee}"/>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

        <!-- CSS -->
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/bootstrap-reboot.min.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/bootstrap-grid.min.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/owl.carousel.min.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/magnific-popup.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/nouislider.min.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/jquery.mCustomScrollbar.min.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/paymentfont.min.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/main.css">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

        <!-- Favicons -->
        <link rel="icon" type="image/png" href="${pageContext.servletContext.contextPath}/assets/icon/logo.png" sizes="32x32">
        <link rel="apple-touch-icon" href="${pageContext.servletContext.contextPath}/assets/icon/logo.png">

        <meta name="description" content="Cosmora Celestica - Selling games and gaming accessories website">
        <meta name="keywords" content="">
        <title>Cosmora Celestica – Games and Accessories</title>

    </head>

    <body>
        <!-- header -->
        <header class="header">
            <div class="header__wrap">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-12">
                            <div class="header__content">
                                <a href="${pageContext.servletContext.contextPath}/home" class="header__logo">
                                    <img src="${pageContext.servletContext.contextPath}/assets/img/logo.png" alt="">
                                </a>

                                <c:choose>
                                    <c:when test="${not empty user}">
                                        <div class="admin-dropdown" onclick="toggleDropdown()">
                                            <div class="admin-profile">
                                                <img src="${user.avatarUrl}" alt="Avatar" class="admin-avatar">
                                                <div>
                                                    <p class="admin-name">${user.fullName}</p>
                                                    <span class="admin-role">
                                                        <c:choose>
                                                            <c:when test="${user.role == 'staff'}">Staff</c:when>
                                                            <c:otherwise>Admin</c:otherwise>
                                                        </c:choose>
                                                    </span>
                                                </div>
                                            </div>

                                            <div class="admin-menu" id="adminDropdown">
                                                <div class="admin-user">
                                                    <span class="admin-role">
                                                        <c:choose>
                                                            <c:when test="${user.role == 'staff'}">Staff</c:when>
                                                            <c:otherwise>Admin</c:otherwise>
                                                        </c:choose>
                                                    </span>
                                                    <p class="admin-name">${user.fullName}</p>
                                                </div>
                                                <ul class="admin-links">
                                                    <c:if test="${user.role == 'admin'}">
                                                        <li><a href="${pageContext.servletContext.contextPath}/profile-dashboard">My Profile</a></li>
                                                        </c:if>
                                                    <li><a href="${pageContext.servletContext.contextPath}/logout">Logout</a></li>
                                                </ul>
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.servletContext.contextPath}/login-dashboard" class="header__login">
                                            <svg xmlns='http://www.w3.org/2000/svg' width='512' height='512'
                                                 viewBox='0 0 512 512'>
                                            <path
                                                d='M192,176V136a40,40,0,0,1,40-40H392a40,40,0,0,1,40,40V376a40,40,0,0,1-40,40H240c-22.09,0-48-17.91-48-40V336'
                                                style='fill:none;stroke-linecap:round;stroke-linejoin:round;stroke-width:32px' />
                                            <polyline points='288 336 368 256 288 176'
                                                      style='fill:none;stroke-linecap:round;stroke-linejoin:round;stroke-width:32px' />
                                            <line x1='80' y1='256' x2='352' y2='256'
                                                  style='fill:none;stroke-linecap:round;stroke-linejoin:round;stroke-width:32px' />
                                            </svg>
                                            <span>Login</span>
                                        </a>
                                    </c:otherwise>
                                </c:choose>


                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </header>
        <button class="admin-sidebar-toggle" onclick="$('.admin-sidebar').toggleClass('open')">☰
            Menu</button>

        <aside class="admin-sidebar">
            <div class="admin-sidebar__logo">Dashboard</div>

            <ul class="admin-sidebar__nav">
                <!-- Check if the user role is admin -->
                <c:if test="${sessionScope.currentEmployee != null && sessionScope.currentEmployee.role == 'admin'}">
                    <li class="admin-sidebar__item">
                        <a href="${pageContext.servletContext.contextPath}/dashboard" class="admin-sidebar__link">
                            <i class="fas fa-tachometer-alt"></i> Dashboard
                        </a>
                    </li>
                </c:if>
                <li class="admin-sidebar__item">
                    <a href="${pageContext.servletContext.contextPath}/manage-products" class="admin-sidebar__link">
                        <i class="fas fa-box"></i> Manage Products
                    </a>
                </li>
                <!-- Check if the user role is admin -->
                <c:if test="${sessionScope.currentEmployee != null && sessionScope.currentEmployee.role == 'admin'}">
                    <li class="admin-sidebar__item">
                        <a href="${pageContext.servletContext.contextPath}/manage-staffs" class="admin-sidebar__link">
                            <i class="fas fa-briefcase"></i> Manage Staffs
                        </a>
                    </li>
                </c:if>
                <li class="admin-sidebar__item">
                    <a href="${pageContext.servletContext.contextPath}/manage-customers" class="admin-sidebar__link">
                        <i class="fas fa-users"></i> Manage Customers
                    </a>
                </li>
                <li class="admin-sidebar__item">
                    <a href="${pageContext.servletContext.contextPath}/manage-orders" class="admin-sidebar__link">
                        <i class="fas fa-clipboard-list"></i> Manage Orders
                    </a>
                </li>
                <!-- Check if the user role is admin -->
                <c:if test="${sessionScope.currentEmployee != null && sessionScope.currentEmployee.role == 'admin'}">
                    <li class="admin-sidebar__item">
                        <a href="${pageContext.servletContext.contextPath}/manage-vouchers" class="admin-sidebar__link">
                            <i class="fas fa-tag"></i> Manage Vouchers
                        </a>
                    </li>
                </c:if>
                <!-- Check if the user role is admin -->
                <c:if test="${sessionScope.currentEmployee != null && sessionScope.currentEmployee.role == 'admin'}">
                    <li class="admin-sidebar__item">
                        <a href="${pageContext.servletContext.contextPath}/manage-discounts" class="admin-sidebar__link">
                            <i class="fas fa-percentage"></i> Manage Discounts
                        </a>
                    </li>
                </c:if>
            </ul>
        </aside>