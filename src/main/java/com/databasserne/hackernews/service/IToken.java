/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.User;
import com.nimbusds.jose.JOSEException;

/**
 *
 * @author Vixo
 */
public interface IToken {
    String createToken(User user) throws JOSEException;
}
