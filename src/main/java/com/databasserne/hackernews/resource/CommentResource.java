/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.resource;

import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.repo.impl.CommentRepo;
import com.databasserne.hackernews.service.CommentService;
import com.databasserne.hackernews.service.IComment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
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
 * @author Kasper S. Worm & jonassimonsen
 */
@Api
@Path("v1/post")
public class CommentResource {

    private Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    private IComment commentService;

    @GET
    @Path("{id}/comment")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComments(@PathParam("id") int id) {
        commentService = new CommentService(new CommentRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
        JsonObject response;
        try {
            return Response.status(Response.Status.OK).entity(gson.toJson(commentService.getCommentsForPost(id))).type(MediaType.APPLICATION_JSON).build();
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
    @Path("{id}/comment/{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCommentsAndChildComments(@PathParam("commentId") int id) {
        commentService = new CommentService(new CommentRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
        JsonObject response;

        try {
            return Response.status(Response.Status.OK).entity(gson.toJson(commentService.getCommentsAndChildComments(id))).build();
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

    @POST
    @Path("{id}/comment")
    @Produces(MediaType.APPLICATION_JSON)
    public Response postComment(@PathParam("id") int id, String content) {
        commentService = new CommentService(new CommentRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
        JsonObject response;
        try {

            JsonObject inputJson = new JsonParser().parse(content).getAsJsonObject();
            String comment = null;
            if (inputJson.has("comment")) {
                comment = inputJson.get("comment").getAsString();
            }

            commentService.createComment(comment, id);
            return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).build();
        } catch (NotFoundException notFound) {
            response = new JsonObject();
            response.addProperty("error_code", 400);
            response.addProperty("error_message", notFound.getMessage());

            return Response.status(Response.Status.BAD_REQUEST).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
        } catch (JsonSyntaxException e) {
            response = new JsonObject();
            response.addProperty("error_code", 500);
            response.addProperty("error_message", "Unknown server error.");

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
        }
    }
}
