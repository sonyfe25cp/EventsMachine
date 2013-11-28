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
	    	content = wordTags[index].keywords + " | " +wordTags[index].property;
	    	$('#keyword').text(content);
	    	$('#keywordId').val(wordTags[index].id);
	    	}
	    });
	
	$('#keep-word').click(function(){
		var keyword = $('#keyword').text();
		var id = $('#keywordId').val();
		$.ajax({
		    async: false,
		    type: "GET",
		    contentType: "application/json; charset=utf-8",
		    url:"/wordTagging/updateApprove?id=" + id,
		    dataType: "json",
		    success: function(result){
		    	index++;
		    	if(index>=20){
		    		wordTags = getWordTags();
		    		index = 0;
		    	}
		    	content = wordTags[index].keywords + " | " +wordTags[index].property;
		    	$('#keyword').text(content);
		    	$('#keywordId').val(wordTags[index].id);
		    	}
		    });
	});
	
	$('#jump-word').click(function(){
		index++;
		if(index >=20){
			wordTags = getWordTags();
			index = 0;
		}
		content = wordTags[index].keywords + " | " +wordTags[index].property;
    	$('#keyword').text(content);
		$('#keywordId').val(wordTags[index].id);
	});
	
	$('#abort-word').click(function(){
		var keyword = $('#keyword').text();
		var id = $('#keywordId').val();
		$.ajax({
		    async: false,
		    type: "GET",
		    contentType: "application/json; charset=utf-8",
		    url:"/wordTagging/updateAgainst?id=" + id,
		    dataType: "json",
		    success: function(result) {
				index++;
				if(index >=20){
					wordTags = getWordTags();
					index = 0;
				}
				content = wordTags[index].keywords + " | " +wordTags[index].property;
		    	$('#keyword').text(content);
				$('#keywordId').val(wordTags[index].id);
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
