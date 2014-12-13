<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="javax.servlet.ServletException" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ include file="comp/navbar.html" %>
<%	response.setHeader("X-XSS-Protection","1; mode=block");
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>
<%  if (request.getUserPrincipal() != null){ %>
<!DOCTYPE html>
<html>
  <head>
    <title>uChallenge</title>
    
    <link rel="stylesheet" type="text/css" href="stylesheet/bootstrap.css"> <!-- For testing purpose--> 
  </head>
  
  <body>
  	<div class="container">
  		<div class="col-lg-6 col-lg-offset-3">
  			<div class="well bs-component">
  				 <form class="form-horizontal" action="<%= blobstoreService.createUploadUrl("/enqueue")%>" method="post" enctype="multipart/form-data">
  					
 	<%
 
 		String challengeKey = request.getParameter("challengeKey");
 		if (challengeKey == null || challengeKey.isEmpty()){ 
 	%>
 		<p> key is missing</p>
 	<%
	 	}
 		else { 
 		Key challengePostKey = KeyFactory.createKey("ChallengePost", challengeKey);
 		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
 		try {
 			Entity challengePost = datastore.get(challengePostKey);
 			String title = challengePost.getProperty("title").toString(); 
			String description = challengePost.getProperty("description").toString();
	 %> 
	   <fieldset>
	    <legend>Create your challenge</legend>
   	  	<div class="form-group">
     		 <div class="col-lg-6">
     			   <input type="text" class="form-control"  name="title" value="<%= title %>" readonly>
     	 	</div>
  			<br>
   	  	</div> 

   	  	<div class="form-group">
      		<div class="col-lg-6">
     			  <textarea class="form-control" style="resize:none" cols="40" rows="5" name="description" readonly><%= description %></textarea>
     		</div>
   	  	</div>

   	 	 <br>
   	    <div class="form-group">
    	  	<div class="col-lg-6">
      			<input class="btn btn-primary" type="file" name="image" accept="image/*;capture=camera">
      		</div>
      	</div>

      	<div class="form-group">
      		<div class="col-lg-6">
   		   	<input class="btn btn-primary" type="submit" name="Submt" value="Submit" >
   			</div>
   	  	</div>
	<%

 	} catch (Exception e) {
%>
	<p> Didn't find the key : <%= challengeKey %> </p>
<%
 		}
 	}

 	%> 	
 		  </fieldset>
 		 </form>
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