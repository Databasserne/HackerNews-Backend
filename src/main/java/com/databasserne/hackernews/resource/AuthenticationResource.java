package com.databasserne.hackernews.resource;

import com.bluetrainsoftware.prometheus.Prometheus;
import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.impl.UserRepo;
import com.databasserne.hackernews.service.Authentication;
import com.databasserne.hackernews.service.IAuthentication;
import com.databasserne.hackernews.service.IToken;
import com.databasserne.hackernews.service.TokenService;
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

    private Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    private IAuthentication authService;
    private IToken tokenService;

    @POST
    @Path("login")
    @Prometheus(name = "request_login", help = "Login API.")
    public Response login(String content) {
        JsonObject response;
        try {
            JsonObject input = new JsonParser().parse(content).getAsJsonObject();
            String username = null;
            String password = null;
            if(input.has("username")) username = input.get("username").getAsString();
            if(input.has("password")) password = input.get("password").getAsString();
            authService = new Authentication(new UserRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
            tokenService = new TokenService();

            User user = authService.login(username, password);
            String token = tokenService.createToken(user);

            response = new JsonObject();
            response.addProperty("token", token);
            response.addProperty("username",user.getUsername());
            response.addProperty("fullname", user.getFullname());

            return Response
                    .status(Response.Status.OK)
                    .entity(gson.toJson(response))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
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
            e.printStackTrace();
            response = new JsonObject();
            response.addProperty("error_code", 500);
            response.addProperty("error_message", "Unknown server error.");

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Path("signup")
    @Prometheus(name = "request_signup", help = "Sign Up API.")
    public Response signup(String content) {
        JsonObject response;
        try {
            JsonObject inputJson = new JsonParser().parse(content).getAsJsonObject();
            String username = null;
            String password = null;
            String rep_password = null;
            String fullname = null;
            if(inputJson.has("username")) username = inputJson.get("username").getAsString();
            if(inputJson.has("password")) password = inputJson.get("password").getAsString();
            if(inputJson.has("rep_password")) rep_password = inputJson.get("rep_password").getAsString();
            if(inputJson.has("fullname")) fullname = inputJson.get("fullname").getAsString();
            authService = new Authentication(new UserRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));

            User user = authService.register(username, password, rep_password, fullname);

            return Response.status(Response.Status.OK).entity(gson.toJson(user)).type(MediaType.APPLICATION_JSON).build();
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
