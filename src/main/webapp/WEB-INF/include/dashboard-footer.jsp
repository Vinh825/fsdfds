<%-- 
    Document   : dashboard-header
    Created on : Jun 10, 2025, 10:20:40 AM
    Author     : Ainzle
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
    const currentPath = window.location.pathname;

    // Loop through all sidebar links
    document.querySelectorAll('.admin-sidebar__link').forEach(link => {
        const linkPath = new URL(link.href).pathname; // Get full path of href
        if (currentPath.startsWith(linkPath)) {
            link.classList.add('active');
        }
    });
</script>

</body>

</html>