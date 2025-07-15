<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>
<main class="admin-main">
    <div class="table-header">
        <h2 class="table-title">Create New Staff</h2>
    </div>

    <div class="admin-manage-wrapper container py-4">



        <!-- General Information -->

        <form action="${pageContext.servletContext.contextPath}/manage-staffs" method="POST" id="createStaffForm" enctype="multipart/form-data">
            <input type="hidden" name="action" value="create" />
            <fieldset class="mb-4 admin-manage-fieldset">
                <legend class="admin-manage-subtitle">Staff Information</legend>


                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Full Name</label>
                        <input type="text" class="form-control admin-manage-input" name="username" id="username" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Email</label>
                        <input type="email" class="form-control admin-manage-input" name="email" id="email" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Password</label>
                        <input type="password" class="form-control admin-manage-input" name="password" id="password" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Confirm password</label>
                        <input type="password" class="form-control admin-manage-input" name="confirmPassword" id="confirmPassword" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Phone</label>
                        <input type="text" class="form-control admin-manage-input" name="phone" id="phone">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Gender</label>
                        <select class="form-select admin-manage-input" name="gender" required>
                            <option value="">-- Select Gender --</option>
                            <option value="Male">Male</option>
                            <option value="Female">Female</option>
                            
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Role</label>
                        <input type="text" class="form-control admin-manage-input" name="role" value="staff" readonly required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Avatar Image</label>
                        <input type="file" class="form-control admin-manage-input" name="avatar_url" accept=".png" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Date of Birth</label>
                        <input type="date" class="form-control admin-manage-input" name="date_of_birth" required>
                    </div>
                </div>

            </fieldset>

            <div class="d-flex justify-content-between align-items-center mt-4">
                <a href="${pageContext.servletContext.contextPath}/manage-staffs?view=list" class="admin-manage-back">
                    <i class="fas fa-arrow-left mr-1"></i> Back
                </a>

                <div>
                    <button type="reset" class="btn admin-manage-reset mr-2">
                        <i class="fas fa-xmark mr-1"></i> Reset
                    </button>
                    <button type="submit" class="btn admin-manage-button">
                        <i class="fas fa-plus mr-1"></i> Create
                    </button>

                </div>
            </div>
        </form>
    </div>
</main>
<script>
// Validate form on submit
    document.getElementById("createStaffForm").addEventListener("submit", function (e) {
        const username = this.querySelector('[name="username"]').value;
        const email = this.querySelector('[name="email"]').value;
        const password = this.querySelector('[name="password"]').value;
        const confirmPassword = this.querySelector('[name="confirmPassword"]').value;
        const phone = this.querySelector('[name="phone"]').value;

        // Password - must be at least 8 characters long and contain at least 1 letter and 1 number
        const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;
        if (password.length < 8) {
            alert("Password must be at least 8 characters long.");
            e.preventDefault();
            return;
        } else if (!passwordRegex.test(password)) {
            alert("Password must contain at least 1 letter and 1 number.");
            e.preventDefault();
            return;
        }

        // Confirm Password - must match the password
        if (password !== confirmPassword) {
            alert("Passwords do not match.");
            e.preventDefault();
            return;
        }

        // Email - must be a valid Gmail address
        const emailRegex = /^[a-zA-Z0-9._%+-]+@(gmail\.com|googlemail\.com)$/;
        if (!emailRegex.test(email)) {
            alert("Please enter a valid Google email address (gmail.com or googlemail.com).");
            e.preventDefault();
            return;
        }

        // Phone - optional but must be digits and 9–15 chars if provided
        const phoneRegex = /^[0-9]{9,15}$/;
        if (phone !== '' && !phoneRegex.test(phone)) {
            alert('Phone number must contain 9 to 15 digits only.');
            e.preventDefault();
            return;
        }
    });
</script>
<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>