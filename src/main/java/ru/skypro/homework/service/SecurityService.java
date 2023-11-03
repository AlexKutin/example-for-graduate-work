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

    /**
     * Проверяет, является ли указанный пользователь создателем объявления с идентификаторм adId
     * @param authentication текущий авторизованный пользователь, предоставляет фронтенд
     * @param adId идентификатор объявления
     * @return true, если пользователь, указанный в поле authentication, является создателем объявления
     */
    public boolean isOwnerAd(Authentication authentication, Integer adId) {
        String ownerUserName = adService.getAdById(adId).getAuthor().getUsername();
        return authentication.getName().equals(ownerUserName);
    }

    /**
     * Проверяет, является ли указанный пользователь создателем комментария с идентификаторм commentId
     * @param authentication текущий авторизованный пользователь, предоставляет фронтенд
     * @param commentId идентификатор комментария
     * @return true, если пользователь, указанный в поле authentication, является создателем комментария
     */
    public boolean isOwnerComment(Authentication authentication, Integer commentId) {
        String ownerUserName = commentService.getCommentById(commentId).getAuthor().getUsername();
        return authentication.getName().equals(ownerUserName);
    }
}
