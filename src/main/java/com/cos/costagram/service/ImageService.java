package com.cos.costagram.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cos.costagram.domain.image.Image;
import com.cos.costagram.domain.image.ImageRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {

	private final ImageRepository imageRepository;
	
	public List<Image> 피드이미지(int principalId){
		
		// 1. principalId로 내가 팔로우하고 있는 사용자를 찾아야 됨.
		// select toUserId from follow where fromUserId=로그인아이디;
		// select * from image where userId in (select toUserId from follow where fromUserId=3)
		
		return imageRepository.findFollowImage(principalId);
	}
}
