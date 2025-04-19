package com.ntou.creditcard.management.review;

import com.ntou.tool.Common;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("Review")
public class ReviewController {
    @PUT
    @Consumes(MediaType.APPLICATION_JSON + Common.CHARSET_UTF8)
    @Produces(MediaType.APPLICATION_JSON + Common.CHARSET_UTF8)
    public Response doController(ReviewReq req) throws Exception {
        return new Review().doAPI(req);
    }
}