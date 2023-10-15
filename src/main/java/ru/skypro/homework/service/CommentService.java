package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.model.Comment;

public interface CommentService {
    CommentsDTO getCommentsByAdId(Integer adId);

    CommentDTO addCommentToAd(Integer adId, CreateOrUpdateCommentDTO createCommentDTO, Authentication authentication);

    void deleteComment(Integer adId, Integer commentId);

    Comment getCommentById(Integer commentId);

    CommentDTO updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentDTO updateCommentDTO);
}
