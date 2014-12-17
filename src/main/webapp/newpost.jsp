<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="javax.servlet.ServletException" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.memcache.MemcacheService" %>
<%@ page import="com.google.appengine.api.memcache.MemcacheServiceFactory" %>
<%@ page import="com.google.appengine.api.memcache.ErrorHandlers" %>
<%@ page import="java.util.logging.Level" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %> 
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Query.Filter" %> 
<%@ page import="com.google.appengine.api.datastore.Query.FilterOperator" %>
<%@ page import="com.google.appengine.api.datastore.Query.FilterPredicate" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="java.util.List" %>
<%@ page import="users.UserHelper" %>
<%@ page import="enqueue.ChallengesServlet" %>
<%@ include file="comp/navbar.html" %>

<%
/**
This newpost.jsp show nonly when a user have accepted a challenge in the receiving challenges.
There are some checks here: 
	If the user is not logged in, redirect to Homepage.
	If the key is null or empty, redirect to Homepage
	If the post does not the to the right user, redirect to Homepage

When the user have uploaded a photo, or video and select receivers, and the user submit his/her 
challenges. The challenges will be sent to the new receivers. 
*/
%>

<%	response.setHeader("X-XSS-Protection","1; mode=block");
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
  	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
    String userEmail = user.getEmail();
	Key userKey = KeyFactory.createKey("User", userEmail);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  	Query query = new Query("Friendship", userKey).addSort("friendemail", Query.SortDirection.ASCENDING);
  	List<Entity> friends = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5)); 
	  if (request.getUserPrincipal() != null){ %>
<!DOCTYPE html>
<html>
  <head>
    <title>doChallenge</title>
    
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
 		   ChallengesServlet.reDirectToHomepage(response);
	 	}
 		if(!ChallengesServlet.belongPosttoUser(challengeKey, UserHelper.getUserEmail())){
 		   ChallengesServlet.reDirectToHomepage(response);
 		}
 		else { 
 	 		MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
 	 		memcache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
 	 		Entity challengePost;
 		try {
 			challengePost =  (Entity) memcache.get(challengeKey);
 			if(challengePost == null) {
 	 	 		Key challengePostKey = KeyFactory.createKey("ChallengePost", challengeKey);
 	 			Entity challengePostFromDB = datastore.get(challengePostKey);
 	 			memcache.put(challengeKey, challengePostFromDB);
 	 			
 				response.sendRedirect("/newpost.jsp?challengeKey="+challengeKey);
 				response.setStatus(response.SC_MOVED_TEMPORARILY);
 				response.setHeader("Location", "/newpost.jsp?challengeKey="+challengeKey);
 			}
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
        <div style="height: 10em; width: 20em; overflow: auto;">
        	<%if(friends.isEmpty()){ %>
        	
        		<p>Sorry, you have no friends </p>
        		<p>Go it Friends-page, and add your friends </p>
        	<%
        	} else {
        	%>
					<h4 id="tables"> Select your friends to send </h4>
			<%
			int i = 0;
			for (Entity friend : friends) {
				String viewFriend = friend.getProperty("friendemail").toString();
			
			%>
         		 <input id="<%= i %>" type="checkbox" name="friends" value="<%= viewFriend %>"/>
          			<label for="<%= i %>"><%= viewFriend %></label>
         		 <br />
 	        <%
 	         i++;
				}
	    }
	    %> 
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
	   ChallengesServlet.reDirectToHomepage(response);

} %>	
  </body>
</html>