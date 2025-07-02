// com/sec/movietalk/review/dto/ReactionRequest.java
package com.sec.movietalk.review.dto;

public record ReactionRequest(
        String reactionType  // "like" or "dislike"
) {}
