<%--
    Document    : product-edit.jsp
    Created on  : Jun 10, 2025, 10:55:00 PM
    Author      : HoangSang
--%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.ArrayList"%>
<%@page import="shop.model.Product"%>
<%@page import="shop.model.Category"%>
<%@page import="shop.model.Brand"%>
<%@page import="shop.model.GameDetails"%>
<%@page import="shop.model.GameKey"%>
<%@page import="shop.model.StorePlatform"%>
<%@page import="shop.model.OperatingSystem"%>
<%@page import="shop.model.ProductAttribute"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>

<%
    Product product = (Product) request.getAttribute("product");
    List<Category> categories = (List<Category>) request.getAttribute("categoriesList");
    List<Brand> brands = (List<Brand>) request.getAttribute("brandsList");
    Map<String, String> attributeMap = (Map<String, String>) request.getAttribute("attributeMap");
    if (attributeMap == null) {
        attributeMap = new java.util.HashMap<>();
    }

    GameDetails gameDetails = (GameDetails) request.getAttribute("gameDetails");
    List<GameKey> gameKeys = (List<GameKey>) request.getAttribute("gameKeys");
    List<StorePlatform> allPlatforms = (List<StorePlatform>) request.getAttribute("allPlatforms");
    List<OperatingSystem> allOS = (List<OperatingSystem>) request.getAttribute("allOS");
    Set<Integer> selectedPlatformIds = (Set<Integer>) request.getAttribute("selectedPlatformIds");
    Set<Integer> selectedOsIds = (Set<Integer>) request.getAttribute("selectedOsIds");

    String initialProductType = "";
    if (product != null && categories != null) {
        for (Category cat : categories) {
            if (product.getCategoryId() == cat.getCategoryId()) {
                initialProductType = cat.getName().toLowerCase().replaceAll("\\s+", "")
                        .replace("chuột", "mouse")
                        .replace("bànphím", "keyboard")
                        .replace("tainghe", "headphone")
                        .replace("taycầm(controller)", "controller")
                        .replace("game", "game");
                break;
            }
        }
    }
    boolean isGameProduct = "game".equals(initialProductType);
%>

<style>
    /* CSS chung */
    .admin-manage-subtitle {
        font-size: 1.25rem;
        font-weight: 500;
        margin-bottom: 1rem;
        padding-bottom: 0.5rem;
    }

    /* === BẮT ĐẦU CSS CHO UPLOAD ẢNH ĐÃ ĐƯỢC THỐNG NHẤT === */
    .image-uploader {
        border: 2px dashed #ddd;
        border-radius: 8px;
        padding: 20px;
        text-align: center;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        width: 100%;
        height: 180px;
        display: flex;
        justify-content: center;
        align-items: center;
        background-color: #4a4e69;
        transition: border-color 0.3s;
    }
    .image-uploader:hover {
        border-color: #007bff;
    }
    .image-uploader__content {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
    }
    .image-uploader__icon {
        font-size: 40px;
        color: #6c757d;
    }
    .image-uploader p {
        margin: 10px 0 0;
        color: #6c757d;
        font-size: 0.9em;
    }
    .image-uploader input[type="file"] {
        display: none;
    }
    .image-preview {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        object-fit: cover;
        display: none; /* Mặc định ẩn */
    }
    .remove-image-btn {
        position: absolute;
        top: 5px;
        right: 5px;
        background-color: rgba(214, 48, 49, 0.8);
        color: white;
        border: none;
        border-radius: 50%;
        width: 25px;
        height: 25px;
        font-size: 16px;
        line-height: 25px;
        text-align: center;
        cursor: pointer;
        display: none; /* Mặc định ẩn */
        z-index: 10;
        justify-content: center;
        align-items: center;
    }

    /* Class quản lý trạng thái có ảnh */
    .image-uploader.has-image .image-preview,
    .image-uploader.has-image .remove-image-btn {
        display: flex;
    }
    .image-uploader.has-image .image-uploader__content {
        display: none;
    }
