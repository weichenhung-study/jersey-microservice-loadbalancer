package com.ntou.creditcard.billing.feepayment;

import com.ntou.tool.Common;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("FeePayment")
public class FeePaymentController {
    @POST
    @Consumes(MediaType.APPLICATION_JSON + Common.CHARSET_UTF8)
    @Produces(MediaType.APPLICATION_JSON + Common.CHARSET_UTF8)
    public Response doController(FeePaymentReq req) throws Exception {
        return new FeePayment().doAPI(req);
    }
}