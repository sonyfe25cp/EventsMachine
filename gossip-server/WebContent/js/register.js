$(document).ready(function() {
	
	//检查邮箱
	$('#email').blur(function(){
		checkEmail();
	});
	
	//检查用户名
	$('#username').blur(function(){
		checkUsername();
	});
	
	//检查密码
	$('#password').blur(function(){
		checkPassword();
	});
	
	//检查repassword
	$('#repassword').blur(function(){
		checkRepassword();
	});
	
	$('#register').click(function(){
		if(checkEmail()&checkUsername()&checkPassword()&checkRepassword()){
			var username = $('#username').val();
			username = username.replace(/\s/g,"");
			var email = $('#email').val();
			email = email.replace(/\s/g,"");
			var password = $('#password').val();
			password = password.replace(/\s/g,"");
			var data = 'username=' + username + '&password=' + password + '&email=' + email;
			var url = '/register?' + data;
			$.ajax({
				   async: false,
			       type: "GET",
			       contentType: "application/json; charset=utf-8",
			       url:url,
			       dataType: "json",
			       success: function(result) {

			    	   if(result.status == "true") {
			    		 alert("注册成功！");
			    	   }
			       }
			  });
		}
	});//register
	
	$('#login').click(function(){
		window.location.href = "/user-login";
	});
	
});

function checkEmail()
{
	var email = $('#email').val();
	var flag = false;
	email = email.replace(/\s/g,"");
	if(email==null||email=="")
	{
		$('#email-tip').empty();
		$('#email-tip').css("color","red");
		$('#email-tip').html("邮箱不能为空！");
		return false;
	}
	else
	{
		reg=/^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/gi;
	    if(!reg.test(email))
	    {
	    	$('#email-tip').empty();
			$('#email-tip').css("color","red");
			$('#email-tip').html("非法的邮箱！");
	        return false;
	    }
	    else
	    {
			$.ajax({
			   async: false,
		       type: "GET",
		       contentType: "application/json; charset=utf-8",
		       url:"/verifyEmail?email=" + email,
		       dataType: "json",
		       success: function(result) {

		    	   if(result.status == "false") {
		    		   $('#email-tip').empty();
					   $('#email-tip').css("color","red");
					   $('#email-tip').html("该邮箱已注册！");
		    	   }
		    	   else{
		    		   $('#email-tip').empty();
					   $('#email-tip').css("color","green");
					   $('#email-tip').html("该邮箱可以使用！");
					   flag = true;
		    	   }
		       }
		  });
	    }
	}
	return flag;
}

function checkUsername(){
	var username = $('#username').val();
	var flag = false;
	username = username.replace(/\s/g,"");
	if(username==null||username=="")
	{
		$('#uname-tip').empty();
		$('#uname-tip').css("color","red");
		$('#uname-tip').html("用户名不能为空！");
		return false;
	}
	if(!isUsername(username)){
		$('#uname-tip').empty();
		$('#uname-tip').css("color","red");
		$('#uname-tip').html("用户名非法！");
		return false;
	}
	else{
		$.ajax({
			   async: false,
		       type: "GET",
		       contentType: "application/json; charset=utf-8",
		       url:"/verifyUsername?username=" + username,
		       dataType: "json",
		       success: function(result) {

		    	   if(result.status == "false") {
		    		    $('#uname-tip').empty();
		    			$('#uname-tip').css("color","red");
		    			$('#uname-tip').html("用户名已存在！");
		    	   }
		    	   else{
		    		   $('#uname-tip').empty();
					   $('#uname-tip').css("color","green");
					   $('#uname-tip').html("该用户名可以使用！");
					   flag = true;
		    	   }
		       }
		  });
	}//else
	return flag;
	
}//checkuser

//检测用户名的辅助函数

function isUsername( username ){
    if( /^\d.*$/.test( username ) ){
        return false;
    }
    if(! /^[\w_]*$/.test( username ) ){
        return false;
    }
    if(! /^([a-z]|[A-Z])[\w_]{5,19}$/.test( username ) ){
        return false;
    }
    return true;
}

function checkPassword(){
	var password = $('#password').val();
	var flag = false;
	password = password.replace(/\s/g,"");
	if(password==null||password=="")
	{
		$('#pwd-tip').empty();
		$('#pwd-tip').css("color","red");
		$('#pwd-tip').html("密码不能为空！");
		return false;
	}
    if(! /^[\w_]{5,19}$/.test( password ) ){
    	$('#pwd-tip').empty();
		$('#pwd-tip').css("color","red");
		$('#pwd-tip').html("密码不合法！");
        return false;
    }
    $('#pwd-tip').empty();
    return true;
}

function checkRepassword(){
	var repassword = $('#repassword').val();
	var password = $('#password').val();
	password = password.replace(/\s/g,"");
	repassword = repassword.replace(/\s/g,"");
	if(password!=repassword){
    	$('#repwd-tip').empty();
		$('#repwd-tip').css("color","red");
		$('#repwd-tip').html("两次密码不一致！");
        return false;
	}
	$('#repwd-tip').empty();
	return true;
}