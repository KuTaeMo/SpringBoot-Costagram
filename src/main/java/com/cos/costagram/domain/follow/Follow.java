package com.cos.costagram.domain.follow;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.cos.costagram.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Follow {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; 
	
	@JsonIgnoreProperties({"images"})
	@JoinColumn(name="fromUserId")
	@ManyToOne
	private User fromUser; // follow한 주체 ~~로부터 팔로워
	
	@JsonIgnoreProperties({"images"})
	@JoinColumn(name="toUserId")
	@ManyToOne
	private User toUser; // follow 된 사람 ~~를 팔로잉
	
	@CreationTimestamp
	private Timestamp createDate;
}
