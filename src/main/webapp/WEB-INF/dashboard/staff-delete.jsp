<%-- 
    Document   : delete-staff
    Created on : Jun 14, 2025, 5:54:24 PM
    Author     : VICTUS
--%>

<%@page import="shop.model.Staff"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>

	<main class="admin-main">
		<div class="table-header">
			<h2 class="table-title">Delete Staff</h2>
		</div>

		<div class="admin-manage-wrapper container py-4">
		

			<!-- Summary -->
                       <%
                      Staff s = (Staff) request.getAttribute("s");
                       
                       %>
			<div class="admin-manage-fieldset mb-4">
				<p class="mb-2"><strong>Full Name:</strong><%=s.getFullName()%></p>
				
				<p class="mb-2"><strong>Email:</strong> <%=s.getEmail()  %></p>
				<p class="mb-2"><strong>Role:</strong> <%=s.getRole()  %></p>
				<br>
				<p class="mb-2 text-warning"><i class="fas fa-triangle-exclamation mr-1"></i> This action cannot be
					undone.</p>
			</div>

			<!-- Delete Buttons -->
			<div class="d-flex justify-content-between align-items-center mt-4">
				<!-- Back Link -->
				<a href="${pageContext.servletContext.contextPath}/manage-staffs?view=list" class="admin-manage-back">
					<i class="fas fa-arrow-left mr-1"></i> Back
				</a>

				<!-- Action Buttons -->
                                <form action="${pageContext.servletContext.contextPath}/manage-staffs" method="POST">
                                     <input type="hidden" name="action" value="delete" />
                                     <input type="hidden" name="id" value="<%=s.getId() %>" />
                                    
                                    <div>
				
					<button type="submit" class="btn admin-manage-button-delete">
						<i class="fas fa-trash mr-1"></i> Delete
					</button>
				</div>
                                    
                                </form>
			</div>

		</div>
	</main>

<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>