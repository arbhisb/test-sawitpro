package com.asb.playgroundJava11.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asb.playgroundJava11.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
   Optional<List<User>> findByPhoneNumberAndPassword(String phoneNumber, String password);
   Optional<User> findOneByPhoneNumber(String phoneNumber);
}
