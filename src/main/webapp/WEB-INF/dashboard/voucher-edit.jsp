<%@page import="shop.model.Voucher"%>

<%@include file="/WEB-INF/include/dashboard-header.jsp" %>


<button class="admin-sidebar-toggle" onclick="$('.admin-sidebar').toggleClass('open')">? Menu</button>

<aside class="admin-sidebar">
    <%-- Sidebar content--%>
</aside>

<%
    Voucher voucher = (Voucher) request.getAttribute("voucher");

%>

<main class="admin-main">
    <div class="table-header">

        <h2 class="table-title">Edit Voucher</h2>
        <%
            if (voucher == null) {
        %>
        <div style="border: 1px solid red; background-color: #ffe6e6; color: red; padding: 15px; border-radius: 5px;">
            <strong>Error:</strong> Voucher not found.
        </div>
       <a href="<%= request.getContextPath()%>/manage-vouchers" class="admin-manage-back">
    <i class="fas fa-arrow-left mr-2" style="font-size: 1.1rem; color: #333;"></i> Back
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
    <form method="post" action="<%= request.getContextPath()%>/manage-vouchers">
        <input type="hidden" name="action" value="edit" >
        <input type="hidden" name="id" value="<%= voucher.getVoucherId() %>" >
        <div class="admin-manage-type voucher-details">
            <fieldset class="mb-4 admin-manage-fieldset">                  
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Code</label>
                        <input type="text" class="form-control admin-manage-input"
                               name="code" value="<%= voucher.getCode()%>" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Value</label>
                        <input type="number" class="form-control admin-manage-input"
                               name="value" value="<%= voucher.getValue()%>" min="0" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Usage Limit</label>
                        <input type="number" class="form-control admin-manage-input"
                               name="usage_limit" value="<%= voucher.getUsageLimit()%>" min="0" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Minimum Order Value</label>
                        <input type="number" class="form-control admin-manage-input"
                               name="min_order_value" value="<%= voucher.getMinOrderValue()%>"  min="0" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Start Date</label>
                        <input type="date" class="form-control admin-manage-input"
                               name="start_date" id="start_date" value="<%= voucher.getStartDate()%>" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">End Date</label>
                        <input type="date" class="form-control admin-manage-input"
                               name="end_date" id="end_date" value="<%= voucher.getEndDate()%>" required>
                    </div>
                    <div class="col-12">
                        <label class="form-label admin-manage-label">Description</label>
                        <textarea class="form-control admin-manage-input"
                                  required      name="description" value="<%= voucher.getDescription()%>"> </textarea>
                    </div>
                    <div class="col-12">
                        <label class="form-label admin-manage-label">Active</label>
                        <select class="form-control admin-manage-input" name="active" required>
                            <option value="1" <%= voucher.getActive() == 1 ? "selected" : ""%>>Active</option>
                            <option value="0" <%= voucher.getActive() == 0 ? "selected" : ""%>>Inactive</option>
                            <option value="2" <%= voucher.getActive() == 2 ? "selected" : ""%>>Not yet started</option>
                        </select>
                    </div>
                </div>
            </fieldset>
        </div>

        <div class="d-flex justify-content-between align-items-center mt-4">
            <a href="<%= request.getContextPath()%>/manage-vouchers" class="admin-manage-back">
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
        function validateDateRange(changedField) {
            const startInput = document.getElementById("start_date");
            const endInput = document.getElementById("end_date");
            const notYetOption = document.querySelector('select[name="active"] option[value="2"]');
            const select = document.querySelector('select[name="active"]');

            const today = new Date();
            today.setHours(0, 0, 0, 0); // reset gi? v? ??u ng?y

            if (startInput.value) {
                const startDate = new Date(startInput.value);
                startDate.setHours(0, 0, 0, 0); // reset gi? v? ??u ng?y

                if (startDate.getTime() > today.getTime()) {
                    // N?u startDate > h?m nay ? cho ph?p "Not yet started"
                    notYetOption.disabled = false;
                } else {
                    // N?u startDate ? h?m nay ? KH?NG cho ch?n "Not yet started"
                    notYetOption.disabled = true;

                    // N?u ?ang ch?n "Not yet started" th? reset l?i v? "Inactive"
                    if (select.value === "2") {
                        select.value = "0";
                    }
                }
            }

            // Ki?m tra ng?y b?t ??u < ng?y k?t th?c
            if (startInput.value && endInput.value) {
                const startDate = new Date(startInput.value);
                const endDate = new Date(endInput.value);

                if (startDate >= endDate) {
                    alert("Start date must be earlier than end date.");
                    if (changedField === "start") {
                        startInput.value = "";
                        startInput.focus();
                    } else if (changedField === "end") {
                        endInput.value = "";
                        endInput.focus();
                    }
                }
            }
        }
        window.addEventListener("DOMContentLoaded", () => {
            document.getElementById("start_date").addEventListener("change", function () {
                validateDateRange("start");
            });
            document.getElementById("end_date").addEventListener("change", function () {
                validateDateRange("end");
            });

            // G?i l?n ??u khi trang v?a load (n?u ?? c? ng?y start ???c prefill)
            validateDateRange("start");
        });

    </script>

    <%
        } // end else
    %>
</main>

<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>
