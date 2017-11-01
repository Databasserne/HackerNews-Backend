package com.databasserne.hackernews.resource;

import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.impl.PostRepo;
import com.databasserne.hackernews.repo.impl.UserRepo;
import com.databasserne.hackernews.service.*;
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

@Api
@Path("/v1/post")
public class PostResource {

    private Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    private IPost postService;
    private IAuthentication authService;
    private IUser userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "List of all posts",
            notes = "List of all posts",
            response = JsonObject.class,
            responseContainer = "List"
    )
    public Response getAllPosts(@Context SecurityContext context) {

        postService = new PostService(new PostRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
        JsonArray response;
        if (context.getUserPrincipal() == null) {
            response = postService.getAllPosts(-1);
        } else {
            response = postService.getAllPosts(Integer.parseInt(context.getUserPrincipal().getName()));
        }

        return Response.status(Response.Status.OK).entity(gson.toJson(response)).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPost(@Context SecurityContext context, @PathParam("id") int id) {
        postService = new PostService(new PostRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
        JsonObject response;
        try {
            Post post = postService.getPost(id);
            response = new JsonObject();
            response.addProperty("title", post.getTitle());
            response.addProperty("body", post.getBody());
            String author = "";
            if (post.getAuthor() != null) {
                author = post.getAuthor().getUsername();
            }
            response.addProperty("author_name", author);
            response.addProperty("created_at", post.getCreated().toString());

            return Response.status(Response.Status.OK).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
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
            if (inputJson.has("title")) {
                title = inputJson.get("title").getAsString();
            }
            if (inputJson.has("body")) {
                body = inputJson.get("body").getAsString();
            }

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
            if (!inputJson.has("title") || inputJson.get("title").getAsString().equals("")) {
                throw new BadRequestException("Title is required.");
            }
            if (!inputJson.has("body") || inputJson.get("body").getAsString().equals("")) {
                throw new BadRequestException("Body is required.");
            }
            postService = new PostService(new PostRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
            Post post = postService.getPost(id);
            post.setTitle(inputJson.get("title").getAsString());
            post.setBody(inputJson.get("body").getAsString());
            if (!post.validate(Integer.parseInt(securityContext.getUserPrincipal().getName()))) {
                response = new JsonObject();
                response.addProperty("error_code", 401);
                response.addProperty("error_message", "Unauthorized.");
                return Response.status(Response.Status.UNAUTHORIZED).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
            }

            postService.editPost(post);
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
            e.printStackTrace();
            response = new JsonObject();
            response.addProperty("error_code", 500);
            response.addProperty("error_message", "Unknown server error.");

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
        }
    }

    @DELETE
    @Path("{id}")
    @PermitAll
    public Response deletePost(@Context SecurityContext context, @PathParam("id") int id) {
        JsonObject response;
        try {
            postService = new PostService(new PostRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
            Post post = postService.getPost(id);
            post.validate(Integer.parseInt(context.getUserPrincipal().getName()));
            postService.deletePost(post);
            return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).build();
        } catch (BadRequestException badRequest) {
            response = new JsonObject();
            response.addProperty("error_code", 400);
            response.addProperty("error_message", badRequest.getMessage());

            return Response.status(Response.Status.BAD_REQUEST).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            e.printStackTrace();
            response = new JsonObject();
            response.addProperty("error_code", 500);
            response.addProperty("error_message", "Unknown server error.");

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).type(MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Path("{id}/upvote")
    @PermitAll
    public Response upvotePost(@Context SecurityContext context, @PathParam("id") int id) {
        JsonObject response;
        try {
            User user = new User();
            user.setId(Integer.parseInt(context.getUserPrincipal().getName()));
            postService = new PostService(new PostRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
            Post p = new Post();
            p.setId(id);

            postService.votePost(user, p, 1);

            return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).build();
        } catch (BadRequestException badRequest) {
            response = new JsonObject();
            response.addProperty("error_code", 400);
            response.addProperty("error_message", badRequest.getMessage());

            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).build();
        } catch (NotFoundException notFound) {
            response = new JsonObject();
            response.addProperty("error_code", 404);
            response.addProperty("error_message", notFound.getMessage());

            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            response = new JsonObject();
            response.addProperty("error_code", 500);
            response.addProperty("error_meesage", "Unknown server error.");

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Path("{id}/downvote")
    @PermitAll
    public Response downvotePost(@Context SecurityContext context, @PathParam("id") int id) {
        JsonObject response;
        try {
            User user = new User();
            user.setId(Integer.parseInt(context.getUserPrincipal().getName()));
            postService = new PostService(new PostRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
            Post p = new Post();
            p.setId(id);

            postService.votePost(user, p, -1);

            return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).build();
        } catch (BadRequestException badRequest) {
            response = new JsonObject();
            response.addProperty("error_code", 400);
            response.addProperty("error_message", badRequest.getMessage());

            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).build();
        } catch (NotFoundException notFound) {
            response = new JsonObject();
            response.addProperty("error_code", 404);
            response.addProperty("error_message", notFound.getMessage());

            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            response = new JsonObject();
            response.addProperty("error_code", 500);
            response.addProperty("error_meesage", "Unknown server error.");

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).build();
        }
    }
}
