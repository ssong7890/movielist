package com.sec.movietalk.userinfo.service;

import com.sec.movietalk.common.domain.user.User;
import com.sec.movietalk.userinfo.dto.request.PasswordResetRequestDto;
import com.sec.movietalk.userinfo.dto.request.SignupRequestDto;
import com.sec.movietalk.userinfo.dto.response.UserInfoResponseDto;
import com.sec.movietalk.userinfo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(SignupRequestDto dto) {

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 가입한 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setNickname(dto.getNickname());
        user.setPassword(encodedPassword);

        userRepository.save(user); // 비밀번호 암호화는 추후에 추가 가능
    }

    public void resetPassword(PasswordResetRequestDto dto) {
        // 1. email, nickname 일치 회원 찾기
        User user = userRepository.findByEmailAndNickname(dto.getEmail(), dto.getNickname())
                .orElseThrow(() -> new IllegalArgumentException("닉네임과 이메일이 정보와 일치하는 회원이 없습니다."));

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("소셜 로그인 회원입니다 !");
        }


        // 2. 새 비밀번호, 확인값 일치 체크
        if (!dto.getNewPassword().equals(dto.getNewPasswordConfirm())) {
            throw new IllegalArgumentException("새 비밀번호와 확인값이 일치하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());

        // 4. 비밀번호 변경
        user.setPassword(encodedPassword); // 보통 여기서 암호화 필요 (예: BCrypt)
        userRepository.save(user);
    }

    public UserInfoResponseDto getUserInfo(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return new UserInfoResponseDto(

                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getCommentCnt() != null ? user.getCommentCnt() : 0,
                user.getReviewCnt() != null ? user.getReviewCnt() : 0

        );
    }

}
