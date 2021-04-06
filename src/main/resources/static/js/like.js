function likeOrUnLike(imageId) {
  let _buttonI = event.target;

  if (_buttonI.classList.contains("far")) {
  $.ajax({
		  type: "POST",
		  url: `/image/${imageId}/likes`,
		  dataType: "json"
	  }).done(res=>{
		  console.log(res);
		    _buttonI.classList.add("fas");
		    _buttonI.classList.add("active");
		    _buttonI.classList.remove("far");
		    
		    let likeCountStr  = $(`#like_count_${imageId}`).text();
		    let likeCount = Number(likeCountStr) + 1;
		    $(`#like_count_${imageId}`).text(likeCount);
	  });
    
  } else {
  
  	$.ajax({
		  type: "DELETE",
		  url: `/image/${imageId}/likes`,
		  dataType: "json"
	  }).done(res=>{
		  console.log(res);
		    _buttonI.classList.remove("fas");
		    _buttonI.classList.remove("active");
		    _buttonI.classList.add("far");
		    
		    let likeCountStr  = $(`#like_count_${imageId}`).text();
		    let likeCount = Number(likeCountStr) - 1;
		    $(`#like_count_${imageId}`).text(likeCount);
	  });
  }
}



