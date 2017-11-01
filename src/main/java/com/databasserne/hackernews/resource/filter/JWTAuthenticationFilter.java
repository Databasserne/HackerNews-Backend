package com.databasserne.hackernews.resource.filter;

import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.impl.UserRepo;
import com.databasserne.hackernews.service.security.Secrets;
import com.databasserne.hackernews.service.security.UserPrincipal;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.persistence.Persistence;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JWTAuthenticationFilter implements ContainerRequestFilter {

    private static final List<Class<? extends Annotation>> securityAnnotations = Arrays.asList(DenyAll.class, PermitAll.class, RolesAllowed.class);

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext request) throws IOException {

        if (isSecuredResource()) {
            String authorizationHeader = request.getHeaderString("Authorization");
            if (authorizationHeader == null) {
                throw new NotAuthorizedException("No authorization header provided", Response.Status.UNAUTHORIZED);
            }
            String token = request.getHeaderString("Authorization").substring("Bearer ".length());
            try {
                if (tokenIsExpired(token)) {
                    throw new NotAuthorizedException("Your authorization token has timed out, please login again", Response.Status.UNAUTHORIZED);
                }

                String id = getUserIdFromToken(token);
                final UserPrincipal user = getPricipalByUserId(id);
                if (user == null) {
                    throw new NotAuthorizedException("User could not be authenticated via the provided token", Response.Status.FORBIDDEN);
                }

                request.setSecurityContext(new SecurityContext() {

                    @Override
                    public boolean isUserInRole(String role) {
                        return user.isUserInRole(role);
                    }

                    @Override
                    public boolean isSecure() {
                        return false;
                    }

                    @Override
                    public Principal getUserPrincipal() {
                        return user;
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return SecurityContext.BASIC_AUTH;
                    }
                });

            } catch (ParseException | JOSEException e) {
                throw new NotAuthorizedException("You are not authorized to perform this action", Response.Status.FORBIDDEN);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(JWTAuthenticationFilter.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            } catch (InvalidKeySpecException ex) {
                Logger.getLogger(JWTAuthenticationFilter.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            } catch (Exception en) {
                en.printStackTrace();
            }
        } else {
            String authorizationHeader = request.getHeaderString("Authorization");
            if (authorizationHeader == null) {
                return;
            }
            String token = request.getHeaderString("Authorization").substring("Bearer ".length());
            try {
                if (tokenIsExpired(token)) {
                    return;
                }

                String id = getUserIdFromToken(token);
                final UserPrincipal user = getPricipalByUserId(id);
                if (user == null) {
                    return;
                }

                request.setSecurityContext(new SecurityContext() {

                    @Override
                    public boolean isUserInRole(String role) {
                        return user.isUserInRole(role);
                    }

                    @Override
                    public boolean isSecure() {
                        return false;
                    }

                    @Override
                    public Principal getUserPrincipal() {
                        return user;
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return SecurityContext.BASIC_AUTH;
                    }
                });

            } catch (ParseException | JOSEException e) {
                return;
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(JWTAuthenticationFilter.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            } catch (InvalidKeySpecException ex) {
                Logger.getLogger(JWTAuthenticationFilter.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            } catch (Exception en) {
                en.printStackTrace();
            }
        }
    }

    private UserPrincipal getPricipalByUserId(String username) throws NoSuchAlgorithmException, InvalidKeySpecException {
        UserRepo userRepo = new UserRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME));
        User user = userRepo.getUserById(Integer.parseInt(username));
        if (user != null) {
            return new UserPrincipal(String.valueOf(user.getId()));
        }
        return null;
    }

    private boolean isSecuredResource() {

        for (Class<? extends Annotation> securityClass : securityAnnotations) {
            if (resourceInfo.getResourceMethod().isAnnotationPresent(securityClass)) {
                return true;
            }
        }

        for (Class<? extends Annotation> securityClass : securityAnnotations) {
            if (resourceInfo.getResourceClass().isAnnotationPresent(securityClass)) {
                return true;
            }
        }

        return false;
    }

    private boolean tokenIsExpired(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(Secrets.TOKEN_SECRET);

        if (signedJWT.verify(verifier)) {
            return new Date().getTime() > signedJWT.getJWTClaimsSet().getExpirationTime().getTime();
        }
        return false;
    }

    private String getUserIdFromToken(String token) throws ParseException, JOSEException {

        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(Secrets.TOKEN_SECRET);

        if (signedJWT.verify(verifier)) {
            return signedJWT.getJWTClaimsSet().getSubject();
        } else {
            throw new JOSEException("Firm is not verified.");
        }
    }
}
