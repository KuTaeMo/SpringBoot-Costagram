package com.cos.costagram.utils;

public class Script {
	//성공시
	public static String href(String msg, String url) {
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("alert('"+ msg +"');");
		sb.append("location.href='"+url+"';");
		sb.append("</script>");
		
		return sb.toString();
	}
	
	//오류시
	public static String back(String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("alert('"+ msg +"');");
		sb.append("history.back();");
		sb.append("</script>");
		
		return sb.toString();
	}
}
