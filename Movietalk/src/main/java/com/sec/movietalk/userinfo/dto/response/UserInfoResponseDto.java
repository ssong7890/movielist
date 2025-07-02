package com.sec.movietalk.userinfo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDto {

    private Long userId;
    private String email;
    private String nickname;
    private Integer commentCnt;
    private Integer reviewCnt;

}
