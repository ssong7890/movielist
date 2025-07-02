package com.sec.movietalk.actor.service;

import com.sec.movietalk.actor.dto.ActorCommentRequest;
import com.sec.movietalk.common.domain.comment.ActorComment;
import com.sec.movietalk.actor.repository.ActorCommentRepository;
import com.sec.movietalk.common.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActorCommentService {

    private final ActorCommentRepository commentRepository;

    public void addComment(Long userId, ActorCommentRequest request) {
        // ID만 채운 더미 User 객체 생성
        User user = new User();
        user.setUserId(userId);

        ActorComment comment = new ActorComment();
        comment.setActorId(request.getActorId());
        comment.setUser(user); // 이렇게 User 객체 직접 넣기
        comment.setActorContent(request.getContent());

        commentRepository.save(comment);
    }

    public void updateComment(Long commentId, String newContent, Long userId) {
        ActorComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));
        if (!comment.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }
        comment.setActorContent(newContent);
        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, Long userId) {
        ActorComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));
        if (!comment.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }
        comment.setDeleted(true);
        commentRepository.save(comment);
    }

    public List<ActorComment> getComments(Long actorId) {
        return commentRepository.findByActorIdAndIsDeletedFalseOrderByCreatedAtDesc(actorId);
    }
}

