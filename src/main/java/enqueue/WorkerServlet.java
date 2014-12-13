package enqueue;

import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.memcache.AsyncMemcacheService;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * This is a WorkerServlet which stores challenges in DataStore and Memcache
 * @author Vikas
 *
 */
public class WorkerServlet extends HttpServlet {
	
/**
 * This doPost method requests parameters from the Queue.
 * It makes an Entity called ChallengePost, and sets the parameters to properties, 
 * The ChallengePost Entity is been put in both datastore and memcache.
 * The reason for the Entity is been put in memcache is the challenge is fresh and new, 
 * and might be popular and it is important to get the challenge fast.
 * The is also one more entity called ReceivedChallenges, which holds on which properties
 * fromUser, toUser, and ChallengeKey and Date. This can be seen as a lookup-table for 
 * looking up which users received which challenges. 
 */
protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
	 DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	 AsyncMemcacheService asyncCache = MemcacheServiceFactory.getAsyncMemcacheService();
	 asyncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	 
	 String challengeKey = request.getParameter("challengeKey");
	 String user = request.getParameter("user");
     String title = request.getParameter("title");
     String description = request.getParameter("description");
     String date = request.getParameter("date");
     String blobKey = request.getParameter("blobKey");
     String[] sendToFriends = request.getParameter("sendToFriends").substring(1, request.getParameter("sendToFriends").length()-1).split(",");
     Boolean showChallenge = true; 
     
     Entity challengePost = new Entity("ChallengePost",challengeKey);
     challengePost.setProperty("user", user);
     challengePost.setProperty("title", title);
     challengePost.setProperty("description", description);
     challengePost.setProperty("date", date);
     challengePost.setProperty("blobKey", blobKey);
     datastore.put(challengePost);
     asyncCache.put(challengeKey, challengePost);
     
     for (int i = 0; i < sendToFriends.length; i++) {
    	 Entity receivedChallenges = new Entity("ReceivedChallenges");
    	 receivedChallenges.setProperty("fromUser", user);
    	 receivedChallenges.setProperty("toUser", sendToFriends[i].trim());
    	 receivedChallenges.setProperty("challengeKey", challengeKey);
    	 challengePost.setProperty("date", date);
    	 receivedChallenges.setProperty("showChallenge", showChallenge);
    	 datastore.put(receivedChallenges);
	}
     
 }
}
