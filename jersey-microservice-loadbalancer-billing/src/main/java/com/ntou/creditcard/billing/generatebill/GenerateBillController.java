package com.ntou.creditcard.billing.generatebill;

import com.ntou.connections.OkHttpServiceClient;

import javax.ws.rs.core.Response;

public class GenerateBillController {
    private final GenerateBill generateBill;
    public GenerateBillController() {
        this.generateBill = new GenerateBill(new OkHttpServiceClient());
    }
    public GenerateBillController(GenerateBill generateBill) {
        this.generateBill = generateBill;
    }

    public Response doController() throws Exception {
        return generateBill.doAPI();
    }
}