
function UserNameCheck(){
	var username = trimStr($('#username').val());
	//alert(username);
	if(username == "") //对用户名是否为空进行验证
	{
		$('#signuptishi').empty();
		var html = '<div class="alert alert-error " style="margin-top:15px;">用户名不能为空！</div>';
		$('#signuptishi').append(html);
		return false;
	}
	var errorUsername="";
	switch( isUsername( username ) ){
        
        case 1: {
            errorUsername = "用户名必须以字母开头！";
            //用户名不能以数字开头
            $('#signuptishi').empty();
			var html = '<div class="alert alert-error " style="margin-top:15px;">'+errorUsername+'</div>';
			$('#signuptishi').append(html);

			return false;
        }
        case 3: {
            errorUsername = "用户名： ‘"+username+"’ 含有非法字符";
            $('#signuptishi').empty();
			var html = '<div class="alert alert-error " style="margin-top:15px;">'+errorUsername+'</div>';
			$('#signuptishi').append(html);

			return false;
        }
        case 4: {
            errorUsername = "用户名： ‘"+username+"’ 格式不正确!(3-20位且以字母开头)";
            //用户名只能包含_,英文字母，数字
            $('#signuptishi').empty();
			var html = '<div class="alert alert-error " style="margin-top:15px;">'+errorUsername+'</div>';
			$('#signuptishi').append(html);

			return false;
        }
    }
     
	var url = "/verifyUsername?username=" + username;
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url: url,
		dataType: "json",
		anysc: false,
		success: function(result)
		{
			var data = result;
			if(data.status == "false") 
			{
			
				$('#signuptishi').empty();
				var html = '<div class="alert alert-error " style="margin-top:15px;">' + data.info + '</div>';
				$('#signuptishi').append(html);
				return false;
			} else 
			{
				$('#signuptishi').empty();
				var html = '<div class="alert alert-error " style="margin-top:15px;">恭喜您，用户名可以使用！</div>';
				$('#signuptishi').append(html);
			}
		}//sucess
	});
    return 1;
}


//检测用户名的辅助函数

function isUsername( username ){
    if( /^\d.*$/.test( username ) ){
        return 1;
    }
   /* if(! /^.{6,20}$/.test( username ) ){
        return 2;
    }*/
    if(! /^[\w_]*$/.test( username ) ){
        return 3;
    }
    if(! /^([a-z]|[A-Z])[\w_]{2,19}$/.test( username ) ){
        return 4;
    }
    return 0;
}





//对密码输入进行检测


function PasswordCheck() {
	var password = $('#password').val();

	if(password == "") //对用户名是否为空进行验证
	{
		$('#signuptishi').empty();
		var html = '<div class="alert alert-error " style="margin-top:15px;">请输入密码！</div>';
		$('#signuptishi').append(html);

		return false;
	} else {
		$('#signuptishi').empty();
	}

}
//对再次输入密码进行检测

function RepasswordCheck() {
	var repassword = $('#repassword').val();
	var password = $('#password').val();
	if(password != repassword) //对用户名是否为空进行验证
	{
		$('#signuptishi').empty();
		var html = '<div class="alert alert-error " style="margin-top:15px;">两次输入的密码不一致！</div>';
		$('#signuptishi').append(html);

		return false;
	} else {
		$('#signuptishi').empty();

	}

}

function EmailCheck() {
	var email = document.getElementById('email').value;
	var url = "/verifyEmail?email=" + email;
	if(signupform.email.value == "") //对用户名是否为空进行验证
	{
		$('#signuptishi').empty();
		var html = '<div class="alert alert-error " style="margin-top:15px;">email不可以为空！</div>';
		$('#signuptishi').append(html);
		return false;
	}else{
		reg=/^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/gi;
	    if(!reg.test(signupform.email.value))
	    {
	        $('#signuptishi').empty();
			var html = '<div class="alert alert-error " style="margin-top:15px;">非法的email！</div>';
			$('#signuptishi').append(html);
	        return false;
	    }

	}	
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url: url,
		dataType: "json",
		anysc: false,
		success: function(result) {
			var data = result;
			if(data.status == "false") {
				{
					$('#signuptishi').empty();
					var html = '<div class="alert alert-error " style="margin-top:15px;">' + data.info + '</div>';
					$('#signuptishi').append(html);
				}
			}
		}
	});
}

 function Form_Submit() {
	 EmailCheck();
	 alert("submit");
	 var username = document.getElementById('username').value;
	 var password = document.getElementById('password').value;
	 var email = document.getElementById('email').value;
	 var data = 'username=' + username + '&password=' + password + '&email=' + email;
	 var url = '/register?' + data;
	 $.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
			//url: "json_example/logontrue.json",
		url: url,
		dataType: "json",
		anysc: false,
		data: data,
		success: function(result)
		{
			var data = result;
			if(data.status == "true") 
			{
				alert("注册成功");
				$('#signUpModal').modal("hide");
			} else if(data.status == "false") 
			{
				alert(data.info);
			}
		}
		});
	}

