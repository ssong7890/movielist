package com.sec.movietalk.home.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HomeCommentDto {

    private Long commentId;
    private Long reviewId;
    private String nickname;
    private String content;
    private Integer likeCount;

}
