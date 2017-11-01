package com.databasserne.hackernews.resource;

import io.swagger.annotations.Api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api
public class SimulatorResource {

    @GET
    @Path("latest")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLatest() {

        return null;
    }
}
