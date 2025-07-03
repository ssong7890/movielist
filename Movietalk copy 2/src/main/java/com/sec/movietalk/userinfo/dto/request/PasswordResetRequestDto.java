package com.sec.movietalk.userinfo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequestDto {
    private String email;
    private String nickname;
    private String newPassword;
    private String newPasswordConfirm;
}