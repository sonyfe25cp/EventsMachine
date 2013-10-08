//用户点击记录
$(document).ready(function(){
	thisURL = document.URL;
    var newid = thisURL.split("=");
	var name = "username";
	//var username = getcookie(name);
	var username = $.cookie(name);
	var date=getnowdate();
	//var time=getnowtime();
	//alert(date);
    //alert(username);
	var data='username='+username+'&time='+date+'&id='+newid[1];
	//alert(data);
if(username!="''"){	

    $.ajax({
			type: "POST",
			contentType: "application/json; charset=utf-8",
			url: "/Gossip-server/clicklog",
			//url: url,
			dataType: "json",
			anysc: false,
			data: data,
			success: function(result) {

				return "";
			}
		});
}
});
//获取系统当前时间
function getnowdate(){
	var now = new Date();

	var mytime=now.toLocaleString( );    
	var reg = new RegExp("年", "g");            //创建正则RegExp对象
    var newStr = mytime.replace(reg, "-");    // 替换字符
    var reg = new RegExp("月", "g");            //创建正则RegExp对象
     newStr= newStr.replace(reg, "-");    // 替换字符    
    var reg = new RegExp("日", "g");            //创建正则RegExp对象
     newStr= newStr.replace(reg, " ");    // 替换字符
    return newStr;
}

