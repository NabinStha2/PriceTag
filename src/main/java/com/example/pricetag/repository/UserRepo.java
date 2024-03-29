package com.example.pricetag.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pricetag.entity.User;
import com.example.pricetag.enums.AppUserRole;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findById(int id);

  User findByAppUserRole(AppUserRole appUserRole);

}
