package com.example.pricetag.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pricetag.entity.RefreshToken;
import com.example.pricetag.entity.User;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Integer> {

  Optional<RefreshToken> findByToken(String token);

  Optional<RefreshToken> findByUser(User user);

}
