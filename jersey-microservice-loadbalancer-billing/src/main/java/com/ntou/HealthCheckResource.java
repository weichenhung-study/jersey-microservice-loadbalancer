package com.ntou;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/health")
public class HealthCheckResource {

    @GET
    public Response health() {
        return Response.ok("OK").build();
    }
}

