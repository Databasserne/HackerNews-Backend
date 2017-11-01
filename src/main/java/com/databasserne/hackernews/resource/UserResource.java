package com.databasserne.hackernews.resource;

import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.impl.PostRepo;
import com.databasserne.hackernews.repo.impl.UserRepo;
import com.databasserne.hackernews.service.IPost;
import com.databasserne.hackernews.service.IUser;
import com.databasserne.hackernews.service.PostService;
import com.databasserne.hackernews.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.swagger.annotations.Api;

import javax.annotation.security.PermitAll;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Api
@Path("v1/user")
@PermitAll
public class UserResource {

    private Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    private IUser userService;
    private IPost postService;

    @GET
    @Path("me")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserInfo(@Context SecurityContext context) {
        userService = new UserService(new UserRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
        User user = userService.getUserInfo(Integer.parseInt(context.getUserPrincipal().getName()));

        JsonObject response = new JsonObject();
        response.addProperty("username", user.getUsername());
        response.addProperty("fullname", user.getFullname());

        return Response.status(Response.Status.OK).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("post")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPosts(@Context SecurityContext securityContext) {
        userService = new UserService(new UserRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
        User user = userService.getUserInfo(Integer.parseInt(securityContext.getUserPrincipal().getName()));
        postService = new PostService(new PostRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));

        return Response.status(Response.Status.OK).entity(gson.toJson(postService.getUserPosts(user))).type(MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("edit")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editUserInfo(@Context SecurityContext context, String content) {
        JsonObject response;
        try {
            JsonObject inputJson = new JsonParser().parse(content).getAsJsonObject();
            if(!inputJson.has("fullname") || inputJson.get("fullname").getAsString().equals("")) throw new BadRequestException("Fullname is required.");
            userService = new UserService(new UserRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
            User user = userService.getUserInfo(Integer.parseInt(context.getUserPrincipal().getName()));
            user.setFullname(inputJson.get("fullname").getAsString());
            userService.editUserInfo(user);

            return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).build();
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
