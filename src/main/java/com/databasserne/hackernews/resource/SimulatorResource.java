/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.resource;

import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.repo.impl.SimulatorRepo;
import com.databasserne.hackernews.service.ISimulator;
import com.databasserne.hackernews.service.SimulatorService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Kasper S. Worm
 */
@Api
@Path("v1/")
public class SimulatorResource {

    private Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    private ISimulator simulatorService;

    @POST
    @Path("post")
    @Produces(MediaType.APPLICATION_JSON)
    public Response simulatorPost(@PathParam("id") int id) {
        JsonObject response;
        simulatorService = new SimulatorService(new SimulatorRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
        try {
            return null;
        } catch (NotFoundException notFound) {
            response = new JsonObject();
            response.addProperty("error_code", 400);
            response.addProperty("error_message", notFound.getMessage());

            return Response.status(Response.Status.BAD_REQUEST).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            response = new JsonObject();
            response.addProperty("error_code", 500);
            response.addProperty("error_message", "Unknown server error.");

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("latest")
    @Produces(MediaType.APPLICATION_JSON)
    public Response simulatorLatest(@PathParam("id") int id) {
        JsonObject response;
        try {
            return null;
        } catch (NotFoundException notFound) {
            response = new JsonObject();
            response.addProperty("error_code", 400);
            response.addProperty("error_message", notFound.getMessage());

            return Response.status(Response.Status.BAD_REQUEST).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            response = new JsonObject();
            response.addProperty("error_code", 500);
            response.addProperty("error_message", "Unknown server error.");

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response simulatorStatus(@PathParam("id") int id) {
        JsonObject response;
        try {
            return null;
        } catch (NotFoundException notFound) {
            response = new JsonObject();
            response.addProperty("error_code", 400);
            response.addProperty("error_message", notFound.getMessage());

            return Response.status(Response.Status.BAD_REQUEST).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            response = new JsonObject();
            response.addProperty("error_code", 500);
            response.addProperty("error_message", "Unknown server error.");

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
        }
    }

}
