
function sendUpdateRequest(challengeKey, user) {
	var xhr = new XMLHttpRequest();
	xhr.open('PUT',"/challenges?user="+user+"&challengeKey="+challengeKey,true);
	xhr.send()
	var thisURL = window.location.pathname;
   window.location.replace(thisURL);
}

function sendAcceptedRequest(challengeKey, user) {
	var xhr = new XMLHttpRequest();
	xhr.open('PUT',"/challenges?user="+user+"&challengeKey="+challengeKey,true);
	xhr.send()

   window.location.replace("/newpost.jsp?challengeKey="+challengeKey);
}