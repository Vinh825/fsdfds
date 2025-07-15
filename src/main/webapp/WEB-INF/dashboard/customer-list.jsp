<%-- 
    Document   : customer-list
    Created on : Jun 14, 2025, 12:30:33 PM
    Author     : Le Anh Khoa - CE190449
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>
<main class="admin-main">

    <div class="table-header">
        <h2 class="table-title">Manage Customers</h2>
    </div>

    <section class="admin-header">
        <div class="admin-header-top">
            <div class="search-filter-wrapper" style="display: flex; margin-left: auto;">
                <form method="GET" action="${pageContext.servletContext.contextPath}/manage-customers">
                    <input type="text" name="searchName" class="search-input" placeholder="Enter customer name..." value="${param.searchName}">
                    <button type="submit" class="search-btn">Search</button>
                    <a type="button" class="search-btn" href="${pageContext.servletContext.contextPath}/manage-customers">Clear</a>
                </form>
            </div>
        </div>
    </section>

    <c:choose>
        <c:when test="${empty requestScope.paginatedList}">
            <p class="sign__empty">The list is empty</p>
        </c:when>
        <c:otherwise>

            <section class="admin-table-wrapper">
                <div class="table-responsive shadow-sm rounded overflow-hidden">
                    <table class="table table-dark table-bordered table-hover align-middle mb-0">
                        <thead class="table-light text-dark">
                            <tr>
                                <th></th>
                                <th>ID</th>
                                <th>Fullname</th>
                                <th>Username</th>
                                <th>Email</th>
                                <th>Status</th>
                                <th>Email Verified</th>
                                <th style="text-align: center;">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="customer" items="${requestScope.paginatedList}">
                                <tr>
                                    <td><img src="${customer.avatarUrl}" alt="Avatar" class="avatar-img"></td>
                                    <td>${customer.customerId}</td>
                                    <td>${customer.fullName}</td>
                                    <td>${customer.username}</td>
                                    <td>${customer.email}</td>
                                    <td>                            
                                        <span class="badge-status ${customer.isDeactivated ? 'badge-suspend' : 'badge-active'}">
                                            ${customer.isDeactivated ? 'Suspended' : 'Active'}
                                        </span>
                                    </td>
                                    <td>                            
                                        <span class="badge-status ${customer.emailVerified ? 'badge-suspend' : 'badge-active'}">
                                            ${customer.emailVerified ? 'No' : 'Yes'}
                                        </span>
                                    </td>
                                    <td>
                                        <div class="table-actions-center">
                                            <a class="btn-action btn-details" href="
                                               <c:url value="/manage-customers">
                                                   <c:param name="view" value="details"/>
                                                   <c:param name="id" value="${customer.customerId}"/>
                                               </c:url>
                                               ">Details</a>
                                            <a class="btn-action btn-edit" href="
                                               <c:url value="/manage-customers">
                                                   <c:param name="view" value="edit"/>
                                                   <c:param name="id" value="${customer.customerId}"/>
                                               </c:url>
                                               ">Edit</a>
                                            <a class="btn-action btn-delete" href="
                                               <c:url value="/manage-customers">
                                                   <c:param name="view" value="delete"/>
                                                   <c:param name="id" value="${customer.customerId}"/>
                                               </c:url>
                                               ">Delete</a>
                                            <a class="btn-action btn-history" onclick="location.href = '<%= request.getContextPath()%>/manage-customers?view=history&id=${customer.customerId}'">Order
                                                History</a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </section>

            <!-- Pagination -->
            <c:set var="totalPages" value="${requestScope.totalPages}" />
            <c:set var="currentPage" value="${requestScope.currentPage}" />
            <nav class="admin-pagination">
                <ul class="pagination">
                    <!-- Previous button -->
                    <li class="page-item ${requestScope.currentPage == 1 ? 'disabled' : ''}">
                        <c:choose>
                            <c:when test="${requestScope.currentPage == 1}">
                                <a class="page-link">«</a>
                            </c:when>
                            <c:otherwise>
                                <a class="page-link" href="
                                   <c:url value="/manage-customers">
                                       <c:param name="page" value="${requestScope.currentPage - 1}"/>
                                       <c:param name="searchName" value="${param.searchName}"/>
                                   </c:url>
                                   ">«</a>
                            </c:otherwise>
                        </c:choose>
                    </li>
                    <!-- First page -->
                    <li class="page-item ${currentPage == 1 ? 'active' : ''}">
                        <a class="page-link" href="<c:url value='/manage-customers'>
                               <c:param name='page' value='1'/>
                               <c:param name='searchName' value='${param.searchName}'/>
                           </c:url>">1</a>
                    </li>
                    <!-- Ellipsis after first page if currentPage > 4 -->
                    <c:if test="${currentPage > 4}">
                        <li class="page-item"><a class="page-link" href="#">...</a></li>
                        </c:if>
                    <!-- Show pages around current -->
                    <c:set var="startPage" value="${currentPage - 2 < 2 ? 2 : currentPage - 2}" />
                    <c:set var="endPage" value="${currentPage + 2 > totalPages - 1 ? totalPages - 1 : currentPage + 2}" />
                    <c:forEach begin="${startPage}" end="${endPage}" var="pageNum">
                        <li class="page-item ${currentPage == pageNum ? 'active' : ''}">
                            <a class="page-link" href="<c:url value='/manage-customers'>
                                   <c:param name='page' value='${pageNum}'/>
                                   <c:param name='searchName' value='${param.searchName}'/>
                               </c:url>">${pageNum}</a>
                        </li>
                    </c:forEach>
                    <!-- Ellipsis before last page if currentPage < totalPages - 3 -->
                    <c:if test="${currentPage < totalPages - 3}">
                        <li class="page-item"><a class="page-link" href="#">...</a></li>
                        </c:if>
                    <!-- Last page (if more than 1 page) -->
                    <c:if test="${totalPages > 1}">
                        <li class="page-item ${currentPage == totalPages ? 'active' : ''}">
                            <a class="page-link" href="<c:url value='/manage-customers'>
                                   <c:param name='page' value='${totalPages}'/>
                                   <c:param name='searchName' value='${param.searchName}'/>
                               </c:url>">${totalPages}</a>
                        </li>
                    </c:if>
                    <!-- Next button -->
                    <li class="page-item ${requestScope.currentPage == requestScope.totalPages ? 'disabled' : ''}">
                        <c:choose>
                            <c:when test="${requestScope.currentPage == requestScope.totalPages}">
                                <a class="page-link">»</a>
                            </c:when>
                            <c:otherwise>
                                <a class="page-link" href="
                                   <c:url value="/manage-customers">
                                       <c:param name="page" value="${requestScope.currentPage + 1}"/>
                                       <c:param name="searchName" value="${param.searchName}"/>
                                   </c:url>
                                   ">»</a>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </ul>
            </nav>
        </c:otherwise>
    </c:choose>
</main>
<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>