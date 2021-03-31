package com.cos.costagram.config.oauth;

import java.util.Map;

public abstract class OAuth2UserInfo {

	protected Map<String, Object> attributes;

	public OAuth2UserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	public Map<String, Object> getAttributes(){
		return attributes;
	}
	
	// 구글이던 페이스북이던 다 가져올 수 있게!
	public abstract String getUsername();
	public abstract String getId();
	public abstract String getName();
	public abstract String getEmail();
	public abstract String getImageUrl();
}
