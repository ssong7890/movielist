package com.sec.movietalk.actor.controller;

import com.sec.movietalk.actor.dto.*;
import com.sec.movietalk.actor.repository.ActorCacheRepository;
import com.sec.movietalk.actor.service.ActorCacheService;
import com.sec.movietalk.common.domain.comment.ActorComment;
import com.sec.movietalk.actor.external.TmdbService;
import com.sec.movietalk.actor.service.ActorCommentService;
import com.sec.movietalk.client.TmdbClient;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sec.movietalk.common.util.UserUtil.extractUserId;

@Controller
@RequestMapping("/actors")
public class ActorController {


    private final TmdbClient tmdbClient; //TMDB API와 통신하기 위해 가져온 클라이언트 객체
    private final TmdbService tmdbService;

    private final ActorCommentService commentService;

    private final ActorCacheService cacheService;

    private final ActorCacheRepository actorCacheRepository;

    public ActorController(TmdbClient tmdbClient, TmdbService tmdbService, ActorCommentService commentService, ActorCacheService actorCacheService, ActorCacheRepository actorCacheRepository) {
        this.tmdbClient = tmdbClient;
        this.tmdbService = tmdbService;
        this.commentService = commentService;
        this.cacheService = actorCacheService;
        this.actorCacheRepository = actorCacheRepository;
    }

    // 배우 검색
    @GetMapping("/search")
    public String search(@RequestParam(value = "query", required = false) String query, Model model) {

        // 사용자가 입력한 검색어가 NUll이 아니고 공백이 아니면 TMDB API에 검색 요청
        if (query != null && !query.isBlank()) {
            List<ActorDto> actors = tmdbClient.searchActors(query);
            model.addAttribute("actors", actors);
            model.addAttribute("query", query);
        }

        return "actor/search";

    }

    // 배우 상세 정보
    @GetMapping("/{actorId}")
    public String actorDetail(@PathVariable int actorId, @RequestParam(defaultValue = "0") int page,Model model,
                              @RequestParam(defaultValue = "10") int size,
                              @AuthenticationPrincipal Object principal) {

        ActorDto actor = tmdbClient.getActorDetail(actorId);
        model.addAttribute("actor", actor);

        Page<ActorComment> comments = commentService.getComments((long) actorId, page, size);
        model.addAttribute("comments", comments);
        model.addAttribute("currentPage", comments.getNumber());
        model.addAttribute("totalPages", comments.getTotalPages());

        Long userId = extractUserId(principal);

        if (userId != null) {
            model.addAttribute("currentUserId", userId);
        }


        // 이미 저장된 배우인지 확인
        if (!actorCacheRepository.existsById((long) actorId)) {
            String profilePath = actor.getProfilePath();
            String fullProfileUrl = profilePath != null
                    ? "https://image.tmdb.org/t/p/w500" + profilePath
                    : null;

            ActorCache actorCache = ActorCache.builder()
                    .id((long) actorId) // 캐시에 저장할 actorId
                    .name(actor.getName())
                    .gender(actor.getGender())
                    .profilePath(fullProfileUrl)
                    .build();

            actorCacheRepository.save(actorCache);
        }


        return "actor/detail";
    }

    // 배우 필모그래피
    @GetMapping("/{id}/filmography")
    public String getFilmography(@PathVariable int id, Model model) {
        List<FilmographyDto> filmography = tmdbService.getFilmographyByPersonId((long) id);
        ActorDto actor = tmdbClient.getActorDetail(id); // 배우 정보

        model.addAttribute("filmography", filmography);
        model.addAttribute("actor", actor); // model에 담아주기
        return "actor/filmography"; // ← html 파일명
    }


    // 댓글기능
    @PostMapping("/{actorId}/comment")
    public String postComment(@PathVariable Long actorId,
                              @RequestParam String content,
                              @AuthenticationPrincipal Object principal) {

        Long userId = extractUserId(principal);

        commentService.addComment(userId, new ActorCommentRequest(actorId, content));
        return "redirect:/actors/" + actorId;
    }

    @PostMapping("/{actorId}/comment/{commentId}/edit")
    public String editComment(@PathVariable Long actorId,
                              @PathVariable Long commentId,
                              @RequestParam String content,
                              @AuthenticationPrincipal Object principal) {

        Long userId = extractUserId(principal);
        commentService.updateComment(commentId, content, userId);
        return "redirect:/actors/" + actorId;
    }

    @PostMapping("/{actorId}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable Long actorId,
                                @PathVariable Long commentId,
                                @AuthenticationPrincipal Object principal) {

        Long userId = extractUserId(principal);
        commentService.deleteComment(commentId, userId);
        return "redirect:/actors/" + actorId;
    }

}
