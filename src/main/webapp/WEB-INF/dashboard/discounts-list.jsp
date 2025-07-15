
<%@page import="java.util.ArrayList"%>
<%@page import="shop.model.Product"%>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>

<main class="admin-main">

    <div class="table-header">
        <h2 class="table-title">Manage Discounts</h2>
    </div>

    <section class="admin-header">
        <div class="admin-header-top">
            <div class="search-filter-wrapper">
                <form action="<%= request.getContextPath()%>/manage-discounts" method="get">
                    <input type="hidden" name="view" value="search" />
                    <input type="text" name="keyword" class="search-input" placeholder="Enter discount name..." value="<%= request.getAttribute("keyword") != null ? request.getAttribute("keyword") : ""%>">
                    <button class="search-btn">Search</button>                    
                </form>

            </div>
        </div>  
        <%
            String success = (String) session.getAttribute("message");
            if (success != null) {
        %>
        <div style="border: 1px solid green; background-color: #e6ffe6; color: green; padding: 10px; margin-bottom: 15px; border-radius: 5px;">
            <%= success%>
        </div>
        <%
                session.removeAttribute("message");
            }
        %>
        <section class="admin-table-wrapper">
            <div class="table-responsive shadow-sm rounded overflow-hidden">
                <table class="table table-dark table-bordered table-hover align-middle mb-0">
                    <thead class="table-light text-dark">

                        <tr>
                            <th>Product Name</th>
                            <th>Price</th>
                            <th>Sale Price</th>
                            <th>Active</th>
                            <th style="text-align: center;">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            ArrayList<Product> productList = (ArrayList) request.getAttribute("discountlist");
                            if (productList != null && !productList.isEmpty()) {
                                for (Product product : productList) {

                        %>
                        <tr>

                            <td> <%= product.getName()%></td>
                            <td><%= product.getPrice()%></td>
                            <td><%= product.getSalePrice()%></td>
                            <td>
                                <%

                                    if (product.getActive() == 1) {
                                %>  
                                Active
                                <%
                                } else if (product.getActive() == 0) {
                                %>
                                Expired
                                <%
                                    }
                                %>


                            </td> 
                            <td> <div class="table-actions-center">                                      
                                    <button class="btn-action btn-edit"
                                            onclick="location.href = '<%= request.getContextPath()%>/manage-discounts?view=edit&id=<%= product.getProductId()%>'">
                                        Edit
                                    </button>

                                </div> </td> <% }
                                } else {%>
                            <td style="color: orange; margin-bottom: 10px;">No vouchers found.</td>
                            <%}%>


                        </tr>
                    </tbody>

                </table>

            </div>
        </section>
</main>
<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>



