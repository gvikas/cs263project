package enqueue;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

//The Worker servlet should be mapped to the "/worker" URL.

public class Worker extends HttpServlet {
 
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	
protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

	 String challengeKey = request.getParameter("challengeKey");
     String title = request.getParameter("title");
     String description = request.getParameter("description");
     String date = request.getParameter("date");
     String blobKey = request.getParameter("blobKey");
    
     
     Entity challengePost = new Entity("ChallengePost",challengeKey);
     challengePost.setProperty("title", title);
     challengePost.setProperty("description", description);
     challengePost.setProperty("date", date);
     challengePost.setProperty("blobKey", blobKey);
     
    // System.out.println("Worker: ChallengePost:" + challengeKey  + " title:  "+ title +"description: "+ description + "blobKey:" + blobKey);

     DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
     datastore.put(challengePost);
 }
}
