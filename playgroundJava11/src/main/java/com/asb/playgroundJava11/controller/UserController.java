package com.asb.playgroundJava11.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asb.playgroundJava11.model.User;
import com.asb.playgroundJava11.model.request.LoginRequest;
import com.asb.playgroundJava11.model.request.RegisterRequest;
import com.asb.playgroundJava11.model.request.UpdateNameRequest;
import com.asb.playgroundJava11.repository.UserRepository;
import com.asb.playgroundJava11.service.UserService;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    Environment env;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable final Long id) {
        return ResponseEntity.ok(env.getProperty("salt.key"));
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAll() {
        List<User> listUser = userRepository.findAll();
        return ResponseEntity.ok(listUser);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest registerRequest) throws Exception {
        userService.registerUser(registerRequest);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @GetMapping("/getname")
    public ResponseEntity<String> getName(@RequestHeader String authorization) throws Exception{
        return ResponseEntity.ok(userService.getName(authorization));
    }

    @PutMapping("/updateName")
    public ResponseEntity<String> updateName(@RequestHeader String authorization, @Valid @RequestBody UpdateNameRequest updateNameRequest) throws Exception{
        return ResponseEntity.ok(userService.updateName(authorization, updateNameRequest.getName()));
    }
}