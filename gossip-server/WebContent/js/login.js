//登录
var loginSubmit = function() {
		var username = document.getElementById('loginusername').value;
		var password = document.getElementById('loginpassword').value;
		var data = 'username=' + username + '&password=' + password;
		var url = '/Gossip-server/login?' + data;
		$.ajax({
			type: "POST",
			contentType: "application/json; charset=utf-8",
			//url: "json_example/login.json",
			url: url,
			dataType: "json",
			anysc: false,
			data: data,
			success: function(result) {

				var data = result;
				if(data.status == "true") {
					alert("登录成功!");
					//location.href=location.href+"?username="+data["info"].name;
					$('#user-button').empty();
					var name = "username"
					// deletecookie(name);
					// addcookie(name, data["info"].name);
					$.cookie(name, data.info.name, {
						expires: 1
					});
					// var tt = getcookie(name);
					var tt = $.cookie(name);

					//登录之后的样式
					var html = '<a id="usernamedis">' + tt + '</a>';
					html += '<a   onClick="loginexit()">' + "&nbsp;" + "&nbsp;" + '退出</a>';
					$('#user-button').append(html);
					/*var htmlother='<a >角色：'+data["info"].role+'</a>';
                    htmlother+='<a class="label" id="last_login_time">'+"&nbsp;"+"&nbsp;"+'上次登录时间：'+data["info"].last_login_time+'</a>';
                    htmlother+='<a class="label" id="last_login_ip">'+"&nbsp;"+"&nbsp;"+'上次登录IP：'+data["info"].last_login_ip+'</a>';*/
					var htmlother = '<p>角色:' + data["info"].role + "&nbsp;" + "&nbsp;" + '上次登录时间：' + data["info"].last_login_time + "&nbsp;" + "&nbsp;" + '上次登录IP：' + data["info"].last_login_ip + '</p>';
					$('#logininfo').append(htmlother);
					$('#loginModal').modal("hide");

				} else if(data.status == "false") {
					alert(data.info);
				}
			}
		});
	};
	//实现从登录界面转到注册界面

function toSignUp() {
	$('#loginModal').modal("hide");
	$('#signUpModal').modal("show");
}
//实现每次登录前的清理

function loginclear() {
	var tt = document.getElementById("loginbutton");
	var aa = tt.getAttributeNode("disabled");
	if(aa == null) {
		$('#loginbutton').attr("disabled", "disabled");
	}
	$('#logintishi').empty();
	$("#loginusername").val("");
	$("#loginpassword").val("");
}


function loginexit() {
	var name = "username";
	// deletecookie(name);
	$.cookie(name, null);
	$('#user-button').empty();
	$('#logininfo').empty();
	var html = ' <div id="login-popover-container" data-original-title=""><div class="btn-group"><a class="btn btn-success" href="#loginModal" data-toggle="modal" id="login-link" onClick="loginclear()">登录</a><a class="btn btn-success" href="#signUpModal" data-toggle="modal" id="login-link" onClick="signupclear()"> 注册</a></div></div> ';
	$('#user-button').append(html);

}
$(document).ready(function() {
	var name = "username";
	// var data = getcookie(name);
	var data = $.cookie(name);
	if(data != "''" && data != null) {
		$('#user-button').empty();
		var html = '<a id="usernamedis">' + data + '</a>';
		html += '<a   onClick="loginexit()">' + "&nbsp;" + "&nbsp;" + '退出</a>';
		$('#user-button').append(html);
	}

});

//检测登录用户名是否空

function loginNameCheck() {
	logindisabled();
	$('#logintishi').empty();
	var username = document.getElementById('loginusername').value;

	if(username == "") //对用户名是否为空进行验证
	{
		
		var html = '<div class="alert alert-error " style="margin-top:15px;">用户名不能为空！</div>';
		$('#logintishi').append(html);

		return false;
	}
	var errorUsername = "";
	switch(isUsername(username)) {

	case 1:
		{
			errorUsername = "用户名‘" + username + "’含有非法字符";
			//用户名不能以数字开头
			var html = '<div class="alert alert-error " style="margin-top:15px;">' + errorUsername + '</div>';
			$('#logintishi').append(html);

			return false;
		}
		/* case 2: {
            errorUsername = "用户名‘"+username+"’字符长度有误";
            //合法长度为6-20个字符
            return errorUsername ;
        }*/
	case 3:
		{
			errorUsername = "用户名‘" + username + "’含有非法字符";
			var html = '<div class="alert alert-error " style="margin-top:15px;">' + errorUsername + '</div>';
			$('#logintishi').append(html);

			return false;
		}
	case 4:
		{
			errorUsername = "用户名‘" + username + "’格式不正确!(3-20位且以字母开头)";
			//用户名只能包含_,英文字母，数字
			var html = '<div class="alert alert-error " style="margin-top:15px;">' + errorUsername + '</div>';
			$('#logintishi').append(html);
        	return false;
		}
		
	}
	logincheck();
}
//检测用户名的格式

function loginNameFormatCheck() {
    logindisabled();
    var username=document.getElementById('loginusername').value;
	var errorUsername = "";
	switch(isUsername(username)) {

	case 1:
		{
			return false;
		}
		/* case 2: {
            errorUsername = "用户名‘"+username+"’字符长度有误";
            //合法长度为6-20个字符
            return errorUsername ;
        }*/
	case 3:
		{
			return false;
		}
	case 4:
		{
			return false;
		}
	}
      return 1;
}
//检测登录密码是否为空

function loginPasswordCheck() {
	logindisabled();
	var password = document.getElementById('loginpassword').value;

	if(password == "") //对密码是否为空进行验证
	{
		$('#logintishi').empty();
		var html = '<div class="alert alert-error " style="margin-top:15px;">密码不能为空！</div>';
		$('#logintishi').append(html);

		return 0;
	}
	//return 1;
	$('#logintishi').empty();
	logincheck();

}

var logincheck = function() {
		if(loginform.loginusername.value == "") {

			return false;
		} else if(loginform.loginpassword.value == "") //对密码是否为空进行验证
		{

			return false;
		} else {
			if(loginNameFormatCheck()!=1)return false;
			$('#loginbutton').removeAttr("disabled");

		}


	}

function logindisabled() {
	var tt = document.getElementById("loginbutton");
	var aa = tt.getAttributeNode("disabled");
	if(aa == null) {
		$('#loginbutton').attr("disabled", "disabled");
	}
}
