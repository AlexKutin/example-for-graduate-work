package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.model.Comment;

public interface CommentService {
    CommentsDTO getCommentsByAdId(int adId);

    CommentDTO addCommentToAd(int adId, CreateOrUpdateCommentDTO createCommentDTO, Authentication authentication);

    void deleteComment(int adId, int commentId, Authentication authentication);

    Comment getCommentById(int commentId);

    CommentDTO updateComment(int adId, int commentId, CreateOrUpdateCommentDTO updateCommentDTO, Authentication authentication);
}
