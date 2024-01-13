package com.example.pricetag.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name = "file")
@NoArgsConstructor
@AllArgsConstructor
public class File {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "file_id")
  private Integer fileId;

  @Column(name = "file_name")
  private String fileName;

  @Column(name = "content_type")
  private String contentType;

  @Column(name = "file_size")
  private String fileSize;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Lob
  @Column(name = "file_data")
  private byte[] fileData;

}