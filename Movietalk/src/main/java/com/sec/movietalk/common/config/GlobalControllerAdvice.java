package com.sec.movietalk.common.config;

import com.sec.movietalk.common.util.UserUtil;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("role")
    public String globalRole(@AuthenticationPrincipal Object principal) {

        return UserUtil.extractRole(principal);
    }

}
