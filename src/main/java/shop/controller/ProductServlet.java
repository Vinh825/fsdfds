package shop.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import shop.dao.BrandDAO;
import shop.dao.CategoryDAO;
import shop.dao.GameKeyDAO;
import shop.dao.OperatingSystemDAO;
import shop.dao.ProductDAO;
import shop.dao.StorePlatformDAO;
import shop.model.GameDetails;
import shop.model.OperatingSystem;
import shop.model.Product;
import shop.model.ProductAttribute;
import shop.model.StorePlatform;

/**
 *
 * @author HoangSang
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 15
)
@WebServlet(name = "ProductServlet", urlPatterns = {"/manage-products"})
public class ProductServlet extends HttpServlet {

    private static final String UPLOAD_DIRECTORY = "assets/img";
    private static final int PRODUCTS_PER_PAGE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        try {
            switch (action) {
                case "add": {
                    request.setAttribute("categoriesList", new CategoryDAO().getAllCategories());
                    request.setAttribute("brandsList", new BrandDAO().getAllBrands());
                    request.setAttribute("allPlatforms", new StorePlatformDAO().getAllPlatforms());
                    request.setAttribute("allOS", new OperatingSystemDAO().getAllOperatingSystems());
                    request.getRequestDispatcher("/WEB-INF/dashboard/product-add.jsp").forward(request, response);
                    break;
                }
                case "update": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    ProductDAO productDAO = new ProductDAO();
                    Product existingProduct = productDAO.getProductById(id);

                    if (existingProduct == null) {
                        response.sendRedirect("manage-products?action=list");
                        return;
                    }

                    request.setAttribute("product", existingProduct);
                    request.setAttribute("categoriesList", new CategoryDAO().getAllCategories());
                    request.setAttribute("brandsList", new BrandDAO().getAllBrands());
                    request.setAttribute("allPlatforms", new StorePlatformDAO().getAllPlatforms());
                    request.setAttribute("allOS", new OperatingSystemDAO().getAllOperatingSystems());
                    request.setAttribute("gameDetails", existingProduct.getGameDetails());

                    if (existingProduct.getGameDetailsId() != null && existingProduct.getGameDetailsId() > 0) {
                        int gameDetailsId = existingProduct.getGameDetailsId();
                        request.setAttribute("gameKeys", new GameKeyDAO().findByGameDetailsId(gameDetailsId));

                        List<StorePlatform> selectedPlatforms = new StorePlatformDAO().findByGameDetailsId(gameDetailsId);
                        Set<Integer> selectedPlatformIds = new HashSet<>();
                        for (StorePlatform p : selectedPlatforms) {
                            int masterId = new StorePlatformDAO().getMasterStorePlatformIdByName(p.getStoreOSName());
                            if (masterId != -1) {
                                selectedPlatformIds.add(masterId);
                            }
                        }
                        request.setAttribute("selectedPlatformIds", selectedPlatformIds);

                        List<OperatingSystem> selectedOS = new OperatingSystemDAO().findByGameDetailsId(gameDetailsId);
                        Set<Integer> selectedOsIds = new HashSet<>();
                        for (OperatingSystem os : selectedOS) {
                            int masterId = new OperatingSystemDAO().getMasterOsIdByName(os.getOsName());
                            if (masterId != -1) {
                                selectedOsIds.add(masterId);
                            }
                        }
                        request.setAttribute("selectedOsIds", selectedOsIds);
                    }

                    Map<String, String> attributeMap = new HashMap<>();
                    if (existingProduct.getAttributes() != null) {
                        for (ProductAttribute attr : existingProduct.getAttributes()) {
                            attributeMap.put(attr.getAttributeName(), attr.getValue());
                        }
                    }
                    request.setAttribute("attributeMap", attributeMap);

                    request.getRequestDispatcher("/WEB-INF/dashboard/product-edit.jsp").forward(request, response);
                    break;
                }
                case "details": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    ProductDAO productDAO = new ProductDAO();
                    Product product = productDAO.getProductById(id);

                    // 1. Kiểm tra xem sản phẩm có tồn tại không
                    if (product == null) {
                        response.sendRedirect(request.getContextPath() + "/manage-products?action=list");
                        return; // Dừng xử lý ngay lập tức
                    }

                    // 2. Nếu sản phẩm tồn tại, lấy số sao và gán vào sản phẩm (không cần vòng lặp)
                    double stars = productDAO.getAverageStarsForProduct(product.getProductId());
                    product.setAverageStars(stars);

                    // 3. Xử lý các logic còn lại
                    if ("Game".equalsIgnoreCase(product.getCategoryName()) && product.getGameDetailsId() != null && product.getGameDetailsId() > 0) {
                        int gameDetailsId = product.getGameDetailsId();
                        request.setAttribute("gameKeys", new GameKeyDAO().findByGameDetailsId(gameDetailsId));

                        StorePlatformDAO platformDAO = new StorePlatformDAO();
                        List<StorePlatform> rawPlatforms = platformDAO.findByGameDetailsId(gameDetailsId);
                        Set<String> seenPlatformNames = new HashSet<>();
                        List<StorePlatform> distinctPlatforms = new ArrayList<>();
                        for (StorePlatform p : rawPlatforms) {
                            if (seenPlatformNames.add(p.getStoreOSName())) {
                                distinctPlatforms.add(p);
                            }
                        }
                        request.setAttribute("platforms", distinctPlatforms);

                        OperatingSystemDAO osDAO = new OperatingSystemDAO();
                        List<OperatingSystem> rawOs = osDAO.findByGameDetailsId(gameDetailsId);
                        Set<String> seenOsNames = new HashSet<>();
                        List<OperatingSystem> distinctOs = new ArrayList<>();
                        for (OperatingSystem os : rawOs) {
                            if (seenOsNames.add(os.getOsName())) {
                                distinctOs.add(os);
                            }
                        }
                        request.setAttribute("operatingSystems", distinctOs);
                    }

                    Locale localeVN = new Locale("vi", "VN");
                    request.setAttribute("currencyFormatter", NumberFormat.getCurrencyInstance(localeVN));
                    request.setAttribute("timestampFormatter", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
                    request.setAttribute("dateFormatter", new SimpleDateFormat("dd/MM/yyyy"));

                    // 4. Gửi dữ liệu tới JSP
                    request.setAttribute("product", product);
                    request.getRequestDispatcher("/WEB-INF/dashboard/product-details.jsp").forward(request, response);
                    break;
                }
                case "delete": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    ProductDAO productDAO = new ProductDAO();
                    Product productToDelete = productDAO.getProductById(id);
                    request.setAttribute("delete", productToDelete);
                    if (productToDelete != null) {
                        boolean isSold = productDAO.isProductSold(id);
                        request.setAttribute("isSold", isSold);
                    }
                    request.getRequestDispatcher("/WEB-INF/dashboard/product-delete.jsp").forward(request, response);
                    break;
                }
                default: {
                    ProductDAO productDAO = new ProductDAO();
                    List<Product> fullProductList;
                    String searchQuery = request.getParameter("query");

                    if ("search".equals(action) && searchQuery != null && !searchQuery.trim().isEmpty()) {
                        String lowerCaseQuery = searchQuery.toLowerCase();
                        List<Product> allProducts = productDAO.getAllProducts();
                        fullProductList = new ArrayList<>();
                        for (Product p : allProducts) {
                            if (p.getName() != null && p.getName().toLowerCase().contains(lowerCaseQuery)) {
                                fullProductList.add(p);
                            }
                        }
                    } else {
                        fullProductList = productDAO.getAllProducts();
                    }

                    int page = 1;
                    try {
                        page = Integer.parseInt(request.getParameter("page"));
                    } catch (Exception e) {
                        page = 1;
                    }

                    int totalProducts = fullProductList.size();
                    int totalPages = (int) Math.ceil((double) totalProducts / PRODUCTS_PER_PAGE);
                    if (totalPages == 0) {
                        totalPages = 1;
                    }
                    if (page > totalPages) {
                        page = totalPages;
                    }

                    int start = (page - 1) * PRODUCTS_PER_PAGE;
                    int end = Math.min(start + PRODUCTS_PER_PAGE, totalProducts);
                    List<Product> pagedProducts = fullProductList.subList(start, end);

                    String pageUrl;
                    if ("search".equals(action) && searchQuery != null && !searchQuery.isEmpty()) {
                        pageUrl = "manage-products?action=search&query=" + URLEncoder.encode(searchQuery, StandardCharsets.UTF_8.toString()) + "&page=";
                    } else {
                        pageUrl = "manage-products?action=list&page=";
                    }
                    String previousPageUrl = (page > 1) ? pageUrl + (page - 1) : "#";
                    String nextPageUrl = (page < totalPages) ? pageUrl + (page + 1) : "#";

                    request.setAttribute("productList", pagedProducts);
                    request.setAttribute("totalPages", totalPages);
                    request.setAttribute("currentPage", page);
                    request.setAttribute("startRowNumber", start + 1);
                    request.setAttribute("pageUrl", pageUrl);
                    request.setAttribute("previousPageUrl", previousPageUrl);
                    request.setAttribute("nextPageUrl", nextPageUrl);

                    request.getRequestDispatcher("/WEB-INF/dashboard/product-list.jsp").forward(request, response);
                    break;
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Database error in doGet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/manage-products?action=list");
            return;
        }

        try {
            switch (action) {
                case "add": {
                    String productType = request.getParameter("productType");
                    String name = request.getParameter("name");
                    BigDecimal price = new BigDecimal(request.getParameter("price"));
                    String description = request.getParameter("description");
                    int quantity = Integer.parseInt(request.getParameter("quantity"));
                    int categoryId = Integer.parseInt(request.getParameter("categoryId"));

                    List<String> imageUrls = new ArrayList<>();
                    String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }
                    for (Part filePart : request.getParts()) {
                        if ("productImages".equals(filePart.getName()) && filePart.getSize() > 0) {
                            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                            if (fileName != null && !fileName.isEmpty()) {
                                filePart.write(uploadPath + File.separator + fileName);
                                imageUrls.add(fileName);
                            }
                        }
                    }

                    Product product = new Product();
                    product.setName(name);
                    product.setDescription(description);
                    product.setPrice(price);
                    product.setQuantity(quantity);
                    product.setCategoryId(categoryId);

                    ProductDAO productDAO = new ProductDAO();

                    if ("game".equals(productType)) {
                        GameDetails gameDetails = new GameDetails();
                        gameDetails.setDeveloper(request.getParameter("developer"));
                        gameDetails.setGenre(request.getParameter("genre"));
                        gameDetails.setReleaseDate(Date.valueOf(request.getParameter("releaseDate")));

                        String[] platformIds = request.getParameterValues("platformIds");
                        String[] osIds = request.getParameterValues("osIds");
                        String newKeysRaw = request.getParameter("gameKeys");
                        String[] newKeys = (newKeysRaw != null && !newKeysRaw.trim().isEmpty()) ? newKeysRaw.split("\\r?\\n") : new String[0];

                        productDAO.addFullGameProduct(product, gameDetails, imageUrls, platformIds, osIds, newKeys);
                    } else {
                        String brandIdStr = request.getParameter("brandId");
                        product.setBrandId((brandIdStr != null && !brandIdStr.isEmpty()) ? Integer.parseInt(brandIdStr) : null);

                        List<ProductAttribute> attributes = new ArrayList<>();
                        String[] attrNames = {"Warranty", "Weight", "Connection Type", "Usage Time", "Headphone Type", "Material", "Battery Capacity", "Features", "Size", "Keyboard Type", "Mouse Type", "Charging Time"};
                        String[] paramNames = {"warrantyMonths", "weightGrams", "connectionType", "usageTimeHours", "headphoneType", "headphoneMaterial", "headphoneBattery", "headphoneFeatures", "keyboardSize", "keyboardType", "mouseType", "controllerChargingTime"};

                        for (int i = 0; i < attrNames.length; i++) {
                            String value = request.getParameter(paramNames[i]);
                            if (value != null && !value.trim().isEmpty()) {
                                ProductAttribute pa = new ProductAttribute();
                                pa.setAttributeName(attrNames[i]);
                                pa.setValue(value);
                                attributes.add(pa);
                            }
                        }

                        productDAO.addAccessoryProduct(product, attributes, imageUrls);
                    }
                    response.sendRedirect(request.getContextPath() + "/manage-products?action=list");
                    break;
                }
                case "update": {
                    int productId = Integer.parseInt(request.getParameter("productId"));
                    String name = request.getParameter("name");
                    BigDecimal price = new BigDecimal(request.getParameter("price"));
                    String description = request.getParameter("description");
                    int quantity = Integer.parseInt(request.getParameter("quantity"));
                    int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                    String productType = request.getParameter("productType");

                    List<String> finalImageUrls = new ArrayList<>();
                    String[] originalImageViews = request.getParameterValues("originalImageViews");
                    if (originalImageViews != null) {
                        for (String imageUrl : originalImageViews) {
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                finalImageUrls.add(imageUrl);
                            }
                        }
                    }
                    String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }
                    for (Part filePart : request.getParts()) {
                        if ("productImages".equals(filePart.getName()) && filePart.getSize() > 0) {
                            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                            if (fileName != null && !fileName.isEmpty()) {
                                filePart.write(uploadPath + File.separator + fileName);
                                finalImageUrls.add(fileName);
                            }
                        }
                    }

                    Product product = new Product();
                    product.setProductId(productId);
                    product.setName(name);
                    product.setDescription(description);
                    product.setPrice(price);
                    product.setQuantity(quantity);
                    product.setCategoryId(categoryId);

                    GameDetails gameDetails = null;
                    List<ProductAttribute> attributes = null;
                    String[] platformIds = null;
                    String[] osIds = null;
                    String[] newKeys = null;

                    if ("game".equalsIgnoreCase(productType)) {
                        product.setBrandId(null);
                        int gameDetailsId = 0;
                        String gameDetailsIdStr = request.getParameter("gameDetailsId");
                        if (gameDetailsIdStr != null && !gameDetailsIdStr.isEmpty()) {
                            gameDetailsId = Integer.parseInt(gameDetailsIdStr);
                        }
                        if (gameDetailsId > 0) {
                            product.setGameDetailsId(gameDetailsId);
                        }

                        gameDetails = new GameDetails();
                        gameDetails.setGameDetailsId(gameDetailsId);
                        gameDetails.setDeveloper(request.getParameter("developer"));
                        gameDetails.setGenre(request.getParameter("genre"));
                        String releaseDateStr = request.getParameter("releaseDate");
                        if (releaseDateStr != null && !releaseDateStr.isEmpty()) {
                            gameDetails.setReleaseDate(Date.valueOf(releaseDateStr));
                        }
                        platformIds = request.getParameterValues("platformIds");
                        osIds = request.getParameterValues("osIds");
                        String newKeysRaw = request.getParameter("newGameKeys");
                        if (newKeysRaw != null && !newKeysRaw.trim().isEmpty()) {
                            newKeys = newKeysRaw.split("\\r?\\n");
                        }
                    } else {
                        product.setGameDetailsId(null);
                        String brandIdStr = request.getParameter("brandId");
                        product.setBrandId((brandIdStr != null && !brandIdStr.isEmpty()) ? Integer.parseInt(brandIdStr) : null);

                        attributes = new ArrayList<>();
                        String[] attrNames = {"Warranty", "Weight", "Connection Type", "Usage Time", "Headphone Type", "Material", "Battery Capacity", "Features", "Size", "Keyboard Type", "Mouse Type", "Charging Time"};
                        String[] paramNames = {"warrantyMonths", "weightGrams", "connectionType", "usageTimeHours", "headphoneType", "headphoneMaterial", "headphoneBattery", "headphoneFeatures", "keyboardSize", "keyboardType", "mouseType", "controllerChargingTime"};

                        for (int i = 0; i < attrNames.length; i++) {
                            String value = request.getParameter(paramNames[i]);
                            if (value != null && !value.trim().isEmpty()) {
                                ProductAttribute pa = new ProductAttribute();
                                pa.setAttributeName(attrNames[i]);
                                pa.setValue(value);
                                attributes.add(pa);
                            }
                        }
                    }

                    new ProductDAO().updateProduct(product, gameDetails, attributes, finalImageUrls, platformIds, osIds, newKeys);
                    response.sendRedirect(request.getContextPath() + "/manage-products?action=list");
                    break;
                }
                case "delete": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    ProductDAO productDAO = new ProductDAO();
                    productDAO.deleteProduct(id);
                    response.sendRedirect(request.getContextPath() + "/manage-products?action=list");
                    break;
                }
                case "updateVisibility": {
                    int productId = Integer.parseInt(request.getParameter("id"));
                    int newStatus = Integer.parseInt(request.getParameter("newStatus"));
                    String page = request.getParameter("page");

                    ProductDAO productDAO = new ProductDAO();
                    productDAO.updateProductVisibility(productId, newStatus);

                    String redirectUrl = request.getContextPath() + "/manage-products?action=list";
                    if (page != null && !page.isEmpty()) {
                        redirectUrl += "&page=" + page;
                    }

                    response.sendRedirect(redirectUrl);
                    break;
                }
                default:
                    response.sendRedirect(request.getContextPath() + "/manage-products?action=list");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "err: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/dashboard/product-list.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles all product management actions.";
    }
}
