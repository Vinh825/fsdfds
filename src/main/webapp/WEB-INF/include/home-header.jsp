<%-- 
    Document   : home-header
    Created on : Jun 10, 2025, 10:20:25 AM
    Author     : Ainzle
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="user" value="${sessionScope.currentCustomer}"/>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <!-- CSS -->
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/bootstrap-reboot.min.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/bootstrap-grid.min.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/owl.carousel.min.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/magnific-popup.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/nouislider.min.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/jquery.mCustomScrollbar.min.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/paymentfont.min.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/main.css">
        <style>

        </style>


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
                <div class="container">
                    <div class="row">
                        <div class="col-12">
                            <div class="header__content">
                                <button class="header__menu" type="button">
                                    <span></span>
                                    <span></span>
                                    <span></span>
                                </button>

                                <a href="${pageContext.servletContext.contextPath}/home" class="header__logo">
                                    <img src="${pageContext.servletContext.contextPath}/assets/img/logo.png" alt="">
                                </a>

                                <ul class="header__nav">
                                </ul>
                                <%
                                    Integer cartCount = (Integer) session.getAttribute("cartCount");
                                    if (cartCount == null)
                                        cartCount = 0;
                                %>

                                <div class="header__actions">
                                    <a href="<%= request.getContextPath()%>/cart" class="header__link" style="position: relative; display: inline-block;">
                                        <%-- Badge số lượng --%>
                                        <span style="
                                              position: absolute;
                                              top: -8px;
                                              right: -8px;
                                              background-color: red;
                                              color: white;
                                              font-size: 12px;
                                              font-weight: bold;
                                              padding: 2px 6px;
                                              border-radius: 50%;
                                              z-index: 10;
                                              min-width: 20px;
                                              text-align: center;
                                              "><%= cartCount%></span>

                                        <%-- Icon cart --%>
                                        <svg xmlns='http://www.w3.org/2000/svg' width='32' height='32' viewBox='0 0 512 512'>
                                        <circle cx='176' cy='416' r='16' style='fill:none;stroke-linecap:round;stroke-linejoin:round;stroke-width:32px' />
                                        <circle cx='400' cy='416' r='16' style='fill:none;stroke-linecap:round;stroke-linejoin:round;stroke-width:32px' />
                                        <polyline points='48 80 112 80 160 352 416 352' style='fill:none;stroke-linecap:round;stroke-linejoin:round;stroke-width:32px' />
                                        <path d='M160,288H409.44a8,8,0,0,0,7.85-6.43l28.8-144a8,8,0,0,0-7.85-9.57H128' style='fill:none;stroke-linecap:round;stroke-linejoin:round;stroke-width:32px' />
                                        </svg>
                                    </a>

                                    <c:choose>
                                        <c:when test="${not empty user}">
                                            <div class="admin-dropdown" onclick="toggleDropdown()">
                                                <div class="admin-profile">
                                                    <img src="${user.avatarUrl}" alt="Avatar" class="admin-avatar">
                                                    <div>
                                                        <p class="admin-name">${user.username}</p>
                                                        <span class="admin-role">Customer</span>
                                                    </div>
                                                </div>

                                                <div class="admin-menu" id="adminDropdown">
                                                    <div class="admin-user">
                                                        <span class="admin-role">Customer</span>
                                                        <p class="admin-name">${user.username}</p>
                                                    </div>
                                                    <ul class="admin-links">
                                                        <li><a href="${pageContext.servletContext.contextPath}/profile">My Profile</a></li>
                                                        <li><a href="${pageContext.servletContext.contextPath}/logout">Logout</a></li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.servletContext.contextPath}/login" class="header__login">
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
            </div>
        </header>
        <!-- end header -->
