package com.databasserne.hackernews.resource;

import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.impl.PostRepo;
import com.databasserne.hackernews.repo.impl.UserRepo;
import com.databasserne.hackernews.service.Authentication;
import com.databasserne.hackernews.service.IAuthentication;
import com.databasserne.hackernews.service.IPost;
import com.databasserne.hackernews.service.PostService;
import com.google.gson.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.security.PermitAll;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

@Api
@Path("/v1/post")
public class PostResource {

    private Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    private IPost postService;
    private IAuthentication authService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "List of all posts",
            notes = "List of all posts",
            response = JsonObject.class,
            responseContainer = "List"
    )
    public Response getAllPosts() {
        postService = new PostService(new PostRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));

        return Response.status(Response.Status.OK).entity(gson.toJson(postService.getAllPosts())).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPost(@PathParam("id") int id) {
        postService = new PostService(new PostRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
        JsonObject response;
        try {
            return Response.status(Response.Status.OK).entity(gson.toJson(postService.getPost(id))).type(MediaType.APPLICATION_JSON).build();
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response createPost(String content) {
        JsonObject response;
        try {
            JsonObject inputJson = new JsonParser().parse(content).getAsJsonObject();
            postService = new PostService(new PostRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
            String title = null;
            String body = null;
            if(inputJson.has("title")) title = inputJson.get("title").getAsString();
            if(inputJson.has("body")) body = inputJson.get("body").getAsString();

            postService.createPost(title, body);
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

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response editPost(@Context SecurityContext securityContext, @PathParam("id") int id, String content) {
        JsonObject response;
        try {
            JsonObject inputJson = new JsonParser().parse(content).getAsJsonObject();
            postService = new PostService(new PostRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
            String title = null;
            String body = null;
            if(inputJson.has("title")) title = inputJson.get("title").getAsString();
            if(inputJson.has("body")) body = inputJson.get("body").getAsString();

            postService.editPost(id, title, body);
            return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).build();
        } catch (NotFoundException notFound) {
            response = new JsonObject();
            response.addProperty("error_code", 404);
            response.addProperty("error_message", notFound.getMessage());

            return Response.status(Response.Status.NOT_FOUND).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
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
}
