//注册

var check = function() {
		if(signupform.username.value == "") {
			//signupdisabled();
			return false;
		} else if(signupform.password.value == "") //对密码是否为空进行验证
		{
			//signupdisabled();
			return false;
		} else if(signupform.password.value != signupform.repassword.value) //验证两次输入的密码是否匹配
		{
			//signupdisabled();
			return false;
		} else if(signupform.email.value == ""){
			//signupdisabled();
			return false;
		} else {
			if(UserNameCheck1()!=1)return false;
			if(EmailCheck1()!=1)return false;
			$('#signupbutton').removeAttr("disabled");
			return true;
		}

	}

function UserNameCheck(){
	signupdisabled();
	var username = document.getElementById('username').value;
	var url = "/gossip-server/verifyUsername?username=" + escape(username);
	if(signupform.username.value == "") //对用户名是否为空进行验证
	{
		$('#signuptishi').empty();
		var html = '<div class="alert alert-error " style="margin-top:15px;">用户名不能为空！</div>';
		$('#signuptishi').append(html);

		return false;
	}
	var errorUsername="";
	switch( isUsername( username ) ){
        
        case 1: {
            errorUsername = "用户名‘"+username+"’含有非法字符";
            //用户名不能以数字开头
            $('#signuptishi').empty();
			var html = '<div class="alert alert-error " style="margin-top:15px;">'+errorUsername+'</div>';
			$('#signuptishi').append(html);

			return false;
        }
       /* case 2: {
            errorUsername = "用户名‘"+username+"’字符长度有误";
            //合法长度为6-20个字符
            return errorUsername ;
        }*/
        case 3: {
            errorUsername = "用户名‘"+username+"’含有非法字符";
            $('#signuptishi').empty();
			var html = '<div class="alert alert-error " style="margin-top:15px;">'+errorUsername+'</div>';
			$('#signuptishi').append(html);

			return false;
        }
        case 4: {
            errorUsername = "用户名‘"+username+"’格式不正确!(3-20位且以字母开头)";
            //用户名只能包含_,英文字母，数字
            $('#signuptishi').empty();
			var html = '<div class="alert alert-error " style="margin-top:15px;">'+errorUsername+'</div>';
			$('#signuptishi').append(html);

			return false;
        }
    }
     
	var data = {classCode: "0001"}; // 这里要直接使用JOSN对象 	 
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		//url: "json_example/logontrue.json",
		url: url,
		dataType: "json",
		anysc: false,
		data: data,
		success: function(result) {
			var data = result;
			if(data.status == "false") {
			
				$('#signuptishi').empty();
				var html = '<div class="alert alert-error " style="margin-top:15px;">' + data.info + '</div>';
				$('#signuptishi').append(html);
				return false;
			} else {
				$('#signuptishi').empty();
				var html = '<div class="alert alert-error " style="margin-top:15px;">恭喜您，用户名可以使用！</div>';
				$('#signuptishi').append(html);
				check();
			}
		}
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



function EmailCheck() {
	signupdisabled();
	var email = document.getElementById('email').value;
	var url = "/gossip-server/verifyEmail?email=" + email;
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
	var data = {
		classCode: "0001"
	}; // 这里要直接使用JOSN对象 	
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		//url: "json_example/logontrue.json",
		url: url,
		dataType: "json",
		anysc: false,
		data: data,
		success: function(result) {
			var data = result;
			/* 	if(data.status=="true")
       	{alert("email可以使用");}
       	else*/
			if(data.status == "false") {
				{
					$('#signuptishi').empty();
					var html = '<div class="alert alert-error " style="margin-top:15px;">' + data.info + '</div>';
					$('#signuptishi').append(html);
				}
			} else {
				$('#signuptishi').empty();
				var html = '<div class="alert alert-error " style="margin-top:15px;">恭喜您，email可以使用！</div>';
				$('#signuptishi').append(html);
				check();
			}
		}
	});
    return 1;
}

//对密码输入进行检测


function PasswordCheck() {
	signupdisabled();
	var password = document.getElementById('password').value;

	if(signupform.password.value == "") //对用户名是否为空进行验证
	{
		$('#signuptishi').empty();
		var html = '<div class="alert alert-error " style="margin-top:15px;">请输入密码！</div>';
		$('#signuptishi').append(html);

		return false;
	} else {
		$('#signuptishi').empty();
		check();
	}

}
//对再次输入密码进行检测

function RepasswordCheck() {
	signupdisabled();
	var repassword = document.getElementById('repassword').value;

	if(signupform.repassword.value != signupform.password.value) //对用户名是否为空进行验证
	{
		$('#signuptishi').empty();
		var html = '<div class="alert alert-error " style="margin-top:15px;">两次输入的密码不一致！</div>';
		$('#signuptishi').append(html);

		return false;
	} else {
		$('#signuptishi').empty();
		check();

	}

}
// 提交时候的前台检测

//function Form_Submit() {
	/*if(signupform.password.value == "") //对密码是否为空进行验证
	{
		var html = '<div class="alert alert-error " style="margin-top:15px;">密码不能为空！</div>';
		$('#signuptishi').append(html);
		return false;
	} else if(signupform.password.value != signupform.repassword.value) //验证两次输入的密码是否匹配
	{
		var html = '<div class="alert alert-error " style="margin-top:15px;">两次输入的密码不一致！</div>';
		$('#signuptishi').append(html);
		return false;
	}*/
	//UserNameCheck();
	//EmailCheck();
//	submit(); //提交表单
//}

