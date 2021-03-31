package com.cos.costagram.config.oauth;

import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.costagram.config.auth.PrincipalDetails;
import com.cos.costagram.domain.user.RoleType;
import com.cos.costagram.domain.user.User;
import com.cos.costagram.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService{
	
	private final UserRepository userRepository;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("OAuth 로그인 진행 중.....");
		// 토큰을 받으면 구글에서 홍길동 대신 회원정보를 받아올 수 있음
		System.out.println("토큰 : "+userRequest.getAccessToken());
		
		// 1. AccessToken으로 회원정보를 받았다는 의미
		OAuth2User oAuth2User= super.loadUser(userRequest);
		
		
		System.out.println("oAuth2User : "+oAuth2User.getAttributes());

		
		return processOAuth2User(userRequest,oAuth2User);
	}
	
	// 파라미터 두 개를 받은 이유
	// userRequest -
	// oAuth2User - 
	// 구글 로그인 프로세스
	private OAuth2User processOAuth2User(OAuth2UserRequest userRequest,OAuth2User oAuth2User) {
		
		// 1번 통합 클래스를 생성 - 구글, 페이스북 등 서로 다른 이름으로 던짐
		System.out.println("뭐로 로그인 됐지?"+ userRequest.getClientRegistration().getClientName());
		
		OAuth2UserInfo oAuth2UserInfo=null;
		// 로그인을 구글에서 했다면!
		if(userRequest.getClientRegistration().getClientName().equals("Google")) {
			oAuth2UserInfo=new GoogleInfo(oAuth2User.getAttributes());
		}else if(userRequest.getClientRegistration().getClientName().equals("Facebook")) {
			oAuth2UserInfo=new FacebookInfo(oAuth2User.getAttributes());
		}
		
		// 2번 최초 : 회원가입+로그인 / 최초 x : 로그인 -> DB에서 유무 확인해서 최초인지 아닌지 확인
		User userEntity=userRepository.findByUsername(oAuth2UserInfo.getUsername());
		
		UUID uuid=UUID.randomUUID();
		String encPassword= new BCryptPasswordEncoder().encode(uuid.toString());
				
		if(userEntity==null) {
			User user=User.builder()
					.email(oAuth2UserInfo.getEmail())
					.password(encPassword)
					.username(oAuth2UserInfo.getUsername())
					.name(oAuth2UserInfo.getName())
					.role(RoleType.USER)
					.build();
			userEntity=userRepository.save(user);
			userRepository.save(userEntity);
			return new PrincipalDetails(userEntity,oAuth2User.getAttributes());
		}else{// 이미 회원가입이 완료됐다는 뜻
			return new PrincipalDetails(userEntity,oAuth2User.getAttributes());
		}
	}
}
