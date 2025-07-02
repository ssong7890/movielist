package com.sec.movietalk.actor.repository;

import com.sec.movietalk.common.domain.comment.ActorComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorCommentRepository extends JpaRepository<ActorComment, Long> {
    List<ActorComment> findByActorIdAndIsDeletedFalseOrderByCreatedAtDesc(Long actorId);

}
