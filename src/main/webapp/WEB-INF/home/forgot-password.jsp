<%-- 
    Document   : forgot-password
    Created on : Jun 10, 2025, 9:14:11 PM
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
                            <!-- authorization form -->
                            <div class="sign__form">
                                <a href="/home" class="sign__logo">
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

                                <!-- Send OTP Form -->
                                <form id="sendOtpForm" class="sign__group">
                                    <div class="sign__group sign__group--otp">
                                        <input type="email" class="sign__input" placeholder="Email" name="email" id="emailForgotInput" value="${currentForgotEmail}" required>
                                        <button type="button" id="sendOtpForgotBtn" class="send-otp-link">Send OTP</button>
                                    </div>
                                    <p id="cooldownText" style="text-align: right; font-size: 12px; color: #999;"></p>


                                    <div class="sign__group" id="otpSection">
                                        <input type="text" class="sign__input" name="otp" id="otpForgotInput" placeholder="Enter OTP" required>
                                        <button type="button" id="verifyOtpForgotBtn" class="sign__btn">Verify OTP</button>
                                    </div>
                                </form>
                                <span class="sign__text">An OTP will be sent to your registered email address.</span>
                                <a type="button" href="${pageContext.servletContext.contextPath}/login" class="sign__goback">Go Back</a>
                                <!-- end authorization form -->
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
                const contextPath = window.location.pathname.split('/')[1]; // Get the context path dynamically

                let countdown = 30;
                let countdownInterval;

                const sendOtpForgotBtn = document.getElementById('sendOtpForgotBtn');
                const verifyOtpForgotBtn = document.getElementById('verifyOtpForgotBtn');
                const emailForgotInput = document.getElementById('emailForgotInput');
                const otpForgotInput = document.getElementById('otpForgotInput');
                const cooldownText = document.getElementById('cooldownText');

                if (sendOtpForgotBtn) {
                    sendOtpForgotBtn.addEventListener('click', function () {
                        const email = emailForgotInput.value.trim();

                        showMessage("Processing...");

                        const errorMessage = isValidEmail(email);
                        if (errorMessage) {
                            showMessage(errorMessage);
                            return;
                        }

                        this.disabled = true;

                        sendOtpToBackend(email);

                        startCountdown();
                    });
                }

                if (verifyOtpForgotBtn) {
                    verifyOtpForgotBtn.addEventListener('click', function () {
                        const email = emailForgotInput.value.trim();
                        const otp = otpForgotInput.value.trim();

                        showMessage("Processing...");

                        const errorMessage = validateFormForgotPassword(email, otp);
                        if (errorMessage) {
                            showMessage(errorMessage);
                            return;
                        }

                        sendOtpForVerification(email, otp);
                    });
                }

                function validateFormForgotPassword(email, otp) {
                    const emailError = isValidEmail(email);
                    if (emailError)
                        return emailError;

                    const otpError = isValidOtp(otp);
                    if (otpError)
                        return otpError;

                    return null;
                }

                function isValidEmail(email) {
                    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
                    if (!email) {
                        emailForgotInput.focus();
                        return "Please enter your email.";
                    }

                    if (!emailRegex.test(email)) {
                        emailForgotInput.focus();
                        return "Please enter a valid email address (example@gmail.com).";
                    }
                    return null;
                }

                function isValidOtp(otp) {
                    const otpRegex = /^[0-9]+$/;
                    if (!otp) {
                        otpForgotInput.focus();
                        return "Please enter your otp.";
                    }
                    if (!otpRegex.test(otp) || otp.length !== 6) {
                        otpForgotInput.focus();
                        return "Please enter a valid otp (6-digit).";
                    }
                }

                function sendOtpToBackend(email) {
                    fetch('/' + contextPath + '/forgot-password', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: 'email=' + encodeURIComponent(email) + '&action=sendOtp'
                    })
                            .then(res => res.json())  // Parse response as JSON
                            .then(data => {
                                if (data.success) {
                                    showMessage(data.message);
                                } else {
                                    showMessage(data.message);
                                    sendOtpForgotBtn.disabled = false;
                                    clearInterval(countdownInterval);
                                    cooldownText.textContent = '';
                                }
                            })
                            .catch(() => {
                                showMessage("An error occurred. Please try again.");
                                sendOtpForgotBtn.disabled = false;
                                clearInterval(countdownInterval);
                                cooldownText.textContent = '';
                            });
                }

                function sendOtpForVerification(email, otp) {
                    fetch('/' + contextPath + '/forgot-password', {
                        method: 'POST',
                        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                        body: 'email=' + encodeURIComponent(email) +
                                '&otp=' + encodeURIComponent(otp) +
                                '&action=verifyOtp'
                    })
                            .then(res => res.json())  // Parse response as JSON
                            .then(data => {
                                if (data.success) {
                                    showMessage(data.message);
                                    window.location.href = data.redirectUrl;
                                } else {
                                    showMessage(data.message);
                                }
                            })
                            .catch(() => {
                                showMessage("An error occurred. Please try again.");
                            });
                }

                function startCountdown() {
                    countdown = 30;
                    cooldownText.textContent = "You can resend OTP in " + countdown + "s";  // Initial message

                    countdownInterval = setInterval(() => {
                        countdown--;

                        if (countdown <= 0) {
                            clearInterval(countdownInterval);
                            cooldownText.textContent = '';
                            sendOtpForgotBtn.disabled = false;
                            sendOtpForgotBtn.textContent = 'Resend OTP';
                        } else {
                            cooldownText.textContent = "You can resend OTP in " + countdown + "s";  // Update the countdown text
                        }
                    }, 1000);
                }

                function showMessage(msg) {
                    document.getElementById('messageText').textContent = "";
                    document.getElementById('messageText').textContent = msg;
                }
            </script>
    </body>

</html>