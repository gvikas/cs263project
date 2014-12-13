<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
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
<%@ include file="comp/navbar.html" %>

<% 
	response.setHeader("X-XSS-Protection","1; mode=block");
  	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
    String userEmail = user.getEmail();
	Key userKey = KeyFactory.createKey("User", userEmail);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  	Query query = new Query("Friendship", userKey).addSort("friendemail", Query.SortDirection.ASCENDING);
  	List<Entity> friends = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));  
  	
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<!DOCTYPE html>
<html>
  <head>
    <title>uChallenge</title>
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
   	 <link rel="stylesheet" type="text/css" href="stylesheet/bootstrap.css"> <!-- Customized bootstrap--> 	
  	 <link rel="stylesheet" type="text/css" href="stylesheet/bootstrap-responsive.css">

  </head>
  <body>
    <%  if (request.getUserPrincipal() != null){ %>  
  	<div class="container">
    <div class="col-lg-6 col-lg-offset-3">
      <div class="well bs-component">
  	 <form class="form-horizontal" action="<%= blobstoreService.createUploadUrl("/enqueue")%>" id="createChallenge" method="post" enctype="multipart/form-data">
     <fieldset>
      <legend>Create your challenge</legend>
   	  <div class="form-group">
     	 <div class="col-lg-6">
     		 <input type="text" class="form-control" id="title" placeholder="Title" name="title">
     	 </div>
  		<br>
   	  </div> 

   	  <div class="form-group">
      	<div class="col-lg-6">
     		 <textarea class="form-control" style="resize:none" cols="40" rows="5" id="description" placeholder="Description" name="description" ></textarea>
     	</div>
   	  </div>

   	  <br>
 
      <div class="form-group">
      	<div class="col-lg-6">
      	<input class="btn btn-primary" id="image" type="file" name="image" accept="image/*|video/*;capture=camera">
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
   		   <input class="btn btn-primary" type="submit" name="Submit" value="Submit" >
   		</div>
   	  </div>
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

<!-- Validation script for the form --> 
<script type="text/javascript" src="/js/bootstrap.js"></script>
<script type="text/javascript" src="/js/jquery-1.11.1.js"></script>
<script type="text/javascript" src="/js/jquery.validate.js"></script>
<script type="text/javascript" src="http://malsup.github.io/jquery.form.js"></script>
    <script type="text/javascript">
    $(document).ready(function(){
      $("#createChallenge").validate({
        rules:{
          title:"required",
          description:"required",
          image:{
            required:true,
            accept: "image/*|video/*"

          }

          },
        messages:{
          title:"Enter a title",
          description:"Enter a description",
          image:"Please choose a image or video to send"
        },
        errorClass: "help-inline",
        errorElement: "div",
        highlight:function(element, errorClass, validClass) {
          $(element).parents('.form-group').addClass('error');
        },
        unhighlight: function(element, errorClass, validClass) {
          $(element).parents('.form-group').removeClass('error');
          $(element).parents('.form-group').addClass('success');
        }
      });
    });
    </script>

  </body>
</html>