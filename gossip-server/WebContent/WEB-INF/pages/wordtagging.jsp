<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/bootstrap.css"
	media="all">
<link rel="stylesheet" type="text/css" href="css/style.css" media="all">
<script type="text/javascript" src="js/jquery-1.8.2.js"></script>
<script type="text/javascript" src="js/bootstrap.js"></script>
<script type="text/javascript" src="js/wordtagging.js"></script>
<script type="text/javascript" src="js/jquery.cookie.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>中文词语标注页面</title>
</head>
<body>
  
  <div class="container">
     <div class="page-header">
         <h1>这是一个中文词语标注网页，如果您觉得页面上显示的词语对分析数据有用，请选择“保留”，若您觉得没用请选择选择“放弃”，否则，请选择“跳过”</h1>
     </div>
     <div id = "container-inner" style = "padding: 20px 0 40px">
        <div id = "word" style="text-align:center">
             <span id ="keyword" style="font-size:60px;padding-top:20px"></span>
        </div>
        <div id = "tagging" style="margin-top:30px;text-align:center">
             <button id = "keep"  class = "btn-large">保留</button>
             <button id = "jump"  class = "btn-large">跳过</button>
             <button id = "abort" class = "btn-large">放弃</button>
        </div>
     </div>
  </div>

</body>
</html>