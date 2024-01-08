package com.example.pricetag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pricetag.entity.User;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String userName);

}