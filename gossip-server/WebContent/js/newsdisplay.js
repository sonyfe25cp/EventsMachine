﻿
//上页获取的点击的事件的id要用到


$(document).ready(function(){
	//获取id
	thisURL = document.URL;
	var newid=thisURL.split("=");
	//alert($("#newsid").val());
	var newsid=$("#newsid").val();
	var url='/gossip-server/news/'+newsid;
	var datt = { classCode: "0001"}; // 这里要直接使用JOSN对象
    $.ajax({ 
       type: "GET", 
       contentType: "application/json; charset=utf-8", 
       url: url, 
       dataType: "json", 
       anysc: false, 
       //data: data, 
       success:function(result){
       	var data=result;
       	$('.news-title').empty();
       	var html='				<h2 style="text-align: center;">'+data.title+'</h2>';
       	html+='				<p style="text-align: center;"><span>'+data.publish_at+'</span>';
       	html+='					<span class="news-source">新闻来源:'+data.source+'</span>';
       	html+='					<span class="news-source">作者:'+data.author+'</span>';
       	html+='				</p>';
       	$('.news-title').append(html);
       	$('.news-content').empty();
       	var htma='     <p align="center"><span style="FONT-SIZE: 12px">'+data.desc+'</span>';
       	htma+='				<br>';
       	htma+='<p style="TEXT-INDENT: 2em">'+data.body+'</p>';
       	$('.news-content').append(htma);
       	}
     });
     var data = { "classCode": "0001"};
     $('.relate-news').empty();
     var urlt='/gossip-server/related-news?id='+newid[1]+'&pageNo=1&limit=15'
   $.ajax({ 
       type: "GET", 
       contentType: "application/json; charset=utf-8", 
       //url: "json_example/rnews.json", 
       url:urlt,
       dataType: "json", 
       anysc:false, 
       data:data, 
       success: function(result){ 
       	   // alert(result.pageNo);
	         var datad=result.news;
	        // alert(datad[1].id);
	          $('#relate').empty();
	         var html="";
	         for(var ir=0;ir<datad.length;ir++){
	         	
	           html+='												<a href="/gossip-server/newsContent?newsId='+datad[ir].id+'" class="span3">'+datad[ir].title+' </a>';
	        }
	     
	        $('.relate-news').append(html);
	 
	      }
		 });

});

function timeConvert(time){
	var timeString="";
	if(time==null||time=="")
		return time;
	if(time.length>8){
		timeString=time.substring(0,4)+"-"+time.substring(4,6)+"-"+time.substring(6,8);
	}
	return timeString;
}

/*
 接口：/new/{id}
 说明：返回id为{id}的新闻
 返回值：

{
    "id":123,//新闻id
    "title":"xxx",//新闻标题
    "desc":"",//新闻描述
    "author":"",//新闻作者
    "body":"",//新闻正文
    "publish_at":"",//发表时间
    "source":"",//来源网站
    "started_location":"",//新闻发生地
    "keywords":["xx","yy"]//新闻关键词
}

*/