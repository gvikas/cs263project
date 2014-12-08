package enqueue;

//The Enqueue servlet should be mapped to the "/enqueue" URL.
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;


public class Enqueue extends HttpServlet {
	
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
		UserService userService = UserServiceFactory.getUserService();
		User currentUser = userService.getCurrentUser();
     String challengeKey = RandomStringUtils.random(16,true,true);
     String title = request.getParameter("title");
     String description = request.getParameter("description");
     String[] sendToFriends = request.getParameterValues("friends");
     
     Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(request);
     BlobKey blobKey = blobs.get("image");
     //System.out.println("Enqueue: challengeKey: " + challengeKey + "title:  "+ title + "description: " + description);
     // Add the task to the default queue.
     Queue queue = QueueFactory.getDefaultQueue();
     queue.add(withUrl("/worker").param("user", currentUser.getEmail()).param("challengeKey", challengeKey).param("title", title).param("description", description).param("blobKey", blobKey.getKeyString()).param("date", new Date().toString()).param("sendToFriends", Arrays.toString(sendToFriends)));

     if (blobKey == null) {
         response.sendRedirect("/miss.html");
     } else {
         //response.sendRedirect("/challenges");
         response.sendRedirect("/challengeview.jsp?challengeKey="+challengeKey);
     }
 }
}
