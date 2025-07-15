<%-- 
    Document   : dashboard 
    Created on : Jun 20, 2025, 5:55:00 PM
    Author : HoangSang
--%>

<%@page import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, shop.model.Product, java.text.NumberFormat, java.util.Locale" %>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>


<%
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

    Map<String, Object> summaryStats = (Map<String, Object>) request.getAttribute("summaryStats");
    double totalRevenue = 0;
    int productsSold = 0;
    if (summaryStats != null) {
        totalRevenue = (double) summaryStats.get("totalRevenue");
        productsSold = (int) summaryStats.get("productsSold");
    }

    int totalStock = (Integer) request.getAttribute("totalStock");
    int totalCustomers = (Integer) request.getAttribute("totalCustomers");
    String currentPeriod = (String) request.getAttribute("currentPeriod");
    List<Product> topSellingProducts = (List<Product>) request.getAttribute("topSellingProducts");

    String trendLabelsJson = (String) request.getAttribute("trendLabelsJson");
    String trendDataJson = (String) request.getAttribute("trendDataJson");
    String stockLabelsJson = (String) request.getAttribute("stockLabelsJson");
    String stockDataJson = (String) request.getAttribute("stockDataJson");
%>

<style>
    body {
        width: 84%;
        margin-left: 15%;
        background-color: #161922;
        margin-top: 6%;

    }
    .dashboard-card {
        background: #1f2334;
        border: 1px solid #2b3149;
        border-radius: 12px;
        padding: 25px;
        color: #fff;
        height: 100%;
    }
    .dashboard-card-title {
        font-size: 0.9rem;
        font-weight: 500;
        color: #828ac4;
        text-transform: uppercase;
        margin-bottom: 8px;
    }
    .dashboard-card-value {
        font-size: 2.2rem;
        font-weight: 700;
        color: #fff;
    }
    .dashboard-card-icon {
        font-size: 1.8rem;
        color: #434c7a;
    }
    .dashboard-card-footer {
        font-size: 0.9rem;
        margin-top: 15px;
    }
    .text-success {
        color: #28a745 !important;
    }
    .text-danger {
        color: #dc3545 !important;
    }

    .chart-wrapper {
        background: #1f2334;
        border: 1px solid #2b3149;
        border-radius: 12px;
        padding: 25px;
    }
    .chart-title {
        color: #fff;
        font-weight: 600;
    }
    .filter-btn-group .btn {
        background-color: #2b3149;
        border: 1px solid #434c7a;
        color: #abb9e8;
        font-size: 0.85rem;
        font-weight: 500;
    }
    .filter-btn-group .btn.active {
        background-color: #6a5af9;
        color: #fff;
        border-color: #6a5af9;
    }

    .table-dark-custom {
        background-color: transparent;
        color: #abb9e8;
    }
    .table-dark-custom th {
        background-color: #2b3149;
        border-color: #3e4565;
        font-weight: 600;
    }
    .table-dark-custom td, .table-dark-custom th {
        border-color: #2b3149;
        vertical-align: middle;
    }
    .table-dark-custom img {
        width: 40px;
        height: 40px;
        object-fit: cover;
        border-radius: 8px;
        margin-right: 15px;
    }

</style>
<div class="container-fluid mt-4">
    <div class="row g-4">
        <div class="col-lg-3 col-md-6">
            <div class="dashboard-card">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <span class="dashboard-card-title">Revenue (<%= currentPeriod%>)</span>
                        <h3 class="dashboard-card-value"><%= currencyFormatter.format(totalRevenue)%></h3>
                    </div>
                    <div class="dashboard-card-icon"><i class="fas fa-dollar-sign"></i></div>
                </div>
            </div>
        </div>
        <div class="col-lg-3 col-md-6">
            <div class="dashboard-card">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <span class="dashboard-card-title">Products Sold (<%= currentPeriod%>)</span>
                        <h3 class="dashboard-card-value"><%= productsSold%></h3>
                    </div>
                    <div class="dashboard-card-icon"><i class="fas fa-shopping-cart"></i></div>
                </div>
            </div>
        </div>
        <div class="col-lg-3 col-md-6">
            <div class="dashboard-card">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <span class="dashboard-card-title">Products In Stock</span>
                        <h3 class="dashboard-card-value"><%= totalStock%></h3>
                    </div>
                    <div class="dashboard-card-icon"><i class="fas fa-boxes"></i></div>
                </div>
            </div>
        </div>
        <div class="col-lg-3 col-md-6">
            <div class="dashboard-card">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <span class="dashboard-card-title">Total Customers</span>
                        <h3 class="dashboard-card-value"><%= totalCustomers%></h3>
                    </div>
                    <div class="dashboard-card-icon"><i class="fas fa-users"></i></div>
                </div>
            </div>
        </div>
    </div>

    <div class="row g-4 mt-3">
        <div class="col-lg-8">
            <div class="chart-wrapper h-100">
                <div class="d-flex justify-content-between align-items-center mb-3 flex-wrap">
                    <h5 class="chart-title mb-2 mb-md-0">Revenue Trend</h5>
                    <div class="btn-group filter-btn-group" role="group">
                        <a href="dashboard?period=today" class="btn btn-sm <%= "today".equals(currentPeriod) ? "active" : ""%>">Today</a>
                        <a href="dashboard?period=week" class="btn btn-sm <%= "week".equals(currentPeriod) ? "active" : ""%>">This Week</a>
                        <a href="dashboard?period=month" class="btn btn-sm <%= "month".equals(currentPeriod) ? "active" : ""%>">This Month</a>
                        <a href="dashboard?period=year" class="btn btn-sm <%= "year".equals(currentPeriod) ? "active" : ""%>">This Year</a>
                    </div>
                </div>
                <div style="height: 350px;">
                    <canvas id="revenueChart"></canvas>
                </div>
            </div>
        </div>
        <div class="col-lg-4">
            <div class="chart-wrapper h-100">
                <h5 class="chart-title mb-3">Stock by Category</h5>
                <div style="height: 350px; max-width: 300px; margin: auto;">
                    <canvas id="stockByCategoryChart"></canvas>
                </div>
            </div>
        </div>
    </div>

    <div class="row g-4 mt-3">
        <div class="col-12">
            <div class="chart-wrapper">
                <h5 class="chart-title mb-3">Top 5 Selling Products</h5>
                <div class="table-responsive">
                    <table class="table table-dark-custom">
                        <thead><tr><th>Product</th><th class="text-end">Sold</th></tr></thead>
                        <tbody>
                            <% if (topSellingProducts != null && !topSellingProducts.isEmpty()) {
                                    for (Product p : topSellingProducts) {%>
                            <tr>
                                <td>
                                    <div class="d-flex align-items-center">
                                        <img src="<%= request.getContextPath()%>/assets/img/<%= (p.getImageUrls() != null && !p.getImageUrls().isEmpty() ? p.getImageUrls().get(0) : "default-product.png")%>" alt="">
                                        <span class="ms-3"><%= p.getName()%></span>
                                    </div>
                                </td>
                                <td class="text-end fw-bold"><%= p.getQuantity()%></td>
                            </tr>
                            <% }
                            } else { %>
                            <tr><td colspan="2" class="text-center text-muted">No sales data available.</td></tr>
                            <% }%>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2.0.0"></script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        Chart.register(ChartDataLabels);
        Chart.defaults.color = '#8e95a5';
        Chart.defaults.font.family = 'Poppins, sans-serif';

        const trendLabels = JSON.parse('<%= trendLabelsJson%>');
        const trendData = JSON.parse('<%= trendDataJson%>');
        const stockLabels = JSON.parse('<%= stockLabelsJson%>');
        const stockData = JSON.parse('<%= stockDataJson%>');

        const revenueCtx = document.getElementById('revenueChart');
        if (revenueCtx) {
            new Chart(revenueCtx.getContext('2d'), {
                type: 'bar',
                data: {
                    labels: trendLabels,
                    datasets: [{
                            label: 'Revenue',
                            data: trendData,
                            backgroundColor: 'rgba(106, 90, 249, 0.7)',
                            borderColor: 'rgba(106, 90, 249, 1)',
                            borderWidth: 1,
                            borderRadius: 4
                        }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {y: {beginAtZero: true, grid: {color: '#2b3149'}, ticks: {color: '#828ac4'}}, x: {grid: {display: false}, ticks: {color: '#828ac4'}}},
                    plugins: {legend: {display: false}, datalabels: {display: false}}
                }
            });
        }

        const stockCtx = document.getElementById('stockByCategoryChart');
        if (stockCtx && stockData.length > 0) {
            new Chart(stockCtx.getContext('2d'), {
                type: 'doughnut',
                data: {
                    labels: stockLabels,
                    datasets: [{
                            data: stockData,
                            backgroundColor: ['#6a5af9', '#00ff75', '#ffc107', '#33a1ff', '#ff6b6b', '#20c997'],
                            borderColor: '#1f2334',
                            borderWidth: 3
                        }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {position: 'bottom', labels: {color: '#abb9e8'}},
                        datalabels: {
                            color: '#fff',
                            formatter: (value, ctx) => {
                                let sum = 0;
                                let dataArr = ctx.chart.data.datasets[0].data;
                                dataArr.map(data => sum += data);
                                let percentage = (value * 100 / sum).toFixed(1) + "%";
                                return percentage;
                            }
                        }
                    }
                }
            });
        }
    });
</script>

<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>