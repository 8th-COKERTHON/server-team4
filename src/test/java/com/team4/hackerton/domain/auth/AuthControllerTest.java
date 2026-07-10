package com.team4.hackerton.domain.auth;

import com.team4.hackerton.domain.auth.dto.request.LoginRequest;
import com.team4.hackerton.domain.auth.dto.request.ReissueRequest;
import com.team4.hackerton.domain.auth.dto.request.SignUpRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 회원가입_로그인_보호된API_접근_흐름() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        setField(signUpRequest, "email", "test@team4.com");
        setField(signUpRequest, "password", "password123");
        setField(signUpRequest, "name", "테스터");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.email").value("test@team4.com"));

        // 중복 이메일 회원가입은 실패해야 한다
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("AUTH409_1"));

        LoginRequest loginRequest = new LoginRequest();
        setField(loginRequest, "email", "test@team4.com");
        setField(loginRequest, "password", "password123");

        // 토큰 없이 보호된 API 접근은 401
        mockMvc.perform(get("/api/example/1"))
                .andExpect(status().isUnauthorized());

        String loginResponseJson = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.accessToken").exists())
                .andReturn().getResponse().getContentAsString();

        String accessToken = objectMapper.readTree(loginResponseJson).get("result").get("accessToken").asString();
        String refreshToken = objectMapper.readTree(loginResponseJson).get("result").get("refreshToken").asString();

        // 발급받은 토큰으로 보호된 API 접근은 200
        mockMvc.perform(get("/api/example/1")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        // 토큰 없이 로그아웃 시도는 401
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isUnauthorized());

        // 로그아웃하면 리프레시 토큰이 무효화된다
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true));

        ReissueRequest reissueRequest = new ReissueRequest();
        setField(reissueRequest, "refreshToken", refreshToken);
        mockMvc.perform(post("/api/auth/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reissueRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("AUTH401_4"));

        // 잘못된 비밀번호는 401
        setField(loginRequest, "password", "wrong-password");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("AUTH401_1"));
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        var field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
