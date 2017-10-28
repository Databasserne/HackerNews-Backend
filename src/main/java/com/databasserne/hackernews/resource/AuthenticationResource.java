package com.databasserne.hackernews.resource;

import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.impl.UserRepo;
import com.databasserne.hackernews.service.AuthenticationService;
import com.databasserne.hackernews.service.IAuthenticationService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.swagger.annotations.Api;

import javax.persistence.Persistence;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api
@Path("v1/auth")
public class AuthenticationResource {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private IAuthenticationService authService;

    @POST
    @Path("login")
    public Response login(String content) {
        JsonObject response;
        try {
            JsonObject input = new JsonParser().parse(content).getAsJsonObject();
            authService = new AuthenticationService(new UserRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV)));

            User user = authService.login(input.get("username").getAsString(), input.get("password").getAsString());
            String token;

            response = new JsonObject();

            return Response.status(Response.Status.OK).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
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
            response.addProperty("error_message", "Unknown server error");

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
        }
    }
}
