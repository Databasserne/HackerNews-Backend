/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.resource;

import com.bluetrainsoftware.prometheus.Prometheus;
import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.model.Comment;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.impl.CommentRepo;
import com.databasserne.hackernews.repo.impl.PostRepo;
import com.databasserne.hackernews.repo.impl.UserRepo;
import com.databasserne.hackernews.service.CommentService;
import com.databasserne.hackernews.service.IComment;
import com.databasserne.hackernews.service.IUser;
import com.databasserne.hackernews.service.UserService;
import com.google.gson.*;
import io.swagger.annotations.Api;
import javax.annotation.security.PermitAll;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

/**
 *
 * @author Kasper S. Worm & jonassimonsen
 */
@Api
@Path("v1/post")
public class CommentResource {

    private Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    private IComment commentService;
    private IUser userService;

    @GET
    @Path("{id}/comment")
    @Produces(MediaType.APPLICATION_JSON)
    @Prometheus(name = "request_comments", help = "Comments API.")
    public Response getComments(@Context SecurityContext context, @PathParam("id") int id) {
        commentService = new CommentService(new CommentRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)),
                new PostRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
        JsonObject response;
        try {
            JsonArray result;
            if(context.getUserPrincipal() == null) {
                result = commentService.getCommentsForPost(id, -1);
            } else {
                result = commentService.getCommentsForPost(id, Integer.parseInt(context.getUserPrincipal().getName()));
            }

            return Response.status(Response.Status.OK).entity(gson.toJson(result)).type(MediaType.APPLICATION_JSON).build();
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
    @Prometheus(name = "request_child_comments", help = "Child Comments API.")
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    @Prometheus(name = "request_post_comment", help = "Post Comment API.")
    public Response postComment(@Context SecurityContext context, @PathParam("id") int id, String content) {
        commentService = new CommentService(new CommentRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
        userService = new UserService(new UserRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
        JsonObject response;
        try {
            User user = userService.getUserInfo(Integer.parseInt(context.getUserPrincipal().getName()));
            JsonObject inputJson = new JsonParser().parse(content).getAsJsonObject();
            String comment = null;
            if (inputJson.has("comment_text")) {
                comment = inputJson.get("comment_text").getAsString();
            }
            Comment com = null;
            if(inputJson.has("comment_id")) {
                com = commentService.createComment(comment, user, id, inputJson.get("comment_id").getAsInt());
            } else {
                com = commentService.createComment(comment, user, id);
            }


            return Response.status(Response.Status.CREATED).entity(gson.toJson(com)).type(MediaType.APPLICATION_JSON).build();
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
    
    @POST
    @Path("{id}/comment/{commentId}/upvote")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    @Prometheus(name = "request_post_upvote", help = "Upvote Comment API.")
    public Response upvoteComment(@Context SecurityContext context, @PathParam("commentId") int id) {
        JsonObject response;
        try {
            User user = new User();
            user.setId(Integer.parseInt(context.getUserPrincipal().getName()));
            commentService = new CommentService(new CommentRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
            Comment c = new Comment();
            c.setId(id);

            commentService.voteComment(user, c, 1);

            return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).build();
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
    @Path("{id}/comment/{commentId}/downvote")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    @Prometheus(name = "request_comment_downvote", help = "Downvote Comment API.")
    public Response downvoteComment(@Context SecurityContext context, @PathParam("commentId") int id) {
        JsonObject response;
        try {
            User user = new User();
            user.setId(Integer.parseInt(context.getUserPrincipal().getName()));
            commentService = new CommentService(new CommentRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)),
                    new PostRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
            Comment c = new Comment();
            c.setId(id);

            commentService.voteComment(user, c, -1);

            return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).build();
        } catch (BadRequestException badRequest) {
            response = new JsonObject();
            response.addProperty("error_code", 400);
            response.addProperty("error_message", badRequest.getMessage());

            return Response.status(Response.Status.BAD_REQUEST).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
        } catch (NotFoundException notFound) {
            response = new JsonObject();
            response.addProperty("error_code", 404);
            response.addProperty("error_message", notFound.getMessage());

            return Response.status(Response.Status.NOT_FOUND).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            response = new JsonObject();
            response.addProperty("error_code", 500);
            response.addProperty("error_meesage", "Unknown server error.");

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
        }
    }
}
