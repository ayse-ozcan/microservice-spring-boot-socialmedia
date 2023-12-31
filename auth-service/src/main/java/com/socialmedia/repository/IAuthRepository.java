package com.socialmedia.repository;

import com.socialmedia.repository.entity.Auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface IAuthRepository extends JpaRepository<Auth,Long> {
    Optional<Auth> findOptionalByEmailIgnoreCaseAndPassword(String email, String password);
    Optional<Auth> findOptionalByEmailIgnoreCase(String email);
}
