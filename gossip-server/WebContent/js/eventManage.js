$(document).ready(function() {
	//获取了id
	var currentPageNum=0;
	thisURL = document.URL;
	var eveid = thisURL.split("=");
	var eventid=$("#eventid").val();
	var status=$("#status").val();
	//alert(status);
//	alert(imageStatus);
	url="/Gossip-server/events/"+eventid;
	
	$.ajax({
		type: "GET",
		contentType: "application/json; charset=utf-8",
		url:url,
		anysc: true,
		dataType: "json",
		success: function(data) {
			$('#eventTitle').val(data['title']);
			$('#eventContent').val(data['desc']);
			$('#eventKeywords').val(data['keywords']);
			$('#eventLocation').val(data['started_location']);
			$('#addnews').val(data['news']);
			var newsId=data['news'];
			$('#newslist').empty();
			var html='<p style="font-size:14px">选择要删除的新闻</p>';
			$.ajax({
				type: "GET",
				contentType: "application/json; charset=utf-8",
				url:"/Gossip-server/news/event-news?newsids="+newsId,
				dataType: "json",
				anysc: true,
				//data: datt,
				success: function(result) {
					var newslist=result.news;
					for(var i=0;i<newslist.length;i++){
						html+='<input type="checkbox" name="check" class="pull-left" value="'+ newslist[i].id +'" id="'+newslist[i].id +'"/>';
						html+='<label for="'+newslist[i].id +'" accesskey="a">'+newslist[i].title+'</label><br/>';
					}
					html+='<button  id="submit1" class="btn btn-primary pull-left" onclick="deleteNews('+eventid +')">删除新闻</button>';
					$('#newslist').append(html);
				}
			});//ajax
		}
	});//ajax
	
$('#submitChange').click(function(){
	var title=$('#eventTitle').val();
	var keyWords=$('#eventTitle').val();
	var summary=$('#eventContent').val();
	var newsId=$('#addnews').val();
	var eventLocation=$('#eventLocation').val();
	//alert("123123");
	var data="eventId="+eventid+"&title="+title+"&summary="+summary+"&newsId="+newsId+"&keyWords="+keyWords+"&location="+eventLocation;
	window.location.href="/Gossip-server/eventModify/content?"+data;
});

if(status=="success")
	$('#imgstatusDis').html("图片提交成功！");
});//document

function deleteNews(eventid){
	var newsId="";
	$("input[type=checkbox][checked]").each(function(){ //由于复选框一般选中的是多个,所以可以循环输出 
		if(newsId=="")
			newsId+=$(this).val();
		else
			newsId+=";"+$(this).val();
		}); 
	var data="eventId="+eventid+"&newsId="+newsId;
	//alert(data);
	window.location.href="/Gossip-server/eventModify/deleteNews?"+data;

};



