package com.asb.playgroundJava11;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import com.asb.playgroundJava11.model.User;
import com.asb.playgroundJava11.model.request.LoginRequest;
import com.asb.playgroundJava11.model.request.RegisterRequest;
import com.asb.playgroundJava11.repository.UserRepository;
import com.asb.playgroundJava11.service.impl.UserServiceImpl;
import com.asb.playgroundJava11.util.JwtTokenUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;

@ExtendWith(MockitoExtension.class)
class PlaygroundJava11ApplicationTests {

    @Mock
    Environment env;

    @Mock
    JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void registerUser_NormalInput_Success() {
        // when(userRepository.save(Mockito.any(User.class)))
        // .thenAnswer(i -> i.getArguments()[0]);

        lenient().when(this.env.getProperty("salt.key")).thenReturn("Dummy");


        RegisterRequest req = new RegisterRequest();
        req.setName("dummy");
        req.setPhoneNumber("08575555555");
        req.setPassword("Makan123");

        try {
            userServiceImpl.registerUser(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void loginUser_NormalInput_Success() throws Exception {
        User user = new User();
        user.setName("dummy");
        user.setPhoneNumber("0857552536212");
        user.setPassword("Makan123");
        Optional<List<User>> listUser = Optional.of(new ArrayList<User>());
        listUser.get().add(user);

        when(userRepository.findByPhoneNumberAndPassword(any(), any()))
        .thenReturn(listUser);

        lenient().when(this.env.getProperty("salt.key")).thenReturn("Dummy");

        when(jwtTokenUtil.doGenerateToken(any(), any()))
        .thenReturn("dummyJwt");

        LoginRequest req = new LoginRequest();
        req.setPhoneNumber("08575555555");
        req.setPassword("Makan123");

        try {
            String jwtDummy = userServiceImpl.loginUser(req);
            assertEquals("dummyJwt", jwtDummy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getName_NormalInput_Success() throws Exception {
        User user = new User();
        user.setId(1);
        user.setName("dummyName");
        user.setPhoneNumber("0857552536212");
        user.setPassword("Makan123");
        Optional<User> optionalUser = Optional.of(user);

        when(userRepository.findOneByPhoneNumber(any()))
        .thenReturn(optionalUser);

        when(jwtTokenUtil.validateToken(any()))
        .thenReturn(true);

        Claims claims = new DefaultClaims();
        claims.put("phoneNumber", "0857552536212");
        when(jwtTokenUtil.getAllClaimsFromToken(any()))
        .thenReturn(claims);

        try {
            String result = userServiceImpl.getName("dummy");
            assertEquals("dummyName", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void updateName_NormalInput_Success() throws Exception {
        User user = new User();
        user.setId(1);
        user.setName("dummyName");
        user.setPhoneNumber("0857552536212");
        user.setPassword("Makan123");
        Optional<User> optionalUser = Optional.of(user);

        when(userRepository.findOneByPhoneNumber(any()))
        .thenReturn(optionalUser);

        when(jwtTokenUtil.validateToken(any()))
        .thenReturn(true);

        Claims claims = new DefaultClaims();
        claims.put("phoneNumber", "0857552536212");
        when(jwtTokenUtil.getAllClaimsFromToken(any()))
        .thenReturn(claims);

        try {
            String result = userServiceImpl.updateName("dummy", "newName");
            assertEquals("Success", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
