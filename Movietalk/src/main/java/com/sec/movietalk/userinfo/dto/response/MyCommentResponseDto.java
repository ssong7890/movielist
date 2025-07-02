package com.sec.movietalk.userinfo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyCommentResponseDto {

    private Long commentId;
    private Long reviewId;
    private String reviewNickName;
    private String content;
    private Integer likeCnt;
    private Integer dislikeCnt;


}
