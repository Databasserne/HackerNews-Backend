package com.databasserne.hackernews.resource;

import io.swagger.annotations.Api;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Api
@Path("v1/auth")
public class AuthenticationResource {

    @POST
    @Path("login")
    public Response login(String content) {

        return null;
    }
}
