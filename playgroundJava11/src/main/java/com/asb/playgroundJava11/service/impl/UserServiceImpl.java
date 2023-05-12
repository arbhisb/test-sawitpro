package com.asb.playgroundJava11.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.asb.playgroundJava11.model.User;
import com.asb.playgroundJava11.model.request.LoginRequest;
import com.asb.playgroundJava11.model.request.RegisterRequest;
import com.asb.playgroundJava11.repository.UserRepository;
import com.asb.playgroundJava11.service.UserService;
import com.asb.playgroundJava11.util.HashHelper;
import com.asb.playgroundJava11.util.JwtTokenUtil;

import io.jsonwebtoken.Claims;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    Environment env;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Override
    public void registerUser(RegisterRequest registerRequest) throws Exception {
        User user = new User();
        user.setName(registerRequest.getName());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setPassword(HashHelper.hashString(registerRequest.getPassword().concat(env.getProperty("salt.key"))));
        userRepository.save(user);
    }

    @Override
    public String loginUser(LoginRequest loginRequest) throws Exception {
        Optional<List<User>> listUser = userRepository.findByPhoneNumberAndPassword(
            loginRequest.getPhoneNumber(), 
            HashHelper.hashString(loginRequest.getPassword().concat(env.getProperty("salt.key")))
            );
        if (listUser.isPresent() && !listUser.get().isEmpty()) {
            Map<String, Object> claims = new HashMap<>();
            String phoneNumber = listUser.get().get(0).getPhoneNumber();
            claims.put("phoneNumber", phoneNumber);
            return jwtTokenUtil.doGenerateToken(claims, phoneNumber);
        }
        return "User Not Found";
    }

    @Override
    public String getName(String bearerToken) throws Exception {
        if(!jwtTokenUtil.validateToken(jwtTokenUtil.removeBearer(bearerToken))){
            return "JWT invalid";
        }
        Claims claims = jwtTokenUtil.getAllClaimsFromToken(jwtTokenUtil.removeBearer(bearerToken));
        System.out.println((String) claims.get("phoneNumber"));
        Optional<User> user = userRepository.findOneByPhoneNumber((String) claims.get("phoneNumber"));
        if(user.isPresent() && user.get().getId() != 0) {
            return user.get().getName();
        }
        return "User Not Found";
    }

    @Override
    public String updateName(String bearerToken, String newName) throws Exception {
        if(!jwtTokenUtil.validateToken(jwtTokenUtil.removeBearer(bearerToken))){
            return "JWT invalid";
        }
        Claims claims = jwtTokenUtil.getAllClaimsFromToken(jwtTokenUtil.removeBearer(bearerToken));
        Optional<User> user = userRepository.findOneByPhoneNumber((String) claims.get("phoneNumber"));
        if(user.isPresent() && user.get().getId() != 0) {
            user.get().setName(newName);
            userRepository.save(user.get());
            return "Success";
        }
        return "Failed";
    }
    
}
