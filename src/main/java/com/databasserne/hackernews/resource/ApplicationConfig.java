package com.databasserne.hackernews.resource;

import com.databasserne.hackernews.repo.impl.UserRepo;
import com.databasserne.hackernews.resource.filter.CorsFilter;
import com.databasserne.hackernews.resource.filter.JWTAuthenticationFilter;
import io.swagger.jaxrs.config.BeanConfig;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("api")
public class ApplicationConfig extends Application {

    public ApplicationConfig() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/api");
        beanConfig.setResourcePackage("com.databasserne.hackernews.resource");
        beanConfig.setScan(true);
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        addResources(resources);
        return resources;
    }

    private void addResources(Set<Class<?>> resources) {
        // Resources
        resources.add(AuthenticationResource.class);
        resources.add(UserResource.class);
        resources.add(PostResource.class);
        resources.add(SimulatorResource.class);
        resources.add(CommentResource.class);

        // Filters
        resources.add(CorsFilter.class);
        resources.add(JWTAuthenticationFilter.class);

        // Swagger
        resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
    }
}
