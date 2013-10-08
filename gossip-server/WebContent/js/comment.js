/*
$(document).ready(function(){
	thisURL = document.URL;
    var newid = thisURL.split("=");
	var name = "username";
	var username = getcookie(name);
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
			url: "/comment",
			//url: url,
			dataType: "json",
			anysc: false,
			data: data,
			success: function(result) {
                var data=result;
                if(data.status=="true"){
                	alert(评论成功);
                	//显示
                }
				else{
					alert(data.info);

				}
			}
		});
}
});*/

//$("comSub").click(function(){
//	alert("fdfsdfdfdsf");
//});

function commentSubmit(){  
    thisURL = document.URL;
    var newid = thisURL.split("=");
	var name = "username";
	//var username = getcookie(name);
	var username=$("#currentUname").val();
	var comment=$("#textarea1").val();
	var date=getnowdate();
	var data='username='+username+'&time='+date+'&id='+newid[1]+'&comment='+comment;
if(username!="''"){	

    $.ajax({
			type: "GET",
			contentType: "application/json; charset=utf-8",
			url: "/Gossip-server/comment",
			//url: url,
			dataType: "json",
			anysc: false,
			data: data,
			success: function(result) {
                var data=result;
                if(result.status=="true"||(result.status).equals("true")){
                	alert("评论成功");
                	//显示
                var name="username";
			    var username=$.cookie(name);
			    if(username!="''"&&username!=null)
				{document.getElementById('commentusername').innerHTML=username;}
			    else{
			    	document.getElementById('commentusername').innerHTML="匿名";
			    }
				var textarea1=document.getElementById("textarea1");
				var table1=document.getElementById("table1");
				var tr=document.createElement("tr");
				var td1=document.createElement("td");
				var td2=document.createElement("td");
				td1.innerText=commentusername.innerHTML;
				td2.innerText=textarea1.value;
				tr.appendChild(td1);
				tr.appendChild(td2);
				table1.appendChild(tr);
                }
				else{
					alert(result.info);

				}
			},
		    error:function(){
		    	alert("asdasda");
		    }
    });
    }
}
		
