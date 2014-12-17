<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.servlet.ServletException" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Query.Filter" %> 
<%@ page import="com.google.appengine.api.datastore.Query.FilterOperator" %>
<%@ page import="com.google.appengine.api.datastore.Query.FilterPredicate" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %> 
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="users.UserHelper" %>
<%@ page import="enqueue.ChallengesServlet" %>
<%@ include file="comp/navbar.html" %>
<% response.setHeader("X-XSS-Protection","1; mode=block"); %>
<%
/**
This friends.jsp, the users can add their friends to their friends-list by their email.
The user can also see his/her friends.

*/
%>

<!DOCTYPE html>
<html>
<head>
  <title>doChallenge</title>
    
    <link rel="stylesheet" type="text/css" href="stylesheet/bootstrap.css"> <!-- Customized bootstrap--> 
    <link rel="stylesheet" type="text/css" href="stylesheet/bootstrap.css">
   
    <!-- <link rel="stylesheet" type="text/css" href="stylesheet/bootstrap.css"> -->
   <link rel="stylesheet" type="text/css" href="stylesheet/bootstrap-responsive.css">
  
</head>
<body>
    <%  if (request.getUserPrincipal() != null){ %>  
  	<div class="container">
    <div class="col-lg-6 col-lg-offset-3">
      <div class="well bs-component">
  	 <form class="form-horizontal" action="/friendenq" id="addFriend" method="post">
     <fieldset>
      <legend>Add a Friend</legend>
   	  <div class="form-group">
     	 <div class="col-lg-6">
     		 <input type="t" class="form-control" id="user_email" placeholder="Email" name="user_mail">
     	 </div>
   	  </div> 

   	  <br>

      <div class="form-group">
      	<div class="col-lg-6">
   		   <input class="btn btn-primary" type="submit" name="Submt" value="Submit" >
   		</div>
   	  </div>
    </fieldset>
		</form>
      	</div>
     
      <% 
      	UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String userEmail = user.getEmail();
		 Key userKey = KeyFactory.createKey("User", userEmail);
      	 DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  	     Query query = new Query("Friendship", userKey).addSort("friendemail", Query.SortDirection.ASCENDING);
  		 List<Entity> friends = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(100));  
      %>
		 <div class="well bs-component">
				<%if(friends.isEmpty()){ %>
					
					<fieldset class="whiteborder">
					
					<p> Sorry, you have no friends </p>
					<p> You can your friends above</p>

					</fieldset>
					
<%				
				}else {
%>
				<table class="table table-striped table-hover ">
					<thead>
						<tr>
							<th> Your Friends</th>
						</tr>
					</thead>
				<tbody>
<%
				for (Entity friend : friends) {
						String viewFriend = friend.getProperty("friendemail").toString();
						%> 
				<tr>
     			 <td><%= viewFriend %></td>
   			    </tr>


	        <%
				}
	    }
	    %> 
	    	</tbody>
		</table>

					
      </div> 
    </div>
   </div>
  <% } else {
	  ChallengesServlet.reDirectToHomepage(response);
} %>

  </body>
</html>