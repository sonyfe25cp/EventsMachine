<#include "/common/template-head.ftl">

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>用户注册</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link href="/css/bootstrap.css" rel="stylesheet">
    <link href="/css/bootstrap-responsive.css" rel="stylesheet">
    <link href="/css/register.css" rel="stylesheet">
    <script "text/javascritpt" src="/js/jquery-1.8.2.js"></script>
    <script "text/javascritpt" src="/js/register.js"></script>
    
    
  </head>
  <body>
    <div class="container">
		<#include "/head.ftl">
		<ol class="breadcrumb">
		  <li><a href="/">新闻系统</a></li>
		  <li class="active">用户注册</li>
		</ol>
		<div class="container">
           <div class="form-signin">
               <h2 class="form-signin-heading">用户注册：</h2>
               <div id = "email-block">
                  <lable for = "email" class = "fortext">
                     注册邮箱：
                  </lable>
                  <input type="text" class="input-block-level" placeholder="Email address" id = "email">
                  <lable id = "email-tip" class ="lable-tip">
                     请使用常用邮箱注册
                  </lable>
               </div>
               
                <div id = "uname-block">
                  <lable for = "username" class = "fortext">
                     &nbsp&nbsp&nbsp用户名：
                  </lable>
                  <input type="text" class="input-block-level" placeholder="Username" id = "username">
                  <lable id = "uname-tip" class ="lable-tip">
                     以字母开头，3-20位，可包含字母、数字和_
                  </lable>
               </div>
               
               <div id = "pwd-block">
                  <lable for = "password" class = "fortext">
                     密&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp码：
                  </lable>
                  <input type="password" class="input-block-level" placeholder="Password" id = "password">
                  <lable id = "pwd-tip" class ="lable-tip">
                     6~20位，数字或字母
                  </lable>
               </div>
               
               <div id = "pwd-block">
                  <lable for = "repassword" class = "fortext">
                     确认密码：
                  </lable>
                  <input type="password" class="input-block-level" placeholder="RePassword" id = "repassword">
                  <lable id = "repwd-tip" class = "lable-tip">
                     
                  </lable>
               </div>
               
               <div id = "tips" style = "margin-top:-6px;">
                  <span id = "login-tip" style= "display:none;color:red"></span>
               </div>

               <div id = "login-block" style = "margin-top:10px;">
                   <button class="btn btn-large btn-primary" id = "register">注册</button>
                   &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp 
                   <font color="red">已有账号?直接登录</font>
                   <button class="btn btn-large btn-primary" id = "login">登录</button>
               </div>
               
           </div>

        </div>
	</div>
    <#include "/common/template-bottom.ftl">
  </body>
</html>