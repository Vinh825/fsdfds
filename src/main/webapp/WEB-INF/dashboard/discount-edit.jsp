<%@page import="shop.model.Product"%>
<%@page import="shop.model.Voucher"%>

<%@include file="/WEB-INF/include/dashboard-header.jsp" %>


<button class="admin-sidebar-toggle" onclick="$('.admin-sidebar').toggleClass('open')">? Menu</button>

<aside class="admin-sidebar">
    <%-- Sidebar content--%>
</aside>

<%
    Product product = (Product) request.getAttribute("product");
    int id = (int) request.getAttribute("id");
%>

<main class="admin-main">
    <div class="table-header">

        <h2 class="table-title">Edit Discount</h2>
        <%
            if (product == null) {
        %>
        <div style="border: 1px solid red; background-color: #ffe6e6; color: red; padding: 15px; border-radius: 5px;">
            <strong>Error:</strong> Voucher not found.
        </div>
        <a href="<%= request.getContextPath()%>/manage-vouchers" class="btn btn-secondary mt-3">
            ? Back to Voucher List
        </a>
        <%
        } else {
        %>
    </div>
    <%
        String error = (String) request.getAttribute("message");
        if (error != null) {
    %>
    <div style="border: 1px solid red; background-color: #ffe6e6; color: red; padding: 10px; margin-bottom: 15px; border-radius: 5px;">
        <strong>Error:</strong> <%= error%>
    </div>

    <%
        }
    %>
    <form method="post" action="<%= request.getContextPath()%>/manage-discounts">
        <input type="hidden" name="action" value="edit" >
        <input type="hidden" name="id" value="<%= id%>" >
        <div class="admin-manage-type voucher-details">
            <fieldset class="mb-4 admin-manage-fieldset">                  
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Name</label>
                        <input type="text" class="form-control admin-manage-input"
                               name="name" value="<%= product.getName()%>"  readonly>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Price</label>
                        <input type="number" class="form-control admin-manage-input"
                               id="price"   name="price"  value="<%=product.getPrice()%>" readonly >
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Sale Price</label>
                        <input type="number" class="form-control admin-manage-input"
                               step="0.01"  id="saleprice" name="saleprice" value="<%= product.getSalePrice()%>" required>
                    </div>

                    <div class="col-12">
                        <label class="form-label admin-manage-label">Active</label>
                        <select class="form-control admin-manage-input" name="active" >
                            <option value="1" <%= product.getActive() == 1 ? "selected" : ""%>>Active</option>
                            <option value="0" <%= product.getActive() == 0 ? "selected" : ""%>>Expired</option>
                        </select>
                    </div>
                </div>
            </fieldset>
        </div>

        <div class="d-flex justify-content-between align-items-center mt-4">
            <a href="<%= request.getContextPath()%>/manage-discounts" class="admin-manage-back">
                <i class="fas fa-arrow-left mr-1"></i> Back
            </a>

            <div>
                <button type="submit" class="btn admin-manage-button">
                    <i class="fas fa-pen-to-square mr-1"></i> Update
                </button>
            </div>
        </div>

    </form>
    <script>
        window.onload = function () {
            document.querySelector("form").onsubmit = function () {
                const price = parseFloat(document.getElementById("price").value);
                const salePriceInput = document.getElementById("saleprice");
                const salePrice = parseFloat(salePriceInput.value);

                if (isNaN(salePrice) || salePrice < 0) {
                    alert("Please enter a sale price of 0 or higher.");
                    salePriceInput.focus();
                    return false;
                }

                if (salePrice > price) {
                    alert("Sale Price must be less than or equal to Price.");
                    salePriceInput.focus();
                    return false;
                }

                return true;
            };
        };
    </script>

    <%
        } // end else
    %>




</main>

<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>
