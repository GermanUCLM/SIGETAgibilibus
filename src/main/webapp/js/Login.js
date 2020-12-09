$("#login").click(function(){
        var username = $("#username").val();
        var pass=$("#pass").val();

        if(username.length==0 || pass.length==0){
            alert("Rellena todos los campos");
        }
    });
    
    function login() {
		var info = {
			type : "Login",
			userName : $("#username").val(),
			pwd : $("#pass").val()
		};

		var data = {
			data : JSON.stringify(info),
			url : "login",
			type : "post",
			contentType : 'application/json',
			success : function() {
				sessionStorage.setItem("id", $("#username").val());
				window.location.href = "UserCalendar.html";
			},
			error : function(response) {
				alert("Contraseña y/o usuario incorrectos");
				pass = document.getElementById("pass");
				username = document.getElementById("username");
				pass.style.border = "2px solid red";
				username.style.border = "2px solid red";
			}
		};
		$.ajax(data);
	}