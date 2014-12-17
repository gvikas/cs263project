
function sendUpdateRequest(challengeKey, user) {
	 var delay=200;//100 miliseconds
 	var xhr = new XMLHttpRequest();
	xhr.open('PUT',"/challenges?user="+user+"&challengeKey="+challengeKey,true);
	xhr.send()
	    setTimeout(function(){
	    	
	    	window.location.replace("/receivedChallenges.jsp");
	    },delay); 

}

function sendAcceptedRequest(challengeKey, user) {
	 var delay=200;//100 miliseconds
		var xhr = new XMLHttpRequest();
		xhr.open('PUT',"/challenges?user="+user+"&challengeKey="+challengeKey,true);
		xhr.send()

	    setTimeout(function(){

	    	window.location.replace("/newpost.jsp?challengeKey="+challengeKey);
	    },delay); 

}