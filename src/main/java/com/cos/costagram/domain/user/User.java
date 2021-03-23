package com.cos.costagram.domain.user;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; 
	
	@Column(length=30,unique = true)
	private String username;
	
	@JsonIgnore
	private String password;
	
	private String name;
	private String website; // 자기 홈페이지
	private String bio; // 자기 소개
	private String email;
	private String phone;
	private String gender;
	
	private String profileImageUrl; // 경로로 저장할 것이기 때문에 String
	private String provider; // 제공자 OAuth - Google, Facebook, Naver etc...
	
	@Enumerated(EnumType.STRING)
	private RoleType role; // USER, ADMIN
	
	@CreationTimestamp
	private Timestamp createDate;
}
