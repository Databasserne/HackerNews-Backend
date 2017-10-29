package com.databasserne.hackernews.resource;

import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.repo.impl.PostRepo;
import com.databasserne.hackernews.service.IPost;
import com.databasserne.hackernews.service.PostService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api
@Path("/v1/post")
public class PostResource {

    private Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    private IPost postService;

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

        return null;
    }
}
