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
	 DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	 
	 String challengeKey = request.getParameter("challengeKey");
	 String user = request.getParameter("user");
     String title = request.getParameter("title");
     String description = request.getParameter("description");
     String date = request.getParameter("date");
     String blobKey = request.getParameter("blobKey");
     String[] sendToFriends = request.getParameter("sendToFriends").substring(1, request.getParameter("sendToFriends").length()-1).split(",");
     
     
     
    
     
     Entity challengePost = new Entity("ChallengePost",challengeKey);
     challengePost.setProperty("user", user);
     challengePost.setProperty("title", title);
     challengePost.setProperty("description", description);
     challengePost.setProperty("date", date);
     challengePost.setProperty("blobKey", blobKey);
     
     for (int i = 0; i < sendToFriends.length; i++) {
		System.out.println(sendToFriends[i].toString());
	}
     
     for (int i = 0; i < sendToFriends.length; i++) {
    	 Entity receivedChallenges = new Entity("ReceivedChallenges");
    	 receivedChallenges.setProperty("fromUser", user);
    	 receivedChallenges.setProperty("toUser", sendToFriends[i].trim());
    	 receivedChallenges.setProperty("challengeKey", challengeKey);
    	 datastore.put(receivedChallenges);
	}
     
     
     
    // System.out.println("Worker: ChallengePost:" + challengeKey  + " title:  "+ title +"description: "+ description + "blobKey:" + blobKey);
     datastore.put(challengePost);
     
 }
}