</style>

<main class="admin-main">
    <div class="table-header">
        <h2 class="table-title">Edit Product</h2>
    </div>

    <div class="admin-manage-wrapper container py-4">
        <div class="mb-4">
            <a href="<%= request.getContextPath()%>/manage-products?action=list" class="admin-manage-back mb-5">
                <i class="fas fa-arrow-left mr-1"></i> Back
            </a>
        </div>

        <c:if test="${not empty requestScope.errorMessage}">
            <div class="alert alert-danger" role="alert">${requestScope.errorMessage}</div>
        </c:if>

        <% if (product != null) {%>
        <form action="<%= request.getContextPath()%>/manage-products?action=update" method="post" enctype="multipart/form-data">
            <input type="hidden" name="productId" value="<%= product.getProductId()%>">
            <input type="hidden" name="gameDetailsId" value="<%= (gameDetails != null && gameDetails.getGameDetailsId() > 0) ? gameDetails.getGameDetailsId() : "0"%>">
            <input type="hidden" id="productTypeField" name="productType" value="">

            <div class="mb-4">
                <label for="categoryId" class="form-label admin-manage-label">Product Type</label>
                <select class="form-select admin-manage-input" id="categoryId" name="categoryId" required onchange="handleProductTypeChange(this)" <%= isGameProduct ? "disabled" : ""%>>
                    <% if (categories != null) {
                            for (Category cat : categories) {
                                String normalizedName = cat.getName().toLowerCase().replaceAll("\\s+", "")
                                        .replace("chuột", "mouse")
                                        .replace("bànphím", "keyboard")
                                        .replace("tainghe", "headphone")
                                        .replace("taycầm(controller)", "controller")
                                        .replace("game", "game");

                                if (!isGameProduct && "game".equals(normalizedName)) {
                                    continue;
                                }

                                boolean isSelected = product.getCategoryId() == cat.getCategoryId();
                    %>
                    <option value="<%= cat.getCategoryId()%>" data-normalized-name="<%= normalizedName%>" <%= isSelected ? "selected" : ""%>><%= cat.getName()%></option>
                    <% }
                        }%>
                </select>
                <% if (isGameProduct) {%>
                <input type="hidden" name="categoryId" value="<%= product.getCategoryId()%>" />
                <% }%>
            </div>

            <div class="mb-4 admin-manage-fieldset">
                <h3 class="admin-manage-subtitle">General Information</h3>
                <div class="row g-3">
                    <div class="col-md-12">
                        <label class="form-label admin-manage-label">Product Name</label>
                        <input type="text" class="form-control admin-manage-input" name="name" value="<%= product.getName()%>" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Price ($)</label>
                        <input type="number" class="form-control admin-manage-input" step="0.01" name="price" value="<%= product.getPrice()%>" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Quantity</label>
                        <input type="number" class="form-control admin-manage-input" name="quantity" value="<%= product.getQuantity()%>" required>
                    </div>
                    <div class="col-12">
                        <label class="form-label admin-manage-label">Description</label>
                        <textarea class="form-control admin-manage-input" name="description" rows="3"><%= product.getDescription()%></textarea>
                    </div>
                </div>
            </div>

            <div class="mb-4 admin-manage-fieldset p-4 border rounded">
                <h3 class="admin-manage-subtitle">Product Images (Up to 6 images)</h3>
                <div class="row g-3">
                    <%
                        List<String> imageUrls = product.getImageUrls();
                        if (imageUrls == null) {
                            imageUrls = new java.util.ArrayList<>();
                        }
                        for (int i = 0; i < 6; i++) {
                            String existingImageUrl = (i < imageUrls.size()) ? imageUrls.get(i) : null;
                            boolean hasImage = existingImageUrl != null && !existingImageUrl.isEmpty();
                    %>
                    <div class="col-md-4 col-sm-6 mb-3">
                        <label class="image-uploader <%= hasImage ? "has-image" : ""%>" for="productImage<%= i%>">
                            <div class="image-uploader__content">
                                <i class="fas fa-cloud-upload-alt image-uploader__icon"></i>
                                <p>Click to upload Image <%= i + 1%></p>
                            </div>
                            <img src="<%= hasImage ? request.getContextPath() + "/assets/img/" + existingImageUrl : ""%>" alt="Preview" class="image-preview">
                            <input type="file" id="productImage<%= i%>" name="productImages" accept="image/*">
                            <input type="hidden" name="originalImageViews" value="<%= hasImage ? existingImageUrl : ""%>">
                            <button type="button" class="remove-image-btn">&times;</button>
                        </label>
                    </div>
                    <% }%>
                </div>
            </div>

            <div id="dynamicFieldsContainer">
                <%-- Game Section --%>
                <div class="admin-manage-type game-details" style="display: none;">
                    <div class="mb-4 admin-manage-fieldset">
                        <h3 class="admin-manage-subtitle">Game Details</h3>
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Developer</label>
                                <input type="text" name="developer" class="form-control admin-manage-input" value="<%= (gameDetails != null && gameDetails.getDeveloper() != null) ? gameDetails.getDeveloper() : ""%>">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Genre</label>
                                <input type="text" name="genre" class="form-control admin-manage-input" value="<%= (gameDetails != null && gameDetails.getGenre() != null) ? gameDetails.getGenre() : ""%>">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Release Date</label>
                                <input type="date" name="releaseDate" class="form-control admin-manage-input" value="<%= (gameDetails != null && gameDetails.getReleaseDate() != null) ? gameDetails.getReleaseDate() : ""%>">
                            </div>
                        </div>
                    </div>
                    <div class="mb-4 admin-manage-fieldset">
                        <h3 class="admin-manage-subtitle">Platforms</h3>
                        <div class="row g-3">
                            <div class="col-12">
                                <label class="form-label admin-manage-label">Store Platforms</label>
                                <select multiple class="form-select admin-manage-input" name="platformIds" size="4">
                                    <% if (allPlatforms != null) {
                                            for (StorePlatform platform : allPlatforms) {
                                                boolean isChecked = selectedPlatformIds != null && selectedPlatformIds.contains(platform.getPlatformId());%>
                                    <option value="<%= platform.getPlatformId()%>" <%= isChecked ? "selected" : ""%>><%= platform.getStoreOSName()%></option>
                                    <% }
                                        } %>
                                </select>
                            </div>
                            <div class="col-12">
                                <label class="form-label admin-manage-label">Operating Systems</label>
                                <select multiple class="form-select admin-manage-input" name="osIds" size="4">
                                    <% if (allOS != null) {
                                            for (OperatingSystem os : allOS) {
                                                boolean isChecked = selectedOsIds != null && selectedOsIds.contains(os.getOsId());%>
                                    <option value="<%= os.getOsId()%>" <%= isChecked ? "selected" : ""%>><%= os.getOsName()%></option>
                                    <% }
                                        }%>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="mb-4 admin-manage-fieldset">
                        <h3 class="admin-manage-subtitle">Game Keys</h3>
                        <label class="form-label admin-manage-label">Existing Keys (<%= (gameKeys != null ? gameKeys.size() : 0)%>)</label>
                        <div class="border p-2" style="max-height: 150px; overflow-y: auto;color: #000 ; background-color: #f1f1f1; border-radius: 4px;">
                            <% if (gameKeys != null && !gameKeys.isEmpty()) {
                                    for (GameKey key : gameKeys) {%>
                            <code><%= key.getKeyCode()%></code><br>
                            <% }
                            } else { %>
                            <p class="text-muted small mb-0">No existing keys.</p>
                            <% } %>
                        </div>
                        <div class="mt-3">
                            <label class="form-label admin-manage-label">Add New Keys (one per line)</label>
                            <textarea class="form-control admin-manage-input" name="newGameKeys" rows="4"></textarea>
                        </div>
                    </div>
                </div>

                <%
                    String[] accessoryTypes = {"mouse", "headphone", "keyboard", "controller"};
                    for (String type : accessoryTypes) {
                %>
                <div class="admin-manage-type <%= type%>-details" style="display: none;">
                    <div class="mb-4 admin-manage-fieldset">
                        <h3 class="admin-manage-subtitle">Accessory Details</h3>
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Brand</label>
                                <select class="form-select admin-manage-input" name="brandId">
                                    <option value="">-- Select Brand --</option>
                                    <% if (brands != null) {
                                            for (Brand brand : brands) {
                                                Integer productBrandId = product.getBrandId();
                                                boolean isSelected = productBrandId != null && productBrandId.equals(brand.getBrandId());%>
                                    <option value="<%= brand.getBrandId()%>" <%= isSelected ? "selected" : ""%>><%= brand.getBrandName()%></option>
                                    <% }
                                        }%>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Warranty (Months)</label>
                                <input type="number" class="form-control admin-manage-input" name="warrantyMonths" value="<%= attributeMap.getOrDefault("Warranty", "")%>">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Connection Type</label>
                                <input type="text" class="form-control admin-manage-input" name="connectionType" value="<%= attributeMap.getOrDefault("Connection Type", "")%>">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Weight (grams)</label>
                                <input type="number" class="form-control admin-manage-input" name="weightGrams" value="<%= attributeMap.getOrDefault("Weight", "")%>">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Usage Time (Hours)</label>
                                <input type="text" class="form-control admin-manage-input" name="usageTimeHours" value="<%= attributeMap.getOrDefault("Usage Time", "")%>">
                            </div>
                        </div>
                    </div>

                    <% if (type.equals("mouse")) {%>
                    <div class="mb-4 admin-manage-fieldset">
                        <h3 class="admin-manage-subtitle">Mouse Specifics</h3>
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Mouse Type</label>
                                <input type="text" class="form-control admin-manage-input" name="mouseType" value="<%= attributeMap.getOrDefault("Mouse Type", "")%>">
                            </div>
                        </div>
                    </div>
                    <% } else if (type.equals("headphone")) {%>
                    <div class="mb-4 admin-manage-fieldset">
                        <h3 class="admin-manage-subtitle">Headphone Specifics</h3>
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Headphone Type</label>
                                <input type="text" class="form-control admin-manage-input" name="headphoneType" value="<%= attributeMap.getOrDefault("Headphone Type", "")%>">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Material</label>
                                <input type="text" class="form-control admin-manage-input" name="headphoneMaterial" value="<%= attributeMap.getOrDefault("Material", "")%>">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Battery Capacity</label>
                                <input type="text" class="form-control admin-manage-input" name="headphoneBattery" value="<%= attributeMap.getOrDefault("Battery Capacity", "")%>">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Features</label>
                                <input type="text" class="form-control admin-manage-input" name="headphoneFeatures" value="<%= attributeMap.getOrDefault("Features", "")%>">
                            </div>
                        </div>
                    </div>
                    <% } else if (type.equals("keyboard")) {%>
                    <div class="mb-4 admin-manage-fieldset">
                        <h3 class="admin-manage-subtitle">Keyboard Specifics</h3>
                        <div class="row g-3">
                            <div class="col-md-4">
                                <label class="form-label admin-manage-label">Size</label>
                                <input type="text" class="form-control admin-manage-input" name="keyboardSize" value="<%= attributeMap.getOrDefault("Size", "")%>">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label admin-manage-label">Keyboard Type</label>
                                <input type="text" class="form-control admin-manage-input" name="keyboardType" value="<%= attributeMap.getOrDefault("Keyboard Type", "")%>">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label admin-manage-label">Material</label>
                                <input type="text" class="form-control admin-manage-input" name="keyboardMaterial" value="<%= attributeMap.getOrDefault("Material", "")%>">
                            </div>
                        </div>
                    </div>
                    <% } else if (type.equals("controller")) {%>
                    <div class="mb-4 admin-manage-fieldset">
                        <h3 class="admin-manage-subtitle">Controller Specifics</h3>
                        <div class="row g-3">
                            <div class="col-md-4">
                                <label class="form-label admin-manage-label">Material</label>
                                <input type="text" class="form-control admin-manage-input" name="controllerMaterial" value="<%= attributeMap.getOrDefault("Material", "")%>">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label admin-manage-label">Battery Capacity</label>
                                <input type="text" class="form-control admin-manage-input" name="controllerBattery" value="<%= attributeMap.getOrDefault("Battery Capacity", "")%>">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label admin-manage-label">Charging Time</label>
                                <input type="text" class="form-control admin-manage-input" name="controllerChargingTime" value="<%= attributeMap.getOrDefault("Charging Time", "")%>">
                            </div>
                        </div>
                    </div>
                    <% } %>
                </div>
                <% }%>
            </div>

            <div class="d-flex justify-content-between align-items-center mt-4">
                <a href="<%= request.getContextPath()%>/manage-products?action=list" class="admin-manage-back">
                    <i class="fas fa-arrow-left mr-1"></i> Back
                </a>
                <div>
                    <button type="reset" class="btn admin-manage-reset mr-2">
                        <i class="fas fa-xmark mr-1"></i> Reset
                    </button>
                    <button type="submit" class="btn admin-manage-button">
                        <i class="fas fa-pen-to-square mr-1"></i> Edit
                    </button>
                </div>
            </div>
        </form>
        <% } else { %>
        <div class="alert alert-danger">Product not found or an error occurred.</div>
        <% }%>
    </div>
