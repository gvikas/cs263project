document.getElementById("delete").onclick = function() {sendDeleteRequest()};

function sendDeleteRequest(challengeKey) {
	var xhr = new XMLHttpRequest();
	xhr.open('DELETE',"/challenges?challengeKey="+challengeKey,true);
	xhr.send()
   window.location.replace("/challenges.jsp");
}