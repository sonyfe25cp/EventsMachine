//0001111

$(document).ready(function() {
	$('#keyword').empty();
	var wordTags;
	var index = 0;
	$.ajax({
		async: false,
	    type: "GET",
	    contentType: "application/json; charset=utf-8",
	    url:"/wordTagging/getWordTagRandom",
	    dataType: "json",
	    success: function(result) {
	    	wordTags = result;
	    	$('#keyword').text(wordTags[index].keywords);
	    	}
	    });
	
	$('#keep').click(function(){
		var keyword = $('#keyword').text();
		$.ajax({
		    async: false,
		    type: "GET",
		    contentType: "application/json; charset=utf-8",
		    url:"/wordTagging/updateApprove?keyword=" + keyword,
		    dataType: "json",
		    success: function(result){
		    	index++;
		    	if(index>=20){
		    		wordTags = getWordTags();
		    		index = 0;
		    	}
		    	$('#keyword').text(wordTags[index].keywords);
		    	}
		    });
	});
	
	$('#jump').click(function(){
		index++;
		if(index >=20){
			wordTags = getWordTags();
			index = 0;
		}
		$('#keyword').text(wordTags[index].keywords);
	});
	
	$('#abort').click(function(){
		var keyword = $('#keyword').text();
		$.ajax({
		    async: false,
		    type: "GET",
		    contentType: "application/json; charset=utf-8",
		    url:"/wordTagging/updateAgainst?keyword=" + keyword,
		    dataType: "json",
		    success: function(result) {
				index++;
				if(index >=20){
					wordTags = getWordTags();
					index = 0;
				}
				$('#keyword').text(wordTags[index].keywords);
		    	}
		    });
	});
	
	function getWordTags(){
		var wordTags;
		$.ajax({
			async: false,
		    type: "GET",
		    contentType: "application/json; charset=utf-8",
		    url:"/wordTagging/getWordTagRandom",
		    dataType: "json",
		    success: function(result) {
		    	wordTags = result;
		    	}
		    });
		return wordTags;
	};
	
});
