package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    private final AdService adService;
    private final CommentService commentService;

    public SecurityService(AdService adService, CommentService commentService) {
        this.adService = adService;
        this.commentService = commentService;
    }

    public boolean isOwnerAd(Authentication authentication, Integer adId) {
        String ownerUserName = adService.getAdById(adId).getAuthor().getUsername();
        return authentication.getName().equals(ownerUserName);
    }

    public boolean isOwnerComment(Authentication authentication, Integer commentId) {
        String ownerUserName = commentService.getCommentById(commentId).getAuthor().getUsername();
        return authentication.getName().equals(ownerUserName);
    }
}
