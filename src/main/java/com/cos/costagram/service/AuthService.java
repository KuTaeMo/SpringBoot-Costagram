package com.cos.costagram.service;

import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cos.costagram.domain.user.RoleType;
import com.cos.costagram.domain.user.User;
import com.cos.costagram.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoderEncoder;

	@Transactional
	public void 회원가입(User user) {
		String rawPassword=user.getPassword();
		String encPassword=bCryptPasswordEncoderEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		
		user.setRole(RoleType.USER);
		userRepository.save(user);
	}
}
