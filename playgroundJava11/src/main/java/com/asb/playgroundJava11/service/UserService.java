package com.asb.playgroundJava11.service;

import com.asb.playgroundJava11.model.request.LoginRequest;
import com.asb.playgroundJava11.model.request.RegisterRequest;

public interface UserService {
    void registerUser(RegisterRequest registerRequest) throws Exception;
    String loginUser(LoginRequest loginRequest) throws Exception;
    String getName(String bearerToken) throws Exception;
    String updateName(String bearerToken, String newName) throws Exception;
}
