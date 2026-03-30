package com.example.pricetag.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "otp")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Otp {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "otp_code", length = 4)
  private int otpCode;

  @CreationTimestamp
  @Column(updatable = false, name = "otp_generation_time")
  private LocalDateTime otpGenerationTime;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

}
