<%-- 
    Document   : register
    Created on : Jun 10, 2025, 9:06:15 PM
    Author     : CE190449 - Le Anh Khoa
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
                            <!-- registration form -->
                            <form action="${pageContext.servletContext.contextPath}/register" method="POST" id="registerForm" class="sign__form">
                                <a href="${pageContext.servletContext.contextPath}/home" class="sign__logo">
                                    <img src="${pageContext.servletContext.contextPath}/assets/img/logo.png" alt="">
                                </a>

                                <!-- Message Container -->
                                <div id="message" style="color: yellow; margin-bottom: 15px;">
                                    <p id="messageText">
                                        <c:if test="${not empty message}">
                                            ${message}
                                        </c:if>
                                    </p>
                                </div>

                                <div class="sign__group">
                                    <input type="text" class="sign__input" placeholder="Full Name" id="fullName" name="fullName" value="${requestScope.fullName}" required>
                                </div>

                                <div class="sign__group">
                                    <input type="text" class="sign__input" placeholder="Username" id="username" name="username" value="${requestScope.username}" required>
                                </div>

                                <div class="sign__group">
                                    <input type="email" class="sign__input" placeholder="Email" id="email" name="email" value="${requestScope.email}" autocomplete="username" required>
                                </div>

                                <div class="sign__group">
                                    <input type="password" class="sign__input" placeholder="Password" id="password" name="password" value="${requestScope.password}" autocomplete="new-password" required>
                                </div>

                                <div class="sign__group">
                                    <input type="password" class="sign__input" placeholder="Confirm Password" id="confirmPassword" value="${requestScope.confirmPassword}" name="confirmPassword" autocomplete="new-password" required>
                                </div>

                                <button class="sign__btn" type="submit">Sign up</button>

                                <span class="sign__delimiter">or</span>

                                <div class="sign__social">
                                    <a class="gl" href="#"><svg xmlns='http://www.w3.org/2000/svg' class='ionicon'
                                                                viewBox='0 0 512 512'>
                                        <path
                                            d='M473.16 221.48l-2.26-9.59H262.46v88.22H387c-12.93 61.4-72.93 93.72-121.94 93.72-35.66 0-73.25-15-98.13-39.11a140.08 140.08 0 01-41.8-98.88c0-37.16 16.7-74.33 41-98.78s61-38.13 97.49-38.13c41.79 0 71.74 22.19 82.94 32.31l62.69-62.36C390.86 72.72 340.34 32 261.6 32c-60.75 0-119 23.27-161.58 65.71C58 139.5 36.25 199.93 36.25 256s20.58 113.48 61.3 155.6c43.51 44.92 105.13 68.4 168.58 68.4 57.73 0 112.45-22.62 151.45-63.66 38.34-40.4 58.17-96.3 58.17-154.9 0-24.67-2.48-39.32-2.59-39.96z' />
                                        </svg></a>
                                </div>

                                <span class="sign__text">Already have an account? <a href="${pageContext.servletContext.contextPath}/login">Login!</a></span>
                                <a type="button" href="${pageContext.servletContext.contextPath}/home" class="sign__goback">Go Back</a>
                            </form>
                            <!-- registration form -->
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
            const registerForm = document.getElementById("registerForm");

            if (registerForm) {
                registerForm.addEventListener("submit", function (event) {

                    const username = document.getElementById('username').value.trim();
                    const email = document.getElementById('email').value.trim();
                    const password = document.getElementById('password').value.trim();
                    const confirmPassword = document.getElementById('confirmPassword').value.trim();
                    const fullName = document.getElementById('fullName').value.trim();

                    showMessage("Processing...");

                    const errorMessage = isValidRegister(username, email, password, confirmPassword, fullName);
                    if (errorMessage) {
                        event.preventDefault(); // prevent form from submitting if invalid
                        showMessage(errorMessage);
                        return;
                    }
                });
            }

            function isValidRegister(username, email, password, confirmPassword, fullName) {
                const usernameRegex = /^[a-zA-Z0-9_]{3,20}$/;
                if (!username || !usernameRegex.test(username)) {
                    return "Username must be 3-20 characters long and can only contain letters, numbers, and underscores.";
                }

                const emailRegex = /^[a-zA-Z0-9._%+-]+@(gmail\.com|googlemail\.com)$/;
                if (!emailRegex.test(email)) {
                    return "Please enter a valid Google email address (gmail.com or googlemail.com).";
                }

                const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;
                if (password.length < 8) {
                    return "Password must be at least 8 characters long.";
                } else if (!passwordRegex.test(password)) {
                    return "Password must contain at least 1 letter and 1 number.";
                }

                if (password !== confirmPassword) {
                    return "Passwords do not match.";
                }

                const nameRegex = /^[a-zA-Z]+(?:\s[a-zA-Z]+)*$/;
                if (!nameRegex.test(fullName)) {
                    return "Full Name can only contain letters and single spaces (no multiple spaces).";
                }

                return null;
            }

            function showMessage(msg) {
                document.getElementById('messageText').textContent = "";
                document.getElementById('messageText').textContent = msg;
            }
        </script>
    </body>

</html>