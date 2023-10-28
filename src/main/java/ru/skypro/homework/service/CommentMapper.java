package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.model.Comment;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentMapper {

    public CommentDTO toCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setPk(comment.getCommentId());
        commentDTO.setAuthor(comment.getAuthor().getUserId());
        commentDTO.setAuthorFirstName(comment.getAuthor().getFirstName());
        commentDTO.setAuthorImage(comment.getAuthor().getAvatarFilePath());
        commentDTO.setText(comment.getCommentText());
        commentDTO.setCreatedAt(comment.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        return commentDTO;
    }

    public CommentsDTO toCommentsDTO(List<Comment> comments) {
        CommentsDTO commentsDTO = new CommentsDTO();
        commentsDTO.setCount(comments.size());
        commentsDTO.setResults(comments.stream().map(this::toCommentDTO).collect(Collectors.toList()));

        return commentsDTO;
    }
}
