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
<%@ page import="users.UserHelper" %>
<%@ include file="comp/navbar.html" %>
<%  if (request.getUserPrincipal() != null){ %>
<% ImagesService imagesService = ImagesServiceFactory.getImagesService();
   DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
   Filter filter = new FilterPredicate("user",FilterOperator.EQUAL,UserHelper.getUserEmail());
	Query query = new Query("ChallengePost").addSort("date", Query.SortDirection.DESCENDING).setFilter(filter);
		List<Entity> challenges = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
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
				<%if(challenges.isEmpty()){ %>
					
					<fieldset class="whiteborder">
					
					<p> Sorry, you have no more challenges <p>

					</fieldset>
					
<%				
				}else {
				
				for (Entity challenge : challenges) {
						String challengeKey = challenge.getKey().getName();
						String viewTitle = challenge.getProperty("title").toString();
						String viewDescription = challenge.getProperty("description").toString();
						String viewDate = challenge.getProperty("date").toString();
	        			BlobKey blobKey = new BlobKey(challenge.getProperty("blobKey").toString());
	        			String imageUrl = imagesService.getServingUrl(blobKey);
	        			%> 

  								<fieldset class="whiteborder">
									<h3> <%= viewTitle %></h3>
									
									<h5> Description:  <%= viewDescription %>  </h5>

									<img src=<%= imageUrl %> height=512 width=512> 

									<br>

									<h5> Date: <%= viewDate %> </h5>

								</fieldset>
	        <%
				}
	    }
	    %> 

					
	    </div>
	  </div>  
 	</div>
  </body>
    <% } else {
    response.sendRedirect("/");
    response.setStatus(response.SC_MOVED_TEMPORARILY);
    response.setHeader("Location", "/");

} %> 
</html>