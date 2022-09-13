let index = {
	init: function(){
		$("#btn-save").on("click", ()=>{
			this.save();
		});
		
		$("#btn-update").on("click", ()=>{
			this.update();
		});
		
		$("#btn-delete").on("click", ()=>{
			this.deleteById();
		});
		
		$("#btn-reply-save").on("click", ()=>{
			this.replySave();
		});
	},
	
	save: function() {
		let data = {
			title: $("#title").val(),
			content: $("#content").val()
		}
		
		$.ajax({
			type: "POST",
			url: "/api/board",
			data: JSON.stringify(data), // http body데이터
			contentType: "application/json; charset=utf-8", // body데이터 타입
			dataType: "json" // 응답 타입 (json -> javascript)
		}).done(function(res){ // 통신 성공시
			alert("글쓰기가 완료되었습니다.");
			console.log(res);
			location.href= "/";
		}).fail(function(error){ // 통신 실패시
			alert(JSON.stringify(error));
		});
	},
	
	update: function() {
		
		let id = $("#id").val();
		
		let data = {
			title: $("#title").val(),
			content: $("#content").val()
		}
		
		$.ajax({
			type: "PUT",
			url: "/api/board/"+id,
			data: JSON.stringify(data), // http body데이터
			contentType: "application/json; charset=utf-8", // body데이터 타입
			dataType: "json" // 응답 타입 (json -> javascript)
		}).done(function(res){ // 통신 성공시
			alert("글수정이 완료되었습니다.");
			console.log(res);
			location.href= "/";
		}).fail(function(error){ // 통신 실패시
			alert(JSON.stringify(error));
		});
	},
	
	deleteById: function() {
		let id = $("#id").text();
		
		$.ajax({
			type: "DELETE",
			url: "/api/board/"+id,
			dataType: "json" // 응답 타입 (json -> javascript)
		}).done(function(res){ // 통신 성공시
			alert("삭제가 완료되었습니다.");
			console.log(res);
			location.href= "/";
		}).fail(function(error){ // 통신 실패시
			alert(JSON.stringify(error));
		});
	},
	
	replySave: function() {
		let data = {
			userId: $("#userId").val(),
			boardId: $("#boardId").val(),
			content: $("#reply-content").val()
		};
		
		$.ajax({
			type: "POST",
			url: `/api/board/${data.boardId}/reply`,
			data: JSON.stringify(data), // http body데이터
			contentType: "application/json; charset=utf-8", // body데이터 타입
			dataType: "json" // 응답 타입 (json -> javascript)
		}).done(function(res){ // 통신 성공시
			alert("댓글 작성이 완료되었습니다.");
			console.log(res);
			location.href= `/board/${data.boardId}`;
		}).fail(function(error){ // 통신 실패시
			alert(JSON.stringify(error));
		});
	},
	
	replyDelete: function(boardId, replyId) {
		alert(replyId);
		
		$.ajax({
			type: "DELETE",
			url: `/api/board/${boardId}/reply/${replyId}`,
			dataType: "json" // 응답 타입 (json -> javascript)
		}).done(function(res){ // 통신 성공시
			alert("댓글이 삭제되었습니다.");
			location.href= `/board/${boardId}`;
		}).fail(function(error){ // 통신 실패시
			alert(JSON.stringify(error));
		});
	}
}

index.init();