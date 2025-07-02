package com.sec.movietalk.actor.dto;

public class ActorCommentRequest {
    private Long actorId;
    private String content;

    public ActorCommentRequest() {}

    public ActorCommentRequest(Long actorId, String content) {
        this.actorId = actorId;
        this.content = content;
    }

    public Long getActorId() {
        return actorId;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

