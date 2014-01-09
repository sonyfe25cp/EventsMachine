<#include "/common/template-head.ftl">

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>用户登陆</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link href="/css/bootstrap.css" rel="stylesheet">
    <link href="/css/bootstrap-responsive.css" rel="stylesheet">
    <script "text/javascritpt" src="/js/jquery-1.8.2.js"></script>
    <script "text/javascritpt" src="/js/login.js"></script>
    
    <style type="text/css">
      body {
        padding-top: 40px;
        padding-bottom: 40px;
        background-color: #f5f5f5;
      }

      .form-signin {
        max-width: 300px;
        padding: 19px 29px 29px;
        margin: 0 auto 20px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        -webkit-border-radius: 5px;
           -moz-border-radius: 5px;
                border-radius: 5px;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                box-shadow: 0 1px 2px rgba(0,0,0,.05);
      }
      .form-signin .form-signin-heading,
      .form-signin .checkbox {
        margin-bottom: 10px;
      }
      .form-signin input[type="text"],
      .form-signin input[type="password"] {
        font-size: 16px;
        height: auto;
        margin-bottom: 15px;
        padding: 7px 9px;
      }

    </style>
    
  </head>
  <body>
    <div class="container">
		<#include "/head.ftl">
		<ol class="breadcrumb">
		  <li><a href="/">新闻系统</a></li>
		  <li class="active">用户登陆</li>
		</ol>
		<div class="container">
           <div class="form-signin">
               <h2 class="form-signin-heading">用户登录：</h2>
               <div id = "uname-block">
                  <input type="text" class="input-block-level" placeholder="Email address" id = "username">
               </div>
               
               <div id = "pwd-block">
                  <input type="password" class="input-block-level" placeholder="Password" id = "password">
               </div>
               <div id = "tips" style = "margin-top:-6px;">
                  <span id = "login-tip" style= "display:none;color:red"></span>
               </div>

               <div id = "login-block" style = "margin-top:10px;">
                   <button class="btn btn-large btn-primary" id = "login">登陆</button>
                   &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp
                   <button class="btn btn-large btn-primary" id = "register">注册</button>
               </div>
               
           </div>

        </div>
	</div>
    <#include "/common/template-bottom.ftl">
  </body>
</html>