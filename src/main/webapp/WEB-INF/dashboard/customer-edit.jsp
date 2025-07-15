<%-- 
    Document   : customer-edit
    Created on : Jun 15, 2025, 9:21:54 AM
    Author     : Le Anh Khoa - CE190449
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>
<c:set var="thisCustomer" value="${requestScope.thisCustomer}"/>
<main class="admin-main">
    <div class="table-header">
        <h2 class="table-title">Edit Customer</h2>
    </div>

    <div class="admin-manage-wrapper container py-4">
        <div class="mb-4">
            <a href="${pageContext.servletContext.contextPath}/manage-customers" class="admin-manage-back mb-5">
                <i class="fas fa-arrow-left mr-1"></i> Back
            </a>
        </div>
        <!-- Success Message Container -->
        <div id="successMessage" style="color: green; margin-bottom: 15px;">
            <c:if test="${not empty successMessage}">
                <p>${successMessage}</p>
            </c:if>
        </div>
        <!-- Error Message Container -->
        <div id="errorMessage" style="color: red; margin-bottom: 15px;">
            <c:if test="${not empty requestScope.errorMessage}">
                <p>${requestScope.errorMessage}</p>
            </c:if>
        </div>
        <c:choose>
            <c:when test="${empty thisCustomer}">
                <p class="sign__empty">This Id does not exists.</p>
            </c:when>
            <c:otherwise>
                <!-- General Information -->
                <form class="mb-4 admin-manage-fieldset" action="${pageContext.servletContext.contextPath}/manage-customers" id="customerEditForm" method="POST">
                    <input type="hidden" name="action" value="edit"/>
                    <input type="hidden" name="id" value="${thisCustomer.customerId}"/>
                    <legend class="admin-manage-subtitle">Customer Information</legend>
                    <div class="row g-3">
                        <!-- If offensive or misleading (e.g., hate speech, impersonation) -->
                        <div class="col-md-6">
                            <label class="form-label admin-manage-label">Username</label>
                            <input type="text" class="form-control admin-manage-input" name="username" value="${thisCustomer.username}" required>
                        </div>
                        <!-- 	If it contains abusive text or spam patterns; or to anonymize -->
                        <div class="col-md-6">
                            <label class="form-label admin-manage-label">Email</label>
                            <input type="email" class="form-control admin-manage-input" name="email" value="${thisCustomer.email}" required>
                        </div>
                        <!-- If it includes slurs, fake names, or inappropriate language -->
                        <div class="col-md-6">
                            <label class="form-label admin-manage-label">Full Name</label>
                            <input type="text" class="form-control admin-manage-input" name="fullName" value="${thisCustomer.fullName}">
                        </div>
                        <!-- To blank or anonymize if it’s spammy or fake -->
                        <div class="col-md-6">
                            <label class="form-label admin-manage-label">Phone</label>
                            <input type="text" class="form-control admin-manage-input" value="${thisCustomer.phone}" name="phone">
                        </div>
                        <!-- 	If it contains abusive content or was misused -->
                        <div class="col-md-6">
                            <label class="form-label admin-manage-label">Address</label>
                            <input type="text" class="form-control admin-manage-input" value="${thisCustomer.address}" name="address">
                        </div>
                    </div>
                    <!-- Action Buttons -->
                    <div class="d-flex justify-content-end">
                        <a href="
                           <c:url value="/manage-customers">
                               <c:param name="view" value="edit"/>
                               <c:param name="customerId" value="${thisCustomer.customerId}"/>
                           </c:url>
                           " type="reset" class="btn admin-manage-reset mr-2">
                            <i class="fas fa-xmark mr-1"></i> Reset
                        </a>
                        <button type="submit" class="btn admin-manage-button">
                            <i class="fas fa-pen-to-square mr-1"></i> Edit
                        </button>
                    </div>
                </form>
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

<script>
    document.getElementById('customerEditForm').addEventListener('submit', function (e) {
        const username = this.querySelector('[name="username"]').value.trim();
        const email = this.querySelector('[name="email"]').value.trim();
        const fullName = this.querySelector('[name="fullName"]').value.trim();
        const phone = this.querySelector('[name="phone"]').value.trim();

        // Username - required
        if (username === '') {
            alert('Username is required.');
            e.preventDefault();
            return;
        }

        // Email - required + valid format
        const emailRegex = /^[a-zA-Z0-9._%+-]+@(gmail\.com|googlemail\.com)$/;
        if (email === '') {
            alert('Email is required.');
            e.preventDefault();
            return;
        } else if (!emailRegex.test(email)) {
            alert('Please enter a valid email address.');
            e.preventDefault();
            return;
        }

        // Full Name - optional but must be 2–50 chars if provided
        if (fullName !== '' && (fullName.length < 2 || fullName.length > 50)) {
            alert('Full name must be between 2 and 50 characters.');
            e.preventDefault();
            return;
        }

        // Phone - optional but must be digits and 9–15 chars if provided
        const phoneRegex = /^[0-9]{9,15}$/;
        if (phone !== '' && !phoneRegex.test(phone)) {
            alert('Phone number must contain 9 to 15 digits only.');
            e.preventDefault();
            return;
        }

        // No validation for address
    });
</script>

<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>
