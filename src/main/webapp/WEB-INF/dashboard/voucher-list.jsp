<%@page import="java.util.ArrayList"%>
<%@page import="shop.model.Voucher"%>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>

<main class="admin-main">

    <div class="table-header">
        <h2 class="table-title">Manage Vouchers</h2>
    </div>

    <section class="admin-header">
        <div class="admin-header-top">
            <button class="btn-admin-add"
                    onclick="location.href = '<%= request.getContextPath()%>/manage-vouchers?view=create'">+ Add New Voucher</button>
            <div class="search-filter-wrapper">
                <form action="<%= request.getContextPath()%>/manage-vouchers" method="get">
                    <input type="hidden" name="view" value="search" />
                    <input type="text" name="keyword" class="search-input" placeholder="Enter voucher name..." value="<%= request.getAttribute("keyword") != null ? request.getAttribute("keyword") : ""%>">
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
                            <th>Code</th>
                            <th>Discount (%)</th>
                            <th>Usage</th>
                            <th>Valid From</th>
                            <th>Valid Until</th>
                            <th>Description</th>
                            <th>Status</th>
                            <th style="text-align: center;">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            ArrayList<Voucher> voucherlist = (ArrayList) request.getAttribute("voucherslist");
                            if (voucherlist != null && !voucherlist.isEmpty()) {
                                for (Voucher voucher : voucherlist) {


                        %>
                        <tr>

                            <td> <%= voucher.getCode()%></td>
                            <td><%= voucher.getValue()%></td>
                            <td><%= voucher.getUsageLimit()%></td>
                            <td><%= voucher.getStartDate()%></td>
                            <td><%= voucher.getEndDate()%></td>
                            <td><%= voucher.getDescription()%></td>
                            <td>
                                <%

                                    if (voucher.getActive() == 1) {
                                %>
                                Active
                                <%
                                } else if (voucher.getActive() == 0) {
                                %>
                                Inactive
                                <%
                                } else if (voucher.getActive() == 2) {
                                %>
                                Not yet started
                                <%
                                } else {
                                %>
                                Unknown
                                <%
                                    }
                                %>
                            </td> 
                            <td> <div class="table-actions-center">                                      
                                    <button class="btn-action btn-edit"
                                            onclick="location.href = '<%= request.getContextPath()%>/manage-vouchers?view=edit&id=<%= voucher.getVoucherId()%>'">
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