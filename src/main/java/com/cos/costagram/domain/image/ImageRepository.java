package com.cos.costagram.domain.image;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository<Image, Integer>{
	
	@Query(value="select * from image where userId in (select toUserId from follow where fromUserId=:principalId)",nativeQuery = true)
	List<Image> findFollowImage(int principalId);
}