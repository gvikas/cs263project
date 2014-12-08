<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.io.IOException" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.servlet.ServletException" %>
<%@ page import="com.google.appengine.api.blobstore.BlobKey" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.images.ImagesService" %>
<%@ page import="com.google.appengine.api.images.ImagesServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Query.Filter" %> 
<%@ page import="com.google.appengine.api.datastore.Query.FilterOperator" %>
<%@ page import="com.google.appengine.api.datastore.Query.FilterPredicate" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="users.UserHelper" %>

<% ImagesService imagesService = ImagesServiceFactory.getImagesService();
   DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
   Filter filter = new FilterPredicate("toUser",FilterOperator.EQUAL,UserHelper.getUserEmail());
	Query query = new Query("ReceivedChallenges").addSort("toUser", Query.SortDirection.DESCENDING).setFilter(filter);
		List<Entity> challengeKeys = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
%> 
<!DOCTYPE html>
<html>
  <head>
    <title>uChallenge</title>
    <link rel="stylesheet" type="text/css" href="stylesheet/bootstrap.css"> 
    <link rel="stylesheet" type="text/css" href="stylesheet/style.css"> 
    <script src="/js/httpRequests.js" type="text/javascript"></script><!-- For testing purpose--> 
  </head>
  
  <body>
	<div class="container">
		<div class="col-lg-6 col-lg-offset-3">
			<div class="well bs-component">
				<div class="page-header">
					<h1 id="tables"> Your Challenges </h1>
				</div>
				<%if(challengeKeys.isEmpty()){ %>
					
					<fieldset class="whiteborder">
					
					<p> Sorry, you have no more challenges <p>

					</fieldset>
					
<%				
				}else {
				
				for (Entity chalKey : challengeKeys) {
						Key challengePostKey = KeyFactory.createKey("ChallengePost", chalKey.getProperty("challengeKey").toString());
						Entity challengePost = datastore.get(challengePostKey);
						String challengeKey = challengePost.getKey().getName();
						String viewTitle = challengePost.getProperty("title").toString();
						String viewDescription = challengePost.getProperty("description").toString();
						String viewDate = challengePost.getProperty("date").toString();
	        			BlobKey blobKey = new BlobKey(challengePost.getProperty("blobKey").toString());
	        			String imageUrl = imagesService.getServingUrl(blobKey);%> 

  								<fieldset class="whiteborder">
									<h3> <%= viewTitle %></h3>
									
									<h5> Description:  <%= viewDescription %>  </h5>

									<img src=<%= imageUrl %> height=512 width=512> 

									<br>

									<h5> Date: <%= viewDate %> </h5>

									<a class="btn btn-success" href="/newpost.jsp?challengeKey=<%= challengeKey %>">Accept</a>

									<input type="button" class= "btn btn-danger" value="Delete" onclick="sendDeleteRequest(<%= challengeKey %>)"/>

								</fieldset>
	        <%
				}
	    }
	    %> 

					
	    </div>
	  </div>  
 	</div>
  </body>
</html>