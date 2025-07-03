package com.sec.movietalk.popular.dto;

import lombok.*;

/**
 * 영화 카드 하단에 노출될 배우 요약 정보
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActorChip {

    private int actorId;
    private String name;
}
