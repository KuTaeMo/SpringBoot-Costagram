// 구독 정보 보는 함수
document.querySelector("#subscribeBtn").onclick = (e) => {
  e.preventDefault();
  document.querySelector(".modal-follow").style.display = "flex";
  
  // ajax 통신 후에 json 리턴 -> javaScript 오브젝트 변경 => for문 돌면서 뿌리기
  
  let userId=$("#userId").val();
  
 $.ajax({
  url:`/user/${userId}/follow`,
 }).done((res)=>{
 
  $("#follow_list").empty();
  
  res.data.forEach((u)=>{
    console.log(2,u);
    let item=makeSubscribeInfo(u);
    $("#follow_list").append(item);
  })
 }).fail((error)=>{
  alert("오류 : "+error);
 });
};


function makeSubscribeInfo(u){
  let item=`<div class="follower__item" id="follow-${u.userId}">`;
  item+=`<div class="follower__img"><img src="/upload/${u.profileImageUrl}" alt=""  onerror="this.src='/images/person.png'"/></div>`;
  item+=`<div class="follower__text">`;
  item+=`<h2>${u.username}</h2>`;
  item+=`</div>`;
  
  
  if(!u.equalState){
    if(u.followState){
      item+=`<div class="follower__btn"><button class="cta blue" onclick="followOrUnFollow(${u.userId})">구독취소</button></div>`;
    }else{
      item+=`<div class="follower__btn"><button class="cta" onclick="followOrUnFollow(${u.userId})">구독하기</button></div>`;
    }
  }
  item+=`</div>`;
  return item;
}


function followOrUnFollow(userId){

	let text=$(`#follow-${userId} button`).text();
	
	if(text==="구독취소"){
	
		$.ajax({
		type:"delete",
		url:"/follow/"+userId,
		dataType:"json"
	}).done((res)=>{
		$(`#follow-${userId} button`).removeClass("blue");
		$(`#follow-${userId} button`).text("구독하기");
	});
	
	}else{
	
		$.ajax({
		type:"post",
		url:"/follow/"+userId,
		dataType:"json"
	}).done((res)=>{
		$(`#follow-${userId} button`).addClass("blue");
		$(`#follow-${userId} button`).text("구독취소");
	});
	
	}
}

function folOrUnFol(userId){

	let text=$(`#fButton`).text();
	
	if(text==="구독취소"){
	
		$.ajax({
		type:"delete",
		url:"/follow/"+userId,
		dataType:"json"
	}).done((res)=>{
		$(`#fButton`).removeClass("blue");
		$(`#fButton`).text("구독하기");
	});
	
	}else{
	
		$.ajax({
		type:"post",
		url:"/follow/"+userId,
		dataType:"json"
	}).done((res)=>{
		$(`#fButton`).addClass("blue");
		$(`#fButton`).text("구독취소");
	});
	
	}

}



function closeFollow() {
  document.querySelector(".modal-follow").style.display = "none";
}


document.querySelector(".modal-follow").addEventListener("click", (e) => {
  if (e.target.tagName !== "BUTTON") {
    document.querySelector(".modal-follow").style.display = "none";
  }
});
function popup(obj) {
  console.log(obj);
  document.querySelector(obj).style.display = "flex";
}
function closePopup(obj) {
  console.log(2);
  document.querySelector(obj).style.display = "none";
}
document.querySelector(".modal-info").addEventListener("click", (e) => {
  if (e.target.tagName === "DIV") {
    document.querySelector(".modal-info").style.display = "none";
  }
});
document.querySelector(".modal-image").addEventListener("click", (e) => {
  if (e.target.tagName === "DIV") {
    document.querySelector(".modal-image").style.display = "none";
  }
});

// 프로파일 수정 변경
function profileImageUpload(){

      let principalId = $("#principal-id").val();
      
      $("#profile-image_input").click();

      $("#profile-image_input").on("change", (e)=>{
         let files = e.target.files;
         let filesArr = Array.prototype.slice.call(files);
         filesArr.forEach((f)=>{
            if(!f.type.match("image.*")){
               alert("이미지를 등록해야 합니다.");
               return;
            }

            // 통신 시작
            let profileImageForm = $("#profile-image_form")[0];
            
            let formData = new FormData(profileImageForm); // Form태그 데이터 전송 타입을 multipart/form-data 로 만들어줌.

            $.ajax({
               type: "put",
               url: "/user/"+principalId+"/profileImageUrl",
               data: formData,
               contentType: false, //필수
               processData: false, //필수 : contentType을 false로 줬을 때 쿼리 스트링으로 자동 설정됨. 그거 해제 하는 법
               enctype:"multipart/form-data", //필수 아님
               dataType: "json"
            }).done(res=>{

               // 사진 전송 성공시 이미지 변경
               let reader = new FileReader();
               reader.onload = (e) => { 
                  $("#profile-image-url").attr("src", e.target.result);
               }
               reader.readAsDataURL(f); // 이 코드 실행시 reader.onload 실행됨.
            });

         });
      });
   }

// 회원정보수정
function update(userId){
        event.preventDefault();
		let data=$("#profile_setting").serialize();

		$.ajax({
			type:"put",
			url:"/user/"+userId,
			data:data,
			contentType:"application/x-www-form-urlencoded; charset=utf-8",
			dataType:"json"
			}).done((res)=>{
				location.href="/user/"+userId
				
			})
    }