/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.service.security.Secrets;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Vixo
 */
public class TokenService implements IToken {

    private static final String ISSUER = "Databasserne";

    private JWSSigner signer;
    private JWTClaimsSet claim;
    private SignedJWT signedJWT;

    public TokenService() {
    }

    @Override
    public String createToken(User user) throws JOSEException {

        signer = new MACSigner(Secrets.TOKEN_SECRET.getBytes());
        Calendar date = Calendar.getInstance();
        Calendar expDate = date;
        expDate.add(Calendar.HOUR_OF_DAY, 24);
        claim = new JWTClaimsSet.Builder()
                .subject(String.valueOf(user.getId()))
                .claim("username", user.getUsername())
                .claim("fullname", user.getFullname())
                .issueTime(date.getTime())
                .expirationTime(expDate.getTime())
                .issuer(ISSUER)
                .build();

        signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claim);
        signedJWT.sign(signer);
        
        return signedJWT.serialize();
    }

}
