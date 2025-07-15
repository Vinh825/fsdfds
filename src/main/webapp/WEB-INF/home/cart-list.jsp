<%@page import="shop.model.CartItem"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Locale"%>

<%@include file="../include/home-header.jsp" %>

<!DOCTYPE html>
<html lang="en">



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
                            <li class="breadcrumb__item"><a href="index.jsp">Home</a></li>
                            <li class="breadcrumb__item breadcrumb__item--active">Checkout</li>
                        </ul>
                        <!-- end breadcrumb -->
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- end page title -->

    <!-- section -->
    <div class="section">
        <div class="container">
            <div class="row">
                <div class="col-12 col-lg-8">
                    <!-- cart -->
                    <div class="cart">
                        <div style="padding: 20px;">
                            <div class="table-responsive">
                                <%                                    List<CartItem> cartItems = (List<CartItem>) request.getAttribute("cartItems");

                                    if (cartItems == null || cartItems.isEmpty()) {
                                %>
                                <!-- Gi? hàng r?ng -->
                                <h2 style="color: white">Your cart is empty.</h2>
                                <%
                                } else {
                                %>

                                <div class="table-responsive">
                                    <table class="cart__table">
                                        <thead>
                                            <tr>
                                                <th>Check</th>
                                                <th>Image</th>
                                                <th>Title</th>
                                                <th>Category</th>
                                                <th>Price</th>
                                                <th>Quantity</th>
                                                <th>Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <%
                                                for (CartItem item : cartItems) {
                                                    double unitPrice = item.getSalePrice() != null ? item.getSalePrice() : item.getPrice();
                                                    double totalPrice = unitPrice * item.getQuantity();
                                            %>
                                            <tr>
                                                <!-- Checkbox -->
                                                <td>
                                                    <input type="checkbox"
                                                           class="product-check"
                                                           data-price="<%= String.format(Locale.ENGLISH, "%.2f", (item.getSalePrice() != null ? item.getSalePrice() : item.getPrice()) * item.getQuantity())%>"
                                                           data-product-id="<%= item.getProductId()%>"
                                                           data-quantity="<%= item.getQuantity()%>"
                                                           onclick="updateTotalPrice()"
                                                           style="transform: scale(1.5); appearance: auto; margin-right: 10px;" />
                                                </td>

                                                <!-- Image -->
                                                <td>
                                                    <div class="cart__img">
                                                        <img src="<%= request.getContextPath()%>/assets/img/<%= item.getImageUrl()%>" alt="">
                                                    </div>
                                                </td>

                                                <!-- Product Name -->
                                                <td><a href="#"> <%= item.getProductName()%> </a></td>

                                                <!-- Category -->
                                                <td><%= item.getCategoryName()%></td>

                                                <!-- Price * Quantity -->
                                                <td>
                                                    <span class="cart__price">
                                                        $<%= String.format("%.2f", totalPrice)%>
                                                    </span>
                                                </td>

                                                <!-- Quantity -->
                                                <td>
                                                    <div class="quantity">
                                                        <!-- Decrease -->
                                                        <form method="post" action="cart" style="display:inline;">
                                                            <input type="hidden" name="action" value="decrease">
                                                            <input type="hidden" name="page" value="cart">
                                                            <input type="hidden" name="productId" value="<%= item.getProductId()%>">
                                                            <input type="hidden" name="quantity" value="1">
                                                            <button type="submit" style="color: white;">-</button>
                                                        </form>
                                                        <span class="cart__price"><%= item.getQuantity()%></span>
                                                        <!-- Increase -->
                                                        <form method="post" action="cart" style="display:inline;">
                                                            <input type="hidden" name="page" value="cart">
                                                            <input type="hidden" name="action" value="add">
                                                            <input type="hidden" name="productId" value="<%= item.getProductId()%>">
                                                            <input type="hidden" name="quantity" value="1">
                                                            <button type="submit" style="color: white;">+</button>
                                                        </form>
                                                    </div>
                                                </td>

                                                <!-- Delete -->
                                                <td>
                                                    <form method="post" action="cart">
                                                        <input type="hidden" name="action" value="delete">
                                                        <input type="hidden" name="productId" value="<%= item.getProductId()%>">
                                                        <button type="submit" class="btn-action btn-delete">Delete</button>
                                                    </form>
                                                </td>
                                            </tr>
                                            <% } %>
                                        </tbody>
                                    </table>
                                </div>

                                <!-- Cart Info -->
                                <div class="cart__info">
                                    <div class="cart__total">
                                        <p>Total:</p>
                                        <span id="totalAmount">
                                            <span id="dollarSign">$</span><span id="totalNumber">0.00</span>
                                        </span>
                                    </div>

                                    <div class="cart__systems">
                                        <i class="pf pf-visa"></i>
                                        <i class="pf pf-mastercard"></i>
                                        <i class="pf pf-paypal"></i>
                                    </div>
                                    <form id="checkoutForm" action="checkout" method="post">

                                        <input type="hidden" name="totalAmount" id="hiddenTotalAmount" value="">
                                        <div class="checkout_btn">
                                            <button type="button" class="form__btn" id="checkoutBtn" onclick="prepareAndSubmitForm()" disabled>
                                                Proceed to checkout
                                            </button>
                                        </div>
                                    </form>
                                </div>
                                <%
                                    }
                                %>



                                <!-- end cart -->
                            </div>
                        </div>
                    </div>
                </div>
            </div>


            <script>
                function updateTotalPrice() {
                    let total = 0;
                    const checkboxes = document.querySelectorAll('.product-check:checked');

                    checkboxes.forEach(cb => {
                        const rawPrice = cb.getAttribute('data-price');
                        console.log("? Checkbox selected - data-price:", rawPrice); // <-- DEBUG

                        const price = parseFloat(rawPrice);
                        if (!isNaN(price)) {
                            total += price;
                        } else {
                            console.warn("? Invalid price encountered:", rawPrice); // <-- DEBUG
                        }
                    });



                    const totalSpan = document.getElementById("totalNumber").textContent = total.toFixed(2);
                    totalSpan.textContent = `$${total.toFixed(2)}`;
                    console.log("? Total updated:", total.toFixed(2)); // <-- DEBUG
                }


            </script>
            <script>
                document.querySelectorAll('.product-check').forEach(cb => {
                    cb.addEventListener("change", function () {
                        const anyChecked = document.querySelectorAll('.product-check:checked').length > 0;
                        document.getElementById("checkoutBtn").disabled = !anyChecked;
                    });
                });
            </script>
            <script>
                function prepareAndSubmitForm() {
                    const form = document.getElementById("checkoutForm");

                    document.querySelectorAll(".dynamic-input").forEach(input => input.remove());

                    const checkboxes = document.querySelectorAll('.product-check:checked');
                    let total = 0;

                    checkboxes.forEach(cb => {
                        const productId = cb.getAttribute("data-product-id");
                        const quantity = cb.getAttribute("data-quantity");
                        const price = parseFloat(cb.getAttribute("data-price"));
                        total += isNaN(price) ? 0 : price;

                        const idInput = document.createElement("input");
                        idInput.type = "hidden";
                        idInput.name = "productIds";
                        idInput.value = productId;
                        idInput.classList.add("dynamic-input");
                        form.appendChild(idInput);

                        const quantityInput = document.createElement("input");
                        quantityInput.type = "hidden";
                        quantityInput.name = "quantities";
                        quantityInput.value = quantity;
                        quantityInput.classList.add("dynamic-input");
                        form.appendChild(quantityInput);
                    });

                    document.getElementById("hiddenTotalAmount").value = total.toFixed(2);
                    const actionInput = document.createElement("input");
                    actionInput.type = "hidden";
                    actionInput.name = "action";
                    actionInput.value = "fromcart";
                    form.appendChild(actionInput);

                    form.submit();
                }

            </script>



            </body>

            </html>
            <%@include file="/WEB-INF/include/dashboard-footer.jsp" %>