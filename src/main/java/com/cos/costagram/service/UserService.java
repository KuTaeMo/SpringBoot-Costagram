package com.cos.costagram.service;



import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.costagram.domain.follow.FollowRepository;
import com.cos.costagram.domain.user.User;
import com.cos.costagram.domain.user.UserRepository;
import com.cos.costagram.web.dto.user.UserProfileRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final FollowRepository followRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Transactional(readOnly = true)
	public UserProfileRespDto 회원프로필(int userId,int principalId) {
		
		UserProfileRespDto userProfileRespDto=new UserProfileRespDto();
		
		User userEntity=userRepository.findById(userId).orElseThrow(()->{
			return new IllegalArgumentException();
		});
		
		int followState=followRepository.mFollowState(principalId, userId);
		int followCount=followRepository.mFollowCount(userId);
		
		userProfileRespDto.setFollowState(followState==1);
		userProfileRespDto.setFollowCount(followCount); // 내가 팔로우하고 있는 카운트
		userProfileRespDto.setImageCount(userEntity.getImages().size());
		userProfileRespDto.setUser(userEntity);
		
		userEntity.getImages().forEach((image)->{
			image.setLikeCount(image.getLikes().size());
		});
		
		return userProfileRespDto;
	}
	
	@Transactional
	public User 회원수정(int id,User user) {
		// username, password 수정불가
		User userEntity=userRepository.findById(id).get();
		userEntity.setName(user.getName());
		userEntity.setBio(user.getBio());
		userEntity.setWebsite(user.getWebsite());
		userEntity.setGender(user.getGender());
		
		String rawPassword=user.getPassword();
		String encPassword=bCryptPasswordEncoder.encode(rawPassword);
		userEntity.setPassword(encPassword);
		
		return userEntity;
	}
}
