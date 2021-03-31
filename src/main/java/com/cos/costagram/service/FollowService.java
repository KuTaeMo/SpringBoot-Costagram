package com.cos.costagram.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.costagram.domain.follow.FollowRepository;
import com.cos.costagram.web.dto.follow.FollowRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FollowService {

	private final FollowRepository followRepository;
	private final EntityManager em;
	
	@Transactional(readOnly = true)
	public List<FollowRespDto> 팔로우리스트(int principalId, int pageUserId){
		
		StringBuffer sb=new StringBuffer();
		sb.append("select u.id userId, u.username, u.profileImageUrl, ");
		sb.append("if((select True from follow where fromUserId=? and toUserId=u.id),true,false) followState, "); //principalDetails.user.id
		sb.append("if(u.id=?,true,false) equalState "); //principalDetails.user.id
		sb.append("from follow f inner join user u on u.id= f.toUserId ");
		sb.append("where f.fromUserId=?");  // pageUserId
		
		Query query=em.createNativeQuery(sb.toString())
				.setParameter(1, principalId)
				.setParameter(2, principalId)
				.setParameter(3, pageUserId);
		
		JpaResultMapper result=new JpaResultMapper();
		List<FollowRespDto> followRespDtos=result.list(query,FollowRespDto.class);
		
		return followRespDtos;
	}
	
	@Transactional
	public int 팔로우(int fromUserId, int toUserId) {
		// 원래는 user 두개를 select해서 set해줘야 하는데 native를 쓰면 간단하게 끝남
		return followRepository.mFollow(fromUserId, toUserId);
	}
	
	@Transactional
	public int 언팔로우(int fromUserId, int toUserId) {
		return followRepository.mUnFollow(fromUserId, toUserId);
	}
}
