/*function eventdis(){
		var data = arguments[0];
		$('#container-inner .event-title')[0].empty();
    $("#container-inner .event-title").find("h2").get(0).innerHtml=data["title"];
    var eventD='<div class="event-title"><div class="span4"><img src="'+data["img"]+'" alt=""></div><div class="span8 event-brief"><h2>'+data["title"]+'</h2><p>'+data["desc"]+'</p></div></div>';
    eventD.appendTo(".event-title-inner");
  }
$(function(){
	var script = document.createElement('script');
	//script.src='/events?pageNo={1}&limit={15}';
	script.src = 'http://10.1.1.94:8080/jsonserver/json?callback=eventdis';
	script.type = 'text/javascript';
	$("head").get(0).appendChild(script);
});*/
/*2.事件查看
 接口：/event/{e-1}
 说明：返回id为{e-1}的事件
 返回值：
{
    "id" : "e-1",//事件id
    "title" : "",//事件标题
    "desc" : "",//事件描述
    "img":"/images/e-1.jpg",//事件焦點圖
    "started_at" : "2012-10-1",//起始时间
    "started_location" : "",//起始地点
    "keywords" : ["xx", "yy"],//关键词集合
    "news" : ["n-1", "n-2"]//新闻id集合
}*/
//上页获取的点击的事件的id要用到
//事件查看
$(document).ready(function() {
	//获取了id
	thisURL = document.URL;
	//alert(thisURL);
	var eveid = thisURL.split("=");
	//alert(eveid[1]);
	//eveid=id;
	urle="/gossip-server/events/"+eveid[1];
	//alert(urle);
	var array = new Array();
	$.getJSON(urle, function(data) {
		//alert(data);
		$('#container-inner .event-title').empty();
		var html = '							<div class="span4"><img src="' + data['img'] + '" alt=""></div>';
		//html+='<img src="'+data['img']+'" alt=""></div>';
		html += '<div class="span8 event-brief">';
		html += '<h2>' + data['title'] + '</h2>';
		html += '<p>' + data['desc'] + '</p>';
		html += '</div>';
		$('#container-inner .event-title').append(html);
		var newsids = data.news;
		//	 alert(newsids.length);
		// alert("newsids[1]");
		// alert(newsids[1]);
		//var arr=new Array();
		//	 arr=newsids;
		//	 alert("arr");
		//	 alert(arr[1]);
		var array = new Array();
		var datt = {
			classCode: "0001"
		}; // 这里要直接使用JOSN对象 	
		//alert(url);
		$.ajax({
			type: "GET",
			contentType: "application/json; charset=utf-8",
			//url: "json_example/news.json",
			url:"/gossip-server/news/event-news?newsids="+newsids,
			dataType: "json",
			anysc: true,
			//data: datt,
			success: function(result) {
				array=result.news;
				//alert("length");
				//alert(array.length);
				// var date=array[0].publish_at;
				var date = "";
				//evedate(date);
				var numRows = array.length;
				var numPerPage = 10;
				var numPage = Math.ceil(numRows / numPerPage);
				// var html='';
				var html = '		                <li class="disabled"><a href="#">«</a></li>';
				for(var ib = 0; ib < numPage; ib++) {
					html += '		                <li><a>' + (ib + 1) + '</a></li>'
				}
				//var da=1;
				$('#page ul').append(html);
				//var $pager=$('<div id="page" class="pagination pagination-centered"></div>');
				//***********************************修改
				date = ppp(1, numPerPage, array, date);

				//$('#page ul').removeClass('active');
				$('#page ul li').eq(1).addClass('active');
				for(var pagea = 1; pagea < numPage + 1; pagea++) {
					$('#page li').eq(pagea).bind('click', {
						'newPage': pagea
					}, function(event) {
						var currentPage = event.data['newPage'];
						var pageb = currentPage;
						date = ppp(pageb, numPerPage, array, date);
						$('#page ul li').removeClass('active');
						$('#page li').eq(pageb).addClass('active');
					});
				}
				/* for(var current=1;current<array.length;current++){
    	if(array[current].publish_at==date)
    	{newdis(array[current]);}
    	else
    		{ var html='								</ul></div></div>';
    			$('#timelinecontent').append(html);
    			date=array[current].publish_at;
    			 evedate(date);
    			}
    	}
    	var html='								</ul></div></div>';
    	$('#timelinecontent').append(html);	
     */
			}

		});



		//var numRows=array.length;
	});
	var urlt="/gossip-server/related-events?id="+eveid[1]+"&pageNo=1&limit=15"
	//相关事件的显示
	var data = {
		"classCode": "0001"
	};
	$.ajax({
		type: "GET",
		contentType: "application/json; charset=utf-8",
		//url: "json_example/revents.json",
		url:urlt,
		dataType: "json",
		anysc: false,
		data: data,
		success: function(result) {
			var datad = result.events;
			$('#relate').empty();
			var html = "";
			for(var ir = 0; ir < datad.length; ir++) {
				html += '												<a href="/gossip-server/events?id=' + datad[ir].id + '" class="span3">' + datad[ir].title + ' </a>';
			}

			$('#relate').append(html);

		}
	});
	var timelineHeight = $("#timelinecontent").height()
	$("#timelinebar").height(timelineHeight);

});
var evedate = function(date) {
		var html = '						<div class="news" >';
		html += '							<span class="news-time">' + date + '</span>';
		html += '							<div class="news-list">';
		html += '								<i>';
		html += '								</i>'
		html += '								<ul  id="' + date + '">';
		//html+='							</ul></div></div>';
		$('#timelinecontent').append(html);

	}
var ppp = function(page, numPerPage, array, dateb) {
		var tata = array;
		var t = ((page - 1) * numPerPage);
		var r = page * numPerPage;
		$('#timelinecontent').empty();
		//	evedate(dateb);
		for(var currenta = t;
		(array.length < r) ? (currenta < array.length) : (currenta < r); currenta++) {
			if(array[currenta].publish_at == dateb) {
				newdis(dateb, array[currenta]);
				//alert(2);
				//alert(array[currenta].id);
			} else {
				var htmlb = '								</ul></div></div>';
				$('#timelinecontent .news').append(htmlb);
				dateb = array[currenta].publish_at;
				evedate(dateb);
				newdis(dateb, array[currenta]);
				// alert("1");
				//   alert(array[currenta].desc);
			}
		}
		//****************************************88
		// var html='								</ul></div></div>';
		$('#timelinecontent').append(htmlb);
		return dateb;
	}

var newdis = function(dateb, datb) {
		var htmlc = '									<li class="article">';
		htmlc += '										<div class="article-content">';
		htmlc += '											<h2><a href="/gossip-server/news?id=' + datb.id + '" title="">' + datb.title + '</a></h2>';
		//alert("3");
		//alert(datb.title);
		htmlc += '											<p>' + datb.desc + '</p></div>';
		htmlc += '										<span class="s">来源:' + datb.source + '</span>';
		htmlc += '										<span class="s">作者:' + datb.author + '</span>';
		htmlc += '										</li>'

		$('#' + dateb).append(htmlc);
	}

	//相关事件部分 上页获取的时间的id要用到
	