package com.cos.costagram.service;



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
	
	@Transactional(readOnly = true)
	public UserProfileRespDto 회원프로필(int userId,int principalId) {
		
		UserProfileRespDto userProfileRespDto=new UserProfileRespDto();
		
		User userEntity=userRepository.findById(userId).orElseThrow(()->{
			return new IllegalArgumentException();
		});
		
		int followState=followRepository.mFollowState(principalId, userId);
		int followCount=followRepository.mFollowCount(userId);
		
		userProfileRespDto.setFollowState(true);
		userProfileRespDto.setFollowCount(followCount); // 내가 팔로우하고 있는 카운트
		userProfileRespDto.setImageCount(userEntity.getImages().size());
		userProfileRespDto.setUser(userEntity);
		
		return userProfileRespDto;
	}
}
