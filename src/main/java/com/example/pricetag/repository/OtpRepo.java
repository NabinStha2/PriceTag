package com.example.pricetag.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pricetag.entity.Otp;
import com.example.pricetag.entity.User;

@Repository
public interface OtpRepo extends JpaRepository<Otp, Long> {

  public Optional<Otp> findByUser(User user);

}
