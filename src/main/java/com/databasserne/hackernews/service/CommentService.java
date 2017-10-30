/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.service;

import com.databasserne.hackernews.repo.ICommentRepo;
import java.util.List;

/**
 *
 * @author Kasper S. Worm
 */
public class CommentService implements IComment{
    private ICommentRepo commentRepo;

    public CommentService() {
    }

    public CommentService(ICommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    @Override
    public List<IComment> getCommentsForPost(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IComment createComment(String body) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<IComment> getSingleCommentAndChildComment(int postId, int userId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
