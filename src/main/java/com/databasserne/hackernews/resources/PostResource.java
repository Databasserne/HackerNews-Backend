package com.databasserne.hackernews.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1/post")
@Api(value = "/v1/post", description = "Manage posts")
public class PostResource {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "List of all posts",
            notes = "List of all posts",
            response = JsonObject.class,
            responseContainer = "List"
    )
    public Response getAllPosts() {
        JsonArray response = new JsonArray();
        JsonObject postObj = new JsonObject();
        postObj.addProperty("title", "test title");
        postObj.addProperty("author_name", "MyName");
        postObj.addProperty("created_at", "today");
        response.add(postObj);

        return Response.status(Response.Status.OK).entity(gson.toJson(response)).build();
    }
}
