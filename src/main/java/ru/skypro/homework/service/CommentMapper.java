package ru.skypro.homework.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.model.AdUser;
import ru.skypro.homework.model.Comment;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentMapper {
    @Value("${path.to.user.avatar.file.url}")
    private String avatarFileURL;

    public CommentDTO toCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();

        AdUser author = comment.getAuthor();
        commentDTO.setPk(comment.getCommentId());
        commentDTO.setAuthor(author.getUserId());
        commentDTO.setAuthorFirstName(author.getFirstName());
        commentDTO.setAuthorImage(avatarFileURL + "/" + author.getUserId());
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
