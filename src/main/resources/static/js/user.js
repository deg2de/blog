let index = {
	init: function(){
		$("#btn-save").on("click", ()=>{ // function(){}을 사용하지 않고 ()=>{}를 사용한 이유는 this를 바인딩하기 위해서 사용
			this.save();
		});
		
		$("#btn-update").on("click", ()=>{ // function(){}을 사용하지 않고 ()=>{}를 사용한 이유는 this를 바인딩하기 위해서 사용
			this.update();
		});
	},
	
	save: function() {
		let data = {
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		}
		
		// ajax 호출시 default가 비동기 호출
		// ajax 통실을 이용해서 3개의 데이터를 json으로 변경하여 insert요청
		$.ajax({
			type: "POST",
			url: "/auth/joinProc",
			data: JSON.stringify(data), // http body데이터
			contentType: "application/json; charset=utf-8", // body데이터 타입
			dataType: "json" // 응답 타입 (json -> javascript)
		}).done(function(res){ // 통신 성공시
			if(res.status === 500){
				alert("회원가입에 실패하였습니다.");
			} else {
				alert("회원가입이 완료되었습니다.");
				location.href= "/";
			}
		}).fail(function(error){ // 통신 실패시
			alert(JSON.stringify(error));
		});
	},
	
	update: function() {
		let data = {
			id: $("#id").val(),
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		}
		
		// ajax 호출시 default가 비동기 호출
		// ajax 통실을 이용해서 3개의 데이터를 json으로 변경하여 insert요청
		$.ajax({
			type: "PUT",
			url: "/user",
			data: JSON.stringify(data), // http body데이터
			contentType: "application/json; charset=utf-8", // body데이터 타입
			dataType: "json" // 응답 타입 (json -> javascript)
		}).done(function(res){ // 통신 성공시
			alert("회원수정이 완료되었습니다.");
			// console.log(res);
			location.href= "/";
		}).fail(function(error){ // 통신 실패시
			alert(JSON.stringify(error));
		});
	}
}

index.init();