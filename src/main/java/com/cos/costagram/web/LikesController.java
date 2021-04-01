package com.cos.costagram.web;

import org.springframework.web.bind.annotation.RestController;

import com.cos.costagram.domain.likes.LikesRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class LikesController {
	
	private final LikesRepository likesRepository;

	
}
