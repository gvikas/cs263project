<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<!DOCTYPE html>
<html>
  <head>
    <title>uChallenge</title>
    
    <link rel="stylesheet" type="text/css" href="stylesheet/bootstrap.css"> <!-- For testing purpose--> 
   <script src="/js/fileInput.js" type="text/javascript"></script>

  </head>
  <body>
  	<div class="container">

    <div class="col-lg-6 col-lg-offset-3">
      <div class="well bs-component">
  	 <form class="form-horizontal" action="<%= blobstoreService.createUploadUrl("/enqueue")%>" method="post" enctype="multipart/form-data">
     <fieldset>
      <legend>Create your challenge</legend>
   	  <div class="form-group">
     	 <div class="col-lg-10">
     		 <input type="text" class="form-control" placeholder="Title" name="title">
     	 </div>
  		<br>
   	  </div> 

   	  <div class="form-group">
      	<div class="col-lg-10">
     		 <textarea class="form-control" style="resize:none" cols="40" rows="5" placeholder="Description" name="description"></textarea>
     	</div>
   	  </div>

   	  <br>
 
      <div class="form-group">
      	<div class="col-lg-10">
      	<input class="btn btn-primary" type="file" name="image" accept="image/*;capture=camera">
      	</div>
      </div>

      <div class="form-group">
      	<div class="col-lg-10">
   		   <input class="btn btn-primary" type="submit" name="Submt" value="Submit" >
   		</div>
   	  </div>
  </fieldset>

    </form>
      	</div>
      </div>
    </div>
  </body>
</html>