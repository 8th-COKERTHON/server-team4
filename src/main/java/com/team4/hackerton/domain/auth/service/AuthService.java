package com.team4.hackerton.domain.auth.service;

import com.team4.hackerton.domain.auth.code.AuthErrorCode;
import com.team4.hackerton.domain.auth.dto.request.LoginRequest;
import com.team4.hackerton.domain.auth.dto.request.ReissueRequest;
import com.team4.hackerton.domain.auth.dto.request.SignUpRequest;
import com.team4.hackerton.domain.auth.dto.response.SignUpResponse;
import com.team4.hackerton.domain.auth.dto.response.TokenResponse;
import com.team4.hackerton.domain.user.entity.User;
import com.team4.hackerton.domain.user.repository.UserRepository;
import com.team4.hackerton.global.apiPayload.exception.AppException;
import com.team4.hackerton.global.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private static final String GRANT_TYPE = "Bearer";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(AuthErrorCode.DUPLICATE_EMAIL);
        }

        User user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getName()
        );
        userRepository.save(user);

        return new SignUpResponse(user.getId(), user.getEmail(), user.getName());
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(AuthErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(AuthErrorCode.LOGIN_FAILED);
        }

        return issueTokens(user);
    }

    @Transactional
    public TokenResponse reissue(ReissueRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtProvider.isValid(refreshToken)) {
            throw new AppException(AuthErrorCode.INVALID_TOKEN);
        }
        if (jwtProvider.isExpired(refreshToken)) {
            throw new AppException(AuthErrorCode.EXPIRED_TOKEN);
        }

        String email = jwtProvider.getEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(AuthErrorCode.USER_NOT_FOUND));

        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new AppException(AuthErrorCode.TOKEN_MISMATCH);
        }

        return issueTokens(user);
    }

    private TokenResponse issueTokens(User user) {
        String accessToken = jwtProvider.generateAccessToken(user.getEmail());
        String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());

        user.updateRefreshToken(refreshToken);

        return new TokenResponse(GRANT_TYPE, accessToken, refreshToken);
    }
}
