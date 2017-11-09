/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.resource;

import com.bluetrainsoftware.prometheus.Prometheus;
import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.repo.impl.SimulatorRepo;
import com.databasserne.hackernews.service.ISimulator;
import com.databasserne.hackernews.service.SimulatorService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Histogram;
import io.swagger.annotations.Api;
import javax.annotation.security.PermitAll;
import javax.persistence.Persistence;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.StringWriter;

/**
 *
 * @author Kasper S. Worm
 */
@Api
@Path("")
public class SimulatorResource {

    private Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    private ISimulator simulatorService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("post")
    @PermitAll
    @Prometheus(name = "request_sim_post", help = "Post")
    public Response createPost(String content) {
        JsonObject response = null;
        try {
            simulatorService = new SimulatorService(new SimulatorRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
            JsonObject inputJson = new JsonParser().parse(content).getAsJsonObject();
            String title = null;
            String text = null;
            String username = null;
            String password = null;
            String url = null;
            String type = null;
            int parentId = 0;
            int hanesst = 0;

            if (inputJson.has("post_title")) {
                title = inputJson.get("post_title").getAsString();
            }
            if (inputJson.has("post_text")) {
                text = inputJson.get("post_text").getAsString();
            }
            if (inputJson.has("username")) {
                username = inputJson.get("username").getAsString();
            }
            if (inputJson.has("pwd_hash")) {
                password = inputJson.get("pwd_hash").getAsString();
            }
            if (inputJson.has("post_url")) {
                url = inputJson.get("post_url").getAsString();
            }
            if (inputJson.has("post_type")) {
                type = inputJson.get("post_type").getAsString();
            }
            if (inputJson.has("post_parent")) {
                parentId = inputJson.get("post_parent").getAsInt();
            }
            if (inputJson.has("hanesst_id")) {
                hanesst = inputJson.get("hanesst_id").getAsInt();
            }

            simulatorService.simulatorPost(title, text, url, username, password, type, hanesst, parentId);
            return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).build();
        } catch (BadRequestException badRequest) {
            response = new JsonObject();
            response.addProperty("error_code", 400);
            response.addProperty("error_message", badRequest.getMessage());
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
    @Prometheus(name = "request_sim_latest", help = "Latest")
    public Response simulatorLatest() {
        simulatorService = new SimulatorService(new SimulatorRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
        JsonObject response;
        try {
            return Response.status(Response.Status.OK).entity(gson.toJson(simulatorService.getLatest())).build();
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
    @Prometheus(name = "request_sim_status", help = "Status")
    public Response simulatorStatus() {
        simulatorService = new SimulatorService(new SimulatorRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
        JsonObject response;
        try {
            return Response.status(Response.Status.OK).entity(simulatorService.getStatus()).build();
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
    @Path("metrics")
    public String getMetrics() throws IOException {
        StringWriter stringWriter = new StringWriter();
        io.prometheus.client.exporter.common.TextFormat.write004(
                stringWriter, CollectorRegistry.defaultRegistry.metricFamilySamples());

        return stringWriter.toString();
    }

}