</main>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        // Gọi hàm xử lý loại sản phẩm khi trang được tải
        const initialSelect = document.getElementById('categoryId');
        if (initialSelect) {
            handleProductTypeChange(initialSelect);
        }

        // Bắt đầu logic upload ảnh đã được thống nhất
        document.querySelectorAll('.image-uploader').forEach(uploader => {
            const input = uploader.querySelector('input[type="file"]');
            const hiddenInput = uploader.querySelector('input[type="hidden"][name="originalImageViews"]');
            const preview = uploader.querySelector('.image-preview');
            const removeBtn = uploader.querySelector('.remove-image-btn');

            // Khi người dùng chọn một file mới
            input.addEventListener('change', function () {
                const file = this.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.onload = function (e) {
                        preview.src = e.target.result;
                        uploader.classList.add('has-image');
                    }
                    reader.readAsDataURL(file);
                }
            });

            // Khi người dùng nhấn nút xóa (×)
            removeBtn.addEventListener('click', function (e) {
                e.preventDefault();
                e.stopPropagation();

                input.value = '';
                preview.src = '';

                if (hiddenInput) {
                    hiddenInput.value = ''; // Xóa giá trị để báo cho server biết ảnh đã bị xóa
                }
                uploader.classList.remove('has-image');
            });
        });
        // Kết thúc logic upload ảnh
    });

    // Hàm xử lý việc hiển thị các trường động dựa trên loại sản phẩm
    function handleProductTypeChange(selectElement) {
        const selectedOption = selectElement.options[selectElement.selectedIndex];
        const productType = selectedOption.getAttribute('data-normalized-name');

        document.getElementById('productTypeField').value = productType;

        document.querySelectorAll('.admin-manage-type').forEach(div => {
            div.style.display = 'none';
            div.querySelectorAll('input, select, textarea').forEach(input => {
                input.disabled = true;
            });
        });

        if (productType) {
            const section = document.querySelector('.' + productType + '-details');
            if (section) {
                section.style.display = 'block';
                section.querySelectorAll('input, select, textarea').forEach(input => {
                    input.disabled = false;
                });
            }
        }
    }
</script>

<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>