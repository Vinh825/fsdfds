<%@page import="shop.model.Customer"%>
<%@page import="shop.model.Checkout"%>
<%@page import="java.util.ArrayList"%>
<%@page import="shop.model.Product"%>
<%@include file="../include/home-header.jsp" %>
<!-- page title -->
<!-- page title -->
<section class="section section--first section--last section--head" data-bg="img/bg3.png">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <div class="section__wrap">
                    <!-- section title -->
                    <h2 class="section__title">Checkout</h2>
                    <!-- end section title -->

                    <!-- breadcrumb -->
                    <ul class="breadcrumb">
                        <li class="breadcrumb__item"><a href="index.html">Home</a></li>
                        <li class="breadcrumb__item breadcrumb__item--active">Checkout</li>
                    </ul>
                    <!-- end breadcrumb -->
                </div>
            </div>
        </div>
    </div>
</section>
<!-- end page title -->
<%    double total = (double) session.getAttribute("totalAmount");

    ArrayList<Checkout> product = (ArrayList<Checkout>) session.getAttribute("checkout");
    Customer customer = (Customer) session.getAttribute("currentCustomer");
    int i = 0;
    for (Checkout p : product) {
        if (p.getProductCategory().equalsIgnoreCase("game")) {
            i = 1;
        }
    }
%>
<div class="section">
    <div class="container">
        <div class="row">
            <div class="col-12 col-lg-8">
                <!-- cart -->
                <div class="cart">
                    <div class="table-responsive">
                        <table class="cart__table">
                            <thead>
                                <tr>
                                    <th><a href="#">Product </a></th>
                                    <th><a href="#" class="active">Title</a></th>
                                    <th><a href="#">Category </a></th>
                                    <th><a href="#">Price </a></th>
                                    <th>Quantity</th>
                                </tr>
                            </thead>

                            <tbody>
                                <% for (Checkout pro : product) {%>
                                <tr>
                                    <td>
                                        <div class="cart__img">
                                            <img src="<%= request.getContextPath()%>/assets/img/<%= pro.getImageURL()%>" alt="">
                                        </div>
                                    </td>
                                    <td><a href="#"> <%= pro.getProductName()%> </a></td>
                                    <td><%= pro.getProductCategory()%></td>
                                    <% if (pro.getSale_price() == 0.0) {%>
                                    <td><span class="cart__price"><%= pro.getPrice()%></span></td>
                                        <%} else {%>
                                    <td><span class="cart__price"><%= pro.getSale_price()%></span></td>
                                        <%}
                                        %>

                                    <td><span class="cart__price"><%= pro.getQuantity()%></span></td>

                                </tr>
                                <%}%>
                            </tbody>
                        </table>
                    </div>
                    <div class="cart__info">
                        <div class="cart__total">
                            <p>Total:</p>
                            <span><%= total%></span>
                        </div>

                        <div class="cart__systems">
                            <i class="pf pf-visa"></i>
                            <i class="pf pf-mastercard"></i>
                            <i class="pf pf-paypal"></i>
                        </div>
                    </div>
                </div>
                <!-- end cart -->
            </div>

            <div class="col-12 col-lg-4">
                <!-- checkout -->
                <%
                    Boolean voucherApplied = (Boolean) request.getAttribute("voucherApplied");
                    if (voucherApplied == null || !voucherApplied) {
                %>
                <form action="ApplyVoucherServlet" method="post" class="form form--first form--coupon">
                    <input type="text" name="voucher" class="form__input" placeholder="Coupon code">
                    <button type="submit" class="form__btn">Apply</button>
                </form>
                <% } else { %>
                <p style="color: lightgreen;">Voucher applied successfully!</p>
                <% }%>

                <!-- end checkout -->

                <!-- checkout -->
                <form action="checkout" method="post" class="form">
                    <input type="text" name="customerName" value="<%= customer.getFullName()%>"  readonly class="form__input" style="color: white; background-color: transparent; border: none;" />
                    <input type="text" name="customerAddress" value="<%= customer.getAddress()%>" required="" class="form__input" style="color: white; background-color: transparent; border: none;" />
                    <input type="text" name="customerEmail" value="<%= customer.getEmail()%>" readonly class="form__input" style="color: white; background-color: transparent; border: none;" />
                    <input type="text" name="customerPhone" value="<%= customer.getPhone()%>" readonly class="form__input" style="color: white; background-color: transparent; border: none;" />


                    <% for (Checkout pro : product) {%>
                    <input type="hidden" name="productId" value="<%= pro.getProductId()%>" />
                    <input type="hidden" name="quantity" value="<%= pro.getQuantity()%>" />
                    <input type="hidden" name="price" value="<%= (pro.getSale_price() == 0.0) ? pro.getPrice() : pro.getSale_price()%>" />
                    <% }%>
                    <input type="hidden" name="total" value="<%= total%>" />
                    <input type="hidden" name="customerId" value="<%= customer.getCustomerId()%>" />
                    <input type="hidden" name="vouchercode" value="<%= request.getAttribute("voucherCode")%>" />
                    <input type="hidden" name="action" value="order" />

                    <% if (i == 1) { %>
                    <select name="paymentMethod" class="form__select">
                        <option value="visa">Visa</option>
                        <option value="mastercard">Mastercard</option>
                        <option value="paypal">Paypal</option>
                    </select>
                    <% } else { %>
                    <select name="paymentMethod" class="form__select">
                        <option value="visa">Visa</option>
                        <option value="mastercard">Mastercard</option>
                        <option value="paypal">Paypal</option>
                        <option value="Cash On Delivery">Cash On Delivery</option>
                    </select>
                    <% }%>

                    <span class="form__text">There are many variations of passages of Lorem Ipsum...</span>

                    <button type="submit" class="form__btn" >Proceed to checkout</button>
                </form>

                <!-- end checkout -->
            </div>
        </div>
    </div>
</div>

<!-- end section -->
<!-- end section -->

<%@include file="../include/home-footer.jsp" %>