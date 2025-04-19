package com.ntou.creditcard.management.activation;

import com.ntou.tool.Common;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("Activation")
public class ActivationController {
    @PUT
    @Consumes(MediaType.APPLICATION_JSON + Common.CHARSET_UTF8)
    @Produces(MediaType.APPLICATION_JSON + Common.CHARSET_UTF8)
    public Response doController(ActivationReq req) throws Exception {
        return new Activation().doAPI(req);
    }
}