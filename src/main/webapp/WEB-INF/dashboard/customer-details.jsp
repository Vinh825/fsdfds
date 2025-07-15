<%-- 
    Document   : customer-details
    Created on : Jun 16, 2025, 9:43:12 AM
    Author     : CE190449 - Le Anh Khoa
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>
<c:set var="thisCustomer" value="${requestScope.thisCustomer}"/>
<main class="admin-main">
    <div class="table-header">
        <h2 class="table-title">Customer Details</h2>
    </div>

    <div class="admin-manage-wrapper container py-4">
        <div class="mb-4">
            <a href="${pageContext.servletContext.contextPath}/manage-customers" class="admin-manage-back mb-5">
                <i class="fas fa-arrow-left mr-1"></i> Back
            </a>
        </div>
        <c:choose>
            <c:when test="${empty thisCustomer}">
                <p class="sign__empty">This Id does not exists.</p>
            </c:when>
            <c:otherwise>
                <!-- General Information -->
                <!-- General Information -->
                <div class="admin-manage-fieldset mb-4">
                    <p class="mb-2">
                        <strong>Avatar:</strong><br>
                        <img src="${thisCustomer.avatarUrl}" alt="Customer Avatar" style="max-width: 120px; border-radius: 10px; margin-top: 5px;" />
                    </p>
                    <div class="row">
                        <div class="col-12 col-md-6">
                            <p class="mb-2"><strong>Full Name:</strong> ${thisCustomer.fullName}</p>
                            <p class="mb-2"><strong>Username:</strong> ${thisCustomer.username}</p>
                            <p class="mb-2"><strong>Email:</strong> ${thisCustomer.email}</p>
                            <p class="mb-2"><strong>Phone Number:</strong> 
                                <c:choose>
                                    <c:when test="${not empty thisCustomer.phone}">
                                        ${thisCustomer.phone}
                                    </c:when>
                                    <c:otherwise>Unspecified</c:otherwise>
                                </c:choose>
                            </p>
                            <p class="mb-2"><strong>Gender:</strong> 
                                <c:choose>
                                    <c:when test="${thisCustomer.gender == '0'}">Male</c:when>
                                    <c:when test="${thisCustomer.gender == '1'}">Female</c:when>
                                    <c:when test="${thisCustomer.gender == '2'}">Other</c:when>
                                    <c:otherwise>Unspecified</c:otherwise>
                                </c:choose>
                            </p>
                            <p class="mb-2"><strong>Address:</strong>
                                <c:choose>
                                    <c:when test="${not empty thisCustomer.address}">
                                        ${thisCustomer.address}
                                    </c:when>
                                    <c:otherwise>Unspecified</c:otherwise>
                                </c:choose>
                            </p>
                            <p class="mb-2"><strong>Date of Birth:</strong>
                                <c:choose>
                                    <c:when test="${not empty thisCustomer.dateOfBirth}">
                                        <fmt:formatDate value="${thisCustomer.dateOfBirth}" pattern="dd MMM yyyy" />
                                    </c:when>
                                    <c:otherwise>Unspecified</c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                        <div class="col-12 col-md-6">
                            <p class="mb-2"><strong>Last Login:</strong> 
                                <c:choose>
                                    <c:when test="${not empty thisCustomer.lastLogin}">
                                        <fmt:formatDate value="${thisCustomer.lastLogin}" pattern="dd MMM yyyy HH:mm" />
                                    </c:when>
                                    <c:otherwise>Unspecified</c:otherwise>
                                </c:choose>
                            </p>
                            <p class="mb-2"><strong>Account Created At:</strong> 
                                <fmt:formatDate value="${thisCustomer.createdAt}" pattern="dd MMM yyyy HH:mm" />
                            </p>
                            <p class="mb-2"><strong>Last Updated At:</strong> 
                                <fmt:formatDate value="${thisCustomer.updatedAt}" pattern="dd MMM yyyy HH:mm" />
                            </p>
                            <p class="mb-2"><strong>Status:</strong> 
                                <c:choose>
                                    <c:when test="${thisCustomer.isDeactivated}">Suspended</c:when>
                                    <c:otherwise>Active</c:otherwise>
                                </c:choose>
                            </p>
                            <p class="mb-2"><strong>Email Verified:</strong> 
                                <c:choose>
                                    <c:when test="${thisCustomer.emailVerified}">Yes</c:when>
                                    <c:otherwise>No</c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                    </div>
                </div>

                <div class="d-flex justify-content-between align-items-center mt-4">
                    <!-- Back Link -->
                    <a href="${pageContext.servletContext.contextPath}/manage-customers" class="admin-manage-back">
                        <i class="fas fa-arrow-left mr-1"></i> Back
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>