package com.sec.movietalk.common.util;

import com.sec.movietalk.userinfo.security.CurrentUserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class UserUtil {

    public static Long extractUserId(Object principal) {

        if (principal instanceof CurrentUserDetails userDetails) {
            return userDetails.getUserId();
        } else if (principal instanceof OAuth2User oAuth2User) {
            Object idObj = oAuth2User.getAttribute("userId");
            if (idObj instanceof Long) return (Long) idObj;
            if (idObj instanceof Integer) return ((Integer) idObj).longValue();
            if (idObj instanceof String str) {
                try { return Long.valueOf(str); } catch (NumberFormatException ignore) {}
            }
        }
        return null;
    }

    public static String extractRole(Object principal) {
        if (principal instanceof CurrentUserDetails userDetails) {
            return userDetails.getRole();
        } else if (principal instanceof OAuth2User oAuth2User) {
            Object roleObj = oAuth2User.getAttribute("role");
            return (roleObj != null) ? roleObj.toString() : null;
        }
        return null;
    }

}