//我的
/*
1.用户注册
 接口：/register
 说明：注册用户
 参数：username={user}&&password={123132}&email={123@bit.edu.cn}
 返回值：

{
    "status":"true",//註冊成功
}
{
    "status":"false",//註冊失敗
}

*/
 function Form_Submit() {

		var username = document.getElementById('username').value;
		var password = document.getElementById('password').value;
		var email = document.getElementById('email').value;
		var data = 'username=' + username + '&password=' + password + '&email=' + email;
		var url = '/gossip-server/register?' + data;
		/*	$.post(
	"json_example/logontrue.json",
	data,
	function(result){
		var tt=result;
		alert(result.status);
		alert(1);
		alert(tt.status);
		if(tt.status=="true")
		alert("注册成功");
		else if(tt.status="false")
			alert("注册失败");
		}
	);*/
		$.ajax({
			type: "POST",
			contentType: "application/json; charset=utf-8",
			//url: "json_example/logontrue.json",
			url: url,
			dataType: "json",
			anysc: false,
			data: data,
			success: function(result) {
				var data = result;
				if(data.status == "true") {
					alert("注册成功");
					$('#signUpModal').modal("hide");
				} else if(data.status == "false") {
					alert(data.info);
				}
			}
		});
	}

function signupclear() {
		signupdisabled();
	$('#signuptishi').empty();
	var username = document.getElementById('username');
	var password = document.getElementById('password');
	var repassword = document.getElementById('repassword');
	var email = document.getElementById('email');
	username.value = "";
	password.value = "";
	repassword.value = "";
	email.value = "";
}


function signupdisabled(){
	var tt=document.getElementById("signupbutton");
	var aa=tt.getAttributeNode("disabled");
	if(aa==null){
		$('#signupbutton').attr("disabled","disabled");
	}
}
//在放开注册按钮之前检测名字是否可以使用
function UserNameCheck1(){
	signupdisabled();
	var username = document.getElementById('username').value;
	var url = "/gossip-server/verifyUsername?username=" + escape(username);
	if(signupform.username.value == "") //对用户名是否为空进行验证
	{
		//$('#signuptishi').empty();
		//var html = '<div class="alert alert-error " style="margin-top:15px;">用户名不能为空！</div>';
		//$('#signuptishi').append(html);

		return false;
	}
	var errorUsername="";
	switch( isUsername( username ) ){
        
        case 1: {
           // errorUsername = "用户名‘"+username+"’含有非法字符";
            //用户名不能以数字开头
            //$('#signuptishi').empty();
			//var html = '<div class="alert alert-error " style="margin-top:15px;">'+errorUsername+'</div>';
			//////$('#signuptishi').append(html);

			return false;
        }
       /* case 2: {
            errorUsername = "用户名‘"+username+"’字符长度有误";
            //合法长度为6-20个字符
            return errorUsername ;
        }*/
        case 3: {
            ////errorUsername = "用户名‘"+username+"’含有非法字符";
            ////$('#signuptishi').empty();
			////var html = '<div class="alert alert-error " style="margin-top:15px;">'+errorUsername+'</div>';
			////$('#signuptishi').append(html);

			return false;
        }
        case 4: {
            //errorUsername = "用户名‘"+username+"’格式不正确!(3-20位且以字母开头)";
            //用户名只能包含_,英文字母，数字
            //$('#signuptishi').empty();
			//var html = '<div class="alert alert-error " style="margin-top:15px;">'+errorUsername+'</div>';
			//$('#signuptishi').append(html);

			return false;
        }
    }
     
	var data = {classCode: "0001"}; // 这里要直接使用JOSN对象 	 
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		//url: "json_example/logontrue.json",
		url: url,
		dataType: "json",
		anysc: false,
		data: data,
		success: function(result) {
			var data = result;
			if(data.status == "false") {
			
				$('#signuptishi').empty();
				var html = '<div class="alert alert-error " style="margin-top:15px;">' + data.info + '</div>';
				$('#signuptishi').append(html);
				return false;
			}/* else {
				$('#signuptishi').empty();
				var html = '<div class="alert alert-error " style="margin-top:15px;">恭喜您，用户名可以使用！</div>';
				$('#signuptishi').append(html);
				
			}*/
		}
	});
    return 1;
}

function EmailCheck1() {
	signupdisabled();
	var email = document.getElementById('email').value;
	var url = "/gossip-server/verifyUsername?email=" + email;
	if(signupform.email.value == "") //对用户名是否为空进行验证
	{
		//$('#signuptishi').empty();
		//var html = '<div class="alert alert-error " style="margin-top:15px;">email不可以为空！</div>';
		//$('#signuptishi').append(html);

		return false;
	}else{
		reg=/^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/gi;
	    if(!reg.test(signupform.email.value))
	    {
	       // $('#signuptishi').empty();
			//var html = '<div class="alert alert-error " style="margin-top:15px;">非法的email！</div>';
			//$('#signuptishi').append(html);
	        return false;
	    }

	}
	var data = {
		classCode: "0001"
	}; // 这里要直接使用JOSN对象 	
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		//url: "json_example/logontrue.json",
		url: url,
		dataType: "json",
		anysc: false,
		data: data,
		success: function(result) {
			var data = result;
			/* 	if(data.status=="true")
       	{alert("email可以使用");}
       	else*/
			if(data.status == "false") {
				{
					$('#signuptishi').empty();
					var html = '<div class="alert alert-error " style="margin-top:15px;">' + data.info + '</div>';
					$('#signuptishi').append(html);
				}
			}/* else {
				$('#signuptishi').empty();
				var html = '<div class="alert alert-error " style="margin-top:15px;">恭喜您，email可以使用！</div>';
				$('#signuptishi').append(html);
				
			}*/
		}
	});
    return 1;
}