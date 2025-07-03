package com.sec.movietalk.userinfo.service;

import com.sec.movietalk.common.domain.user.User;
import com.sec.movietalk.userinfo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println("attributes: " + oAuth2User.getAttributes());


        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // "naver", "google" 등
        String email = null;
        String nickname = null;

        if ("kakao".equals(registrationId)) {

            Map<String, Object> attributes = oAuth2User.getAttributes();
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            String name = (String) profile.get("nickname");
            if (name == null || name.isBlank()) {
                name = "Unknown";  // 이름이 없으면 기본값 할당
            }
            nickname = "KaKao-" + name;
            email = "kakao_" + name + "@example.com";
        }


        else if ("naver".equals(registrationId)) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            email = (String) response.get("email");
            String name = (String) response.get("name");
            if (name == null || name.isBlank()) {
                name = "Naver-Unknown";
            }
            nickname = "Naver-" + name;

        } else if ("google".equals(registrationId)) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            email = (String) attributes.get("email");
            String name = (String) attributes.get("name");
            if (name == null || name.isBlank()) {
                name = "Google-Unknown";
            }
            nickname = "Google-" + name;
        }


        User user = userRepository.findByEmail(email);

        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setNickname(nickname);
            user = userRepository.save(user);
        }

        // 커스텀 OAuth2User 리턴 (필요한 정보만 담기)
        Map<String, Object> customAttributes = Map.of(
                "email", user.getEmail(),
                "nickname", user.getNickname(),
                "userId", user.getId(),
                "role",user.getRole()
        );

        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                customAttributes,
                "email"
        );
    }

}

