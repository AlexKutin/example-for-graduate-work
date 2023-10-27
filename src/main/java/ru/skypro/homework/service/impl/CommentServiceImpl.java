package ru.skypro.homework.service.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.AdUser;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentMapper;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private static final String COMMENT_NOT_FOUND_BY_ID_MSG = "Комментарий с id: %d не найден в БД";
    private static final String COMMENT_NOT_FOUND_BY_AD_ID_MSG = "Комментарий с id: %d не найден в БД для объявления с id: %d";
    private final CommentRepository commentRepository;
    private final AdService adService;
    private final UserService userService;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository, AdService adService, UserService userService, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.adService = adService;
        this.userService = userService;
        this.commentMapper = commentMapper;
    }

    @Override
    public CommentsDTO getCommentsByAdId(int adId) {
        List<Comment> comments = commentRepository.findCommentsByAd_AdId(adId);
        return commentMapper.toCommentsDTO(comments);
    }

    @Override
    public CommentDTO addCommentToAd(int adId, CreateOrUpdateCommentDTO createCommentDTO, Authentication authentication) {
        Ad ad = adService.getAdById(adId);
        AdUser author = userService.getCurrentUser(authentication);

        Comment comment = new Comment();
        comment.setCommentText(createCommentDTO.getText());
        comment.setAd(ad);
        comment.setAuthor(author);
        comment.setCreatedAt(LocalDateTime.now());
        comment = commentRepository.save(comment);

        return commentMapper.toCommentDTO(comment);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') or @securityService.isOwnerComment(authentication, #commentId)")
    public void deleteComment(int adId, int commentId, Authentication authentication) {
        Comment comment = checkCommentPresent(adId, commentId);
        commentRepository.delete(comment);
    }

    @Override
    public Comment getCommentById(int commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new CommentNotFoundException(String.format(COMMENT_NOT_FOUND_BY_ID_MSG, commentId)));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') or @securityService.isOwnerComment(authentication, #commentId)")
    public CommentDTO updateComment(int adId, int commentId, CreateOrUpdateCommentDTO updateCommentDTO, Authentication authentication) {
        Comment comment = checkCommentPresent(adId, commentId);
        comment.setCommentText(updateCommentDTO.getText());
        comment = commentRepository.save(comment);
        return commentMapper.toCommentDTO(comment);
    }

    private Comment checkCommentPresent(int adId, int commentId) {
        Comment comment = getCommentById(commentId);
        if (!adService.isAdPresent(adId) || comment.getAd().getAdId() != adId) {
            throw new CommentNotFoundException(String.format(COMMENT_NOT_FOUND_BY_AD_ID_MSG, commentId, adId));
        }
        return comment;
    }
}
