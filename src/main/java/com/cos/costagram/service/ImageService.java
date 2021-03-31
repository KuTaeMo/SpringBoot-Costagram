package com.cos.costagram.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.costagram.config.auth.PrincipalDetails;
import com.cos.costagram.domain.image.Image;
import com.cos.costagram.domain.image.ImageRepository;
import com.cos.costagram.domain.tag.Tag;
import com.cos.costagram.domain.tag.TagRepository;
import com.cos.costagram.utils.TagUtils;
import com.cos.costagram.web.dto.image.ImageReqDto;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class ImageService {

	private final ImageRepository imageRepository;
	private final TagRepository tagRepository;
	
	// yml에 저장한 경로를 가져옴
	@Value("${file.path}")
	private String uploadFolder;
	
	public List<Image> 피드이미지(int principalId){
		
		// 1. principalId로 내가 팔로우하고 있는 사용자를 찾아야 됨.
		// select toUserId from follow where fromUserId=로그인아이디;
		// select * from image where userId in (select toUserId from follow where fromUserId=3)
		
		List<Image> images=imageRepository.mFeed(principalId);
		
		// 좋아요 하트 색깔 로직 + 좋아요 카운트 로직
		images.forEach((image)->{
			
			int likeCount=image.getLikes().size();
			image.setLikeCount(likeCount);
			
			image.getLikes().forEach((like)->{
				if(like.getUser().getId()==principalId) {
					image.setLikeState(true);
				}
			});
		});
		
		return images;
	}
	
	@Transactional
	public void 사진업로드(ImageReqDto imageReqDto,PrincipalDetails principalDetails) {
		
		// 같은 이름의 이미지가 들어올 때 충돌을 방지하기 위해 uuid를 생성하고 이미지 이름 앞에 붙여줌
		UUID uuid=UUID.randomUUID(); 
		// 이미지 이름 뒤에 붙이면 확장자가 망가지기 때문에 앞에 붙인다.
		// 앞에 날짜까지 붙이면 충돌날 확률 0에 가까움!
		String imageFileName=uuid+"_"+imageReqDto.getFile().getOriginalFilename();
		System.out.println("파일명 : "+imageFileName);
		
		Path imageFilePath=Paths.get(uploadFolder+imageFileName);
		System.out.println("파일패스 : "+imageFilePath);
		
		try {
			Files.write(imageFilePath, imageReqDto.getFile().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 참고 : Image 엔티티에 Tag는 주인이 아니다. Image 엔티티로 통해서 Tag를 save할 수 없다.
		
		// 1. Image 저장
		Image image=imageReqDto.toEntity(imageFileName, principalDetails.getUser());
		Image imageEntity=imageRepository.save(image);
		
		// 2. Tag 저장 - utils에 알고리즘 만들기, 서비스에서 하지말기
		// imageEntity로 image와 연관관계 생성!
		List<Tag> tags=TagUtils.parsingToTagObject(imageReqDto.getTags(),imageEntity);
		tagRepository.saveAll(tags);
		
	}
}
