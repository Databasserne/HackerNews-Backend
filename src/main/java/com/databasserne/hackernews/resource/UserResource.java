package com.databasserne.hackernews.resource;

import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.impl.UserRepo;
import com.databasserne.hackernews.service.IUser;
import com.databasserne.hackernews.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;

import javax.annotation.security.PermitAll;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Api
@Path("v1/user")
public class UserResource {

    private Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    private IUser userService;

    @GET
    @Path("me")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getUserInfo(@Context SecurityContext context) {
        userService = new UserService(new UserRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
        User user = userService.getUserInfo(Integer.parseInt(context.getUserPrincipal().getName()));

        JsonObject response = new JsonObject();
        response.addProperty("username", user.getUsername());
        response.addProperty("fullname", user.getFullname());

        return Response.status(Response.Status.OK).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
    }
}
