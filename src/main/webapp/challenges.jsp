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

<% ImagesService imagesService = ImagesServiceFactory.getImagesService();
   DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	Query query = new Query("ChallengePost").addSort("date", Query.SortDirection.DESCENDING);
		List<Entity> challenges = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
%> 
<!DOCTYPE html>
<html>
  <head>
    <title>uChallenge</title>
    <link rel="stylesheet" type="text/css" href="stylesheet/bootstrap.css"> 
    <script src="/js/httpRequests.js" type="text/javascript"></script><!-- For testing purpose--> 
  </head>
  
  <body>
	<div class="container">
		<div class="bs-docs-section">
			<div class="col-lg-6 col-lg-offset-3">
				<div class="page-header">
					<h1 id="tables"> Challenges </h1>
				</div>

				<div class="bs-component">
					<table class="table table-striped table-hover">
					<thead>
						<tr>
						<th>Key</th>
                    	<th>Title</th>
                    	<th>Description</th>
                    	<th>Image</th>
                    	<th>Date </th>
                    	<th>Accept</th>
                    	<th>Delete</th>
						</tr>
					</thead> 
					<tbody>
						<%for (Entity challenge : challenges) {
						String challengeKey = challenge.getKey().getName();
						String viewTitle = challenge.getProperty("title").toString();
						String viewDescription = challenge.getProperty("description").toString();
						String viewDate = challenge.getProperty("date").toString();
	        			BlobKey blobKey = new BlobKey(challenge.getProperty("blobKey").toString());
	        			String imageUrl = imagesService.getServingUrl(blobKey);%> 

	        			<tr>
	        				<td><%= challengeKey %></td>
	        				<td><%= viewTitle %></td>
	        				<td><%= viewDescription %></td>
	        				<td><img src=<%= imageUrl %> height=64 width=64></td>
	        				<td><%= viewDate%> </td>
	        				<td><a class="btn btn-success" href="/challengepost.jsp?challengeKey=<%= challengeKey %>">Accept</a></td>
	        				<td><input type="button" value="Delete" onclick="sendDeleteRequest(<%= challengeKey %>)"/> </td> <!-- Add a delete function -->
	        				
	        			</tr>
	        <%
	    }
	    %> 


					</tbody>
					</table>
				</div>		
			</div>
		</div>


 	</div>
  </body>
</html>