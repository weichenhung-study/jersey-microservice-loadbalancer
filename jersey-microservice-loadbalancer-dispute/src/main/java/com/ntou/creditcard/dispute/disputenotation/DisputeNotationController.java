package com.ntou.creditcard.dispute.disputenotation;

import com.ntou.tool.Common;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("DisputeNotation")
public class DisputeNotationController {
    @PUT
    @Consumes(MediaType.APPLICATION_JSON + Common.CHARSET_UTF8)
    @Produces(MediaType.APPLICATION_JSON + Common.CHARSET_UTF8)
    public Response doController(DisputeNotationReq req) throws Exception {
        return new DisputeNotation().doAPI(req);
    }
}