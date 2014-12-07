<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<!DOCTYPE html>
<html>
  <head>
    <title>uChallenge</title>
    
    <link rel="stylesheet" type="text/css" href="stylesheet/bootstrap.css"> <!-- Customizes--> 
   
    <!-- <link rel="stylesheet" type="text/css" href="stylesheet/bootstrap.css"> -->
   <link rel="stylesheet" type="text/css" href="stylesheet/bootstrap-responsive.css">
   

   <script src="/js/fileInput.js" type="text/javascript"></script>

  </head>
  <body>
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
      	<div class="col-lg-6">
   		   <input class="btn btn-primary" type="submit" name="Submt" value="Submit" >
   		</div>
   	  </div>
  </fieldset>

    </form>
      	</div>
      </div>
    </div>
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