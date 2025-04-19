package com.ntou.creditcard.management.application;

import com.ntou.tool.Common;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("Application")
public class ApplicationController {
    @POST
    @Consumes(MediaType.APPLICATION_JSON + Common.CHARSET_UTF8)
    @Produces(MediaType.APPLICATION_JSON + Common.CHARSET_UTF8)
    public Response doController(ApplicationReq req) throws Exception {
        return new Application().doAPI(req);
    }
}