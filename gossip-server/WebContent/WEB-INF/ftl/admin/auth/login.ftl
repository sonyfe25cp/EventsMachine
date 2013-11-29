
<#include "/common/template-head.ftl">
	<link href="/css/signin.css" rel="stylesheet">
	<div class="container">
		<form class="form-signin" action="/admin/login" method="post">
	        <h2 class="form-signin-heading">Please sign in</h2>
	        <input type="text" class="form-control" placeholder="Email address" required autofocus name="email">
	        <input type="password" class="form-control" placeholder="Password" required name="password">
	        <input type="submit" value="Submit" class="btn btn-lg btn-primary btn-block"></input>
	      </form>
	</div>
<#include "/common/template-bottom.ftl">