<%-- 
    Document   : reset-password
    Created on : Jun 13, 2025, 10:30:24 AM
    Author     : Le Anh Khoa - CE190449
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

        <!-- Favicons -->
        <link rel="icon" type="image/png" href="${pageContext.servletContext.contextPath}/assets/icon/logo.png" sizes="32x32">
        <link rel="apple-touch-icon" href="${pageContext.servletContext.contextPath}/assets/icon/logo.png">

        <meta name="description" content="Cosmora Celestica - Selling games and gaming accessories website">
        <meta name="keywords" content="">
        <title>Cosmora Celestica – Games and Accessories</title>

    </head>

    <body>
        <!-- sign in -->
        <div class="sign section--full-bg" data-bg="${pageContext.servletContext.contextPath}/assets/img/bg3.png">
            <div class="container">
                <div class="row">
                    <div class="col-12">
                        <div class="sign__content">
                            <c:choose>
                                <c:when test="${empty sessionScope.currentForgotCustomer}">
                                    <div class="sign__form">
                                        <a href="${pageContext.servletContext.contextPath}/home" class="sign__logo">
                                            <img src="${pageContext.servletContext.contextPath}/assets/img/logo.png" alt="">
                                        </a>
                                        <p class="sign__empty">Your session has expired. Please go back and restart the OTP verification process.</p>
                                        <a type="button" href="${pageContext.servletContext.contextPath}/forgot-password" class="sign__goback">Go Back</a>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <form action="${pageContext.servletContext.contextPath}/reset-password" method="POST" id="resetPasswordForm" class="sign__form">
                                        <a href="${pageContext.servletContext.contextPath}/home" class="sign__logo">
                                            <img src="${pageContext.servletContext.contextPath}/assets/img/logo.png" alt="">
                                        </a>

                                        <input type="hidden" name="email" value="${sessionScope.currentForgotCustomer.email}">

                                        <!-- Message Container -->
                                        <div id="message" style="color: yellow; margin-bottom: 15px;">
                                            <p id="messageText">
                                                <c:if test="${not empty message}">
                                                    ${message}
                                                </c:if>
                                            </p>
                                        </div>

                                        <span class="sign__currentEmail">
                                            <span>Email: </span>${sessionScope.currentForgotCustomer.email}
                                        </span>

                                        <div class="sign__group">
                                            <input type="password" class="sign__input" placeholder="New Password" value="${requestScope.password}" id="password" name="password" autocomplete="new-password" required>
                                        </div>

                                        <div class="sign__group">
                                            <input type="password" class="sign__input" placeholder="Confirm New Password" value="${requestScope.confirmPassword}" id="confirmPassword" name="confirmPassword" autocomplete="new-password" required>
                                        </div>

                                        <button class="sign__btn" type="submit">Submit</button>
                                        <a type="button" href="${pageContext.servletContext.contextPath}/forgot-password" class="sign__goback">Go Back</a>
                                    </form>
                                    <!-- end authorization form -->
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- end sign in -->
        <!-- JS -->
        <script src="${pageContext.servletContext.contextPath}/assets/js/jquery-3.5.1.min.js"></script>
        <script src="${pageContext.servletContext.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.servletContext.contextPath}/assets/js/owl.carousel.min.js"></script>
        <script src="${pageContext.servletContext.contextPath}/assets/js/jquery.magnific-popup.min.js"></script>
        <script src="${pageContext.servletContext.contextPath}/assets/js/wNumb.js"></script>
        <script src="${pageContext.servletContext.contextPath}/assets/js/nouislider.min.js"></script>
        <script src="${pageContext.servletContext.contextPath}/assets/js/jquery.mousewheel.min.js"></script>
        <script src="${pageContext.servletContext.contextPath}/assets/js/jquery.mCustomScrollbar.min.js"></script>
        <script src="${pageContext.servletContext.contextPath}/assets/js/main.js"></script>
        <script>
            const resetPasswordForm = document.getElementById("resetPasswordForm");

            if (resetPasswordForm) {
                resetPasswordForm.addEventListener("submit", function (event) {

                    const password = document.getElementById('password').value.trim();
                    const confirmPassword = document.getElementById('confirmPassword').value.trim();

                    showMessage("Processing...");

                    const errorMessage = isValidResetForm(password, confirmPassword);
                    if (errorMessage) {
                        event.preventDefault(); // Block form submission
                        showMessage(errorMessage);
                        return;
                    }

                    // Allow traditional POST to go through
                });
            }

            function isValidResetForm(password, confirmPassword) {
                const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;

                if (password.length < 8) {
                    return "Password must be at least 8 characters long.";
                } else if (!passwordRegex.test(password)) {
                    return "Password must contain at least 1 letter and 1 number.";
                }

                if (password !== confirmPassword) {
                    return "Passwords do not match.";
                }

                return null;
            }
        </script>
    </body>

</html>