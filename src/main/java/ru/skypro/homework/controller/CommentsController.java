package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.service.CommentService;

import javax.validation.Valid;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class CommentsController {
    private final Logger logger = LoggerFactory.getLogger(CommentsController.class);
    private final CommentService commentService;

    @GetMapping("/ads/{id}/comments")
    public ResponseEntity<CommentsDTO> getComments(@PathVariable(name = "id") int adId) {
        return ResponseEntity.ok(commentService.getCommentsByAdId(adId));
    }

    @PostMapping("/ads/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable(name = "id") int adId,
                                                 @Valid @RequestBody CreateOrUpdateCommentDTO createCommentDTO,
                                                 Authentication authentication) {
        return ResponseEntity.ok(commentService.addCommentToAd(adId, createCommentDTO, authentication));
    }

    @DeleteMapping("/ads/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable(name = "adId") int adId,
                                              @PathVariable(name = "commentId") int commentId,
                                              Authentication authentication) {
        commentService.deleteComment(adId, commentId, authentication);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/ads/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable(name = "adId") int adId,
                                                    @PathVariable(name = "commentId") int commentId,
                                                    @Valid @RequestBody CreateOrUpdateCommentDTO updateCommentDTO,
                                                    Authentication authentication) {
        return ResponseEntity.ok(commentService.updateComment(adId, commentId, updateCommentDTO, authentication));
    }

    @ExceptionHandler
    ResponseEntity<?> handleCommentNotFound(CommentNotFoundException e) {
        logger.error(e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    ResponseEntity<?> handleUserNotFound(UserNotFoundException e) {
        logger.error(e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    ResponseEntity<?> handleAdNotFound(AdNotFoundException e) {
        logger.error(e.getMessage());
        return ResponseEntity.notFound().build();
    }

}
