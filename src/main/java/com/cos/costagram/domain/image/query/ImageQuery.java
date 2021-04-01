package com.cos.costagram.domain.image.query;

public class ImageQuery {

	public static String explore() {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from image where id in ( ");
		sb.append("select imageId from ( ");
		sb.append("select imageId, count(imageId) likeCount from likes  ");
		sb.append("where userId = :principalId group by imageId order by likeCount desc) t) ");
		return sb.toString();
	}
}
