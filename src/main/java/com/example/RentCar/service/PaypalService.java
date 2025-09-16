package com.example.RentCar.service;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class PaypalService {

    private final PayPalHttpClient payPalClient;

    public PaypalService(PayPalHttpClient payPalClient) {
        this.payPalClient = payPalClient;
    }

    /**
     * Tạo order PayPal
     *
     * @param amount    Giá trị thanh toán (format string 2 chữ số thập phân)
     * @param currency  Mã tiền tệ (USD)
     * @param returnUrl URL callback khi thanh toán thành công
     * @param cancelUrl URL callback khi hủy
     * @return approval link (URL redirect user sang PayPal)
     */
    public String createOrder(String amount, String currency, String returnUrl, String cancelUrl) {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        ApplicationContext applicationContext = new ApplicationContext()
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl);
        orderRequest.applicationContext(applicationContext);

        List<PurchaseUnitRequest> purchaseUnits = Arrays.asList(
                new PurchaseUnitRequest()
                        .amountWithBreakdown(new AmountWithBreakdown().currencyCode(currency).value(amount))
        );
        orderRequest.purchaseUnits(purchaseUnits);

        OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);

        try {
            HttpResponse<Order> response = payPalClient.execute(request);
            if (response.statusCode() == 201) {
                for (LinkDescription link : response.result().links()) {
                    if ("approve".equals(link.rel())) {
                        return link.href(); // URL redirect user sang PayPal
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // có thể log lỗi hoặc ném RuntimeException nếu muốn dừng xử lý
        }

        return null; // nếu tạo order thất bại
    }

    /**
     * Capture order đã được approve
     *
     * @param orderId ID order PayPal
     * @return HttpResponse<Order>
     * @throws IOException
     */
    public HttpResponse<Order> captureOrder(String orderId) throws IOException {
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
        return payPalClient.execute(request);
    }
}
