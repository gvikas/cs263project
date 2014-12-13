<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="javax.servlet.ServletException" %>
<%@ page import="com.google.appengine.api.blobstore.BlobKey" %> 
<%@ page import="com.google.appengine.api.images.ImagesService"%> 
<%@ page import="com.google.appengine.api.images.ImagesServiceFactory"%> 
<%@ page import="com.google.appengine.api.memcache.MemcacheServiceFactory" %>
<%@ page import="com.google.appengine.api.memcache.AsyncMemcacheService" %>
<%@ page import="com.google.appengine.api.memcache.ErrorHandlers" %>
<%@ page import="java.util.concurrent.Future" %>
<%@ page import="java.util.logging.Level" %>
<%@ include file="comp/navbar.html" %>

<!DOCTYPE html>
<html>
  <head>
    <title>uChallenge</title>
    
    <link rel="stylesheet" type="text/css" href="stylesheet/bootstrap.css"> <!-- For testing purpose--> 
  </head>
  
  <body>
      <%  if (request.getUserPrincipal() != null){ %>
  	<div class="container">
  		<div class="col-lg-6 col-lg-offset-3">
  			<div class="well bs-component">
  				<fieldset>
 	<%
 		
 		String challengeKey = request.getParameter("challengeKey");
 		if (challengeKey == null || challengeKey.isEmpty()){ 
 	%>
 		<p> key is missing</p>
 	<%
	 	}
 		else { 
 		AsyncMemcacheService asyncCache = MemcacheServiceFactory.getAsyncMemcacheService();
    	asyncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
 		ImagesService imagesService = ImagesServiceFactory.getImagesService();
 		Future<Object> futureChallenge = asyncCache.get(challengeKey);
 		Entity challengePost;
 		try {
 			challengePost =  (Entity) futureChallenge.get();
 			if (challengePost == null){
 				Key challengePostKey = KeyFactory.createKey("ChallengePost", challengeKey);
 				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
 				Entity challengePostFromDB = datastore.get(challengePostKey);
 				
 				asyncCache.put(challengeKey, challengePostFromDB);
 				response.sendRedirect("/challengeview.jsp?challengeKey="+challengeKey);
 				response.setStatus(response.SC_MOVED_TEMPORARILY);
 				response.setHeader("Location", "/challengeview.jsp?challengeKey="+challengeKey);
 			}
 			String title = challengePost.getProperty("title").toString(); 
			String description = challengePost.getProperty("description").toString();
			String date = challengePost.getProperty("date").toString();
	        BlobKey blobKey = new BlobKey(challengePost.getProperty("blobKey").toString());
	        String imageUrl = imagesService.getServingUrl(blobKey);
	 %> 
		<legend> Your Challenge </legend>
		<h4> Title: <%= title %></h4>
		<h5> Description:  <%= description %>  </h5>

		<img src=<%= imageUrl %> height=256 width=256> 

		<br>

		<p> Date: <%= date %> </p>
	<%

 	} catch (Exception e) {
%>
	<p> Didn't find the key in memcacheStore : <%= challengeKey %> </p>
<%
 		}
 	}
 		

 	%> 	
 		</fieldset>
 		</div>
 	  </div>
 	</div>
  <% } else {
    response.sendRedirect("/");
    response.setStatus(response.SC_MOVED_TEMPORARILY);
    response.setHeader("Location", "/");

} %> 	
  </body>
</html>