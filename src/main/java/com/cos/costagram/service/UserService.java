package com.cos.costagram.service;



import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cos.costagram.config.auth.PrincipalDetails;
import com.cos.costagram.domain.follow.FollowRepository;
import com.cos.costagram.domain.image.Image;
import com.cos.costagram.domain.tag.Tag;
import com.cos.costagram.domain.user.User;
import com.cos.costagram.domain.user.UserRepository;
import com.cos.costagram.utils.TagUtils;
import com.cos.costagram.web.dto.image.ImageReqDto;
import com.cos.costagram.web.dto.user.UserProfileRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final FollowRepository followRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@Value("${file.path}")
	private String uploadFolder;
	
	@Transactional
	public User 회원사진변경(MultipartFile profileImageFile,PrincipalDetails principalDetails) {
		
		// 같은 이름의 이미지가 들어올 때 충돌을 방지하기 위해 uuid를 생성하고 이미지 이름 앞에 붙여줌
		UUID uuid=UUID.randomUUID(); 
		// 이미지 이름 뒤에 붙이면 확장자가 망가지기 때문에 앞에 붙인다.
		// 앞에 날짜까지 붙이면 충돌날 확률 0에 가까움!
		String imageFileName=uuid+"_"+profileImageFile.getOriginalFilename();
		System.out.println("파일명 : "+imageFileName);
		
		Path imageFilePath=Paths.get(uploadFolder+imageFileName);
		System.out.println("파일패스 : "+imageFilePath);
		
		try {
			Files.write(imageFilePath, profileImageFile.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		User userEntity=userRepository.findById(principalDetails.getUser().getId()).get();
		userEntity.setProfileImageUrl(imageFileName);
		
		return userEntity;
	}
	
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
