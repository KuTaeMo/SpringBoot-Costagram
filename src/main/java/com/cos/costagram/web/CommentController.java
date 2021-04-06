package com.cos.costagram.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cos.costagram.service.CommentService;
import com.cos.costagram.web.dto.CMRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class CommentController {
	
	private final CommentService commentService;
	
	@DeleteMapping("/comment/{id}")
	public CMRespDto<?> deleteById(@PathVariable int id){
		commentService.댓글삭제(id);
		return new CMRespDto<>(1,null);
	}
}
