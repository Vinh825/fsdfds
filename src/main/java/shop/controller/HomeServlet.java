package shop.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shop.dao.ProductDAO;
import shop.model.Product;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        ProductDAO productDAO = new ProductDAO();

        try {
            switch (action) {
                case "details": {
                    try {
                        int productId = Integer.parseInt(request.getParameter("productId"));
                        Product product = productDAO.getProductById(productId);

                        if (product != null) {
                            double averageStars = productDAO.getAverageStarsForProduct(productId);
                            product.setAverageStars(averageStars);
                        }

                        request.setAttribute("product", product);
                        request.getRequestDispatcher("/WEB-INF/home/product-details.jsp").forward(request, response);
                    } catch (NumberFormatException e) {
                        response.sendRedirect("home");
                    }
                    break;
                }
                case "search": {
                    try {
                        ArrayList<Product> productlist;
                        String keyword = request.getParameter("keyword");
                        if (keyword != null && !keyword.trim().isEmpty()) {
                            productlist = productDAO.searchProductByName(keyword);
                            request.setAttribute("productList", productlist);
                            System.out.print(productlist);
                        } else {
                            productlist = (ArrayList<Product>) productDAO.getAllProducts();
                            request.setAttribute("productList", productlist);
                        }

                        request.setAttribute("keyword", keyword);
                        request.getRequestDispatcher("/WEB-INF/home/search.jsp").forward(request, response);

                    } catch (Exception ex) {
                        Logger.getLogger(VoucherServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                case "filter": {
                    try {
                        ArrayList<Product> productlist;
                        String keyword = request.getParameter("keyword");
                        if (keyword != null && !keyword.trim().isEmpty()) {
                            productlist = productDAO.getProductsByCategory(keyword);
                            request.setAttribute("productList", productlist);
                            System.out.print(productlist);
                        }
                        request.getRequestDispatcher("/WEB-INF/home/search.jsp").forward(request, response);

                    } catch (Exception ex) {
                        Logger.getLogger(VoucherServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                default: {
                    List<Product> accessoryList = productDAO.getAccessoryProducts();

                    for (Product product : accessoryList) {
                        double stars = productDAO.getAverageStarsForProduct(product.getProductId());
                        product.setAverageStars(stars);
                    }

                    List<Product> gameList = productDAO.getGameProducts();

                    for (Product product : gameList) {
                        double stars = productDAO.getAverageStarsForProduct(product.getProductId());
                        product.setAverageStars(stars);
                    }

                    request.setAttribute("gameList", gameList);
                    request.setAttribute("accessoryList", accessoryList);

                    request.getRequestDispatcher("/WEB-INF/home/home.jsp")
                            .forward(request, response);
                    break;
                }
            }
        } catch (Exception e) {
            throw new ServletException("Error in HomeServlet: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Home Servlet that handles routing for homepage and product details.";
    }
}
