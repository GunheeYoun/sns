package com.fastcampus.snsproject.controller;

import com.fastcampus.snsproject.controller.request.UserJoinRequest;
import com.fastcampus.snsproject.controller.request.UserLoginRequest;
import com.fastcampus.snsproject.exception.ErrorCode;
import com.fastcampus.snsproject.exception.SnsApplicationException;
import com.fastcampus.snsproject.fixture.PostEntityFixture;
import com.fastcampus.snsproject.model.Post;
import com.fastcampus.snsproject.model.User;
import com.fastcampus.snsproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc           // API 형태의 테스트
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
//    @Autowired
    private UserService userService;

    @Test
    public void 회원가입() throws Exception {
        String userName = "userNam";
        String password = "passwor";

        // TODO : mocking
        when(userService.join(eq(userName), eq(password)))
                .thenReturn(mock(User.class));
//        when(userService.join(eq(userName), eq(password)))
//                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        // TODO: add request body
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 회원가입시_이미_회원가입된_userName으로_회원가입을_하는경우_에러반환() throws Exception {
        String userName = "userName";
        String password = "password";

        // TODO : mocking
        when(userService.join(userName, password)).thenThrow(
                new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME)
        );


        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        // TODO: add request body
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName,password)))
                ).andDo(print())
                .andExpect(status().is(ErrorCode.DUPLICATED_USER_NAME.getStatus().value()));
    }

    @Test
    public void 로그인() throws Exception {
        String userName = "userName";
        String password = "password";

        // TODO : mocking
        when(userService.login(userName, password)).thenReturn("test_token");


        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        // TODO: add request body
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 로그인시_회원가입이_안된_userName을_입력할경우_에러반환() throws Exception {
        String userName = "userName";
        String password = "password";

        // TODO : mocking
        when(userService.login(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.USER_NOT_FOUND));


        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        // TODO: add request body
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password)))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void 로그인시_틀린_password를_입력할경우_에러반환() throws Exception {
        String userName = "userName";
        String password = "password";

        // TODO : mocking
        when(userService.login(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.INVALID_PASSWORD));


        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        // TODO: add request body
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void 알람기능() throws Exception {
        // TODO : 알람 mocking
        when(userService.alarmList(any(),any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/users/alarm")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void 알람리스트_요청시_로그인하지_않은경우() throws Exception {
        // TODO : 알람 mocking
        when(userService.alarmList(any(),any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/users/alarm")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
