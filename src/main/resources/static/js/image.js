let page = 0;

let principalId = $("#principal-id").val();
let username = $("#principal-username").val();

function feedLoad() {
  // ajax로 Page<Image> 가져올 예정 (3개)
  $.ajax({
    type: "get",
    url: `/image?page=${page}`,
    dataType: "json",
  }).done((res) => {
    let images = res.data.content;
    images.forEach((image) => {
      let feedBox = feedItem(image);
      $("#feed_list").append(feedBox);
    });
  });
}

feedLoad();

$(window).scroll(() => {
  
//	console.log("scrollTop", $(window).scrollTop());
//	console.log("document height", $(document).height());
//	console.log("window height", $(window).height());
	
	let checkNum = $(window).scrollTop() - ($(document).height() - $(window).height());
	
	// 근사치 계산
	if(checkNum < 1 && checkNum > -1){
		page++;
		feedLoad();
	}
});

function feedItem(image) {
  let result = `
<!--전체 리스트 아이템-->
<div class="story-list__item">
	<!--리스트 아이템 헤더영역-->
	<div class="sl__item__header">
		<div><img src="/upload/${image.user.profileImageUrl}" alt=""  onerror="this.src='/images/person.jpeg'"/><svg viewbox="0 0 110 110"><circle cx="55" cy="55" r="53" /></svg></div>
		<div>${image.user.username}</div>
	</div>
	<!--헤더영역 end-->
	<!--게시물이미지 영역-->
	<div class="sl__item__img">
		<img src="/upload/${image.postImageUrl}" alt="" />
	</div>
	<!--게시물 내용 + 댓글 영역-->
	<div class="sl__item__contents">
		<!-- 하트모양 버튼 박스 -->
		<div class="sl__item__contents__icon"> `;

  if (image.likeState) {
    result += `<button onclick="likeOrUnLike(${image.id})">
							<i class="fas fa-heart active" id="like_icon_${image.id}"></i>
						</button>`;
  } else {
    result += `<button onclick="likeOrUnLike(${image.id})">
							<i class="far fa-heart" id="like_icon_${image.id}"></i>
						</button>`;
  }

  result += `	
		</div>
		<!-- 하트모양 버튼 박스 end -->
		<!--좋아요-->
		<span class="like"><b id="like_count_${image.id}">${image.likeCount}</b>likes</span>
		<!--좋아요end-->
		<!--태그박스-->
		<div class="sl__item__contents__tags">
			<p> `;

  image.tags.forEach((tag) => {
    result += `#${tag.name} `;
  });

  result += `			
			</p>
		</div>
		<!--태그박스end-->
		<!--게시글내용-->
		<div class="sl__item__contents__content">
			<p>${image.caption}</p>
		</div>
		<!--게시글내용end-->
		
		<!-- 댓글 들어오는 박스 -->
		<div id="comment-list-${image.id}">
		`;
  
  		image.comments.forEach((comment)=>{
  			result += `	<div class="sl__item__contents__comment" id="comment-${comment.id}"">
			    <p>
			      <b>${comment.user.username} :</b>
			      ${comment.content}
			    </p>
  				`;
  			
  			if(principalId == comment.user.id){
  	  			result +=`
  				    <button onClick="deleteComment(${comment.id})"><i class="fas fa-times"></i></button>
  				`;
  			}
  			
  			result += `
			  </div>`;
  		});
  
  		result += `
		</div>
		<!-- 댓글 들어오는 박스end -->
		<!--댓글입력박스-->
		<div class="sl__item__input">
			<input type="text" placeholder="댓글 달기..." id="comment-${image.id}"/>
			<button type="button" onClick="addComment(${image.id}, '${username}')">게시</button>
		</div>
		<!--댓글달기박스end-->
	</div>
</div>
<!--전체 리스트 아이템end-->
`;
  return result;
}


function addComment(imageId, username) {
	
	  let commentInput = $("#comment-"+imageId);
	  let commentList = $("#comment-list-"+imageId);

	  // 유저 아이디 필요하면 매개변수로 받아와서 넣으면 됨.
	  let _data = {
	    imageId: imageId,
	    commemtText: commentInput.val(),
	  };
	  if (_data.commemtText === "") {
	    alert("댓글을 작성해주세요!");
	    return;
	  }

	  // 통신 성공하면 아래 prepend 되야 되고 ID값 필요함
	  $.ajax({
		  type: "POST",
		  url: `/image/${imageId}/comment`,
		  data: _data.commemtText,
		  contentType: "plain/text; charset=utf-8",
		  dataType: "json"
	  }).done(res=>{
		  let comment = res.data;
		  let content = `
			  <div class="sl__item__contents__comment" id="comment-${comment.id}">
			    <p>
			      <b>${username} :</b>
			      ${_data.commemtText}
			    </p>
			    <button onClick="deleteComment(${comment.id})"><i class="fas fa-times"></i></button>
			  </div>
			  `;
			  commentList.prepend(content);
			  commentInput.val("");
	  });
	  
}

function deleteComment(commentId) {
  $.ajax({
	  type: "delete",
	  url: "/comment/"+commentId,
	  dataType: "json"
  }).done(res=>{
	  $("#comment-"+commentId).remove();
  });
}