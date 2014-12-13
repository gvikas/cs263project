package enqueue;

//The Enqueue servlet should be mapped to the "/enqueue" URL.
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validation;

import org.apache.commons.lang3.RandomStringUtils;
import org.jsoup.helper.Validate;

import validate.Validator;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

/**
 * This EnqueueServlet requests the parameters from the postChallenge.jsp,
 * and adds them to a TaskQueue
 * @author Vikas
 *
 */

public class EnqueueServlet extends HttpServlet {
	
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	
	/**
	 * This doPost method request parameters to postChallenge.jsp,
	 * and them to a Queue as a task, and sent to WokerServlet.
	 * Afterwards it will redirect the client to viewChallenge.jsp, where the user can view the post
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
		UserService userService = UserServiceFactory.getUserService();
		User currentUser = userService.getCurrentUser();
		String challengeKey = RandomStringUtils.random(16,true,true);
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		String[] sendToFriends = request.getParameterValues("friends");
		
	if(! Validator.isValidName(title) && Validator.isValidName(description)){
		response.sendRedirect("/postChallenge.jsp");
		return;
	}
     
     Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(request);
     BlobKey blobKey = blobs.get("image");
     
     if (blobKey == null) {
         response.sendRedirect("/miss.html");
     }
     
     // Add the task to the default queue.
     Queue queue = QueueFactory.getDefaultQueue();
     queue.add(withUrl("/worker").param("user", currentUser.getEmail()).param("challengeKey", challengeKey).param("title", title).param("description", description).param("blobKey", blobKey.getKeyString()).param("date", new Date().toString()).param("sendToFriends", Arrays.toString(sendToFriends)));
     
     response.sendRedirect("/challengeview.jsp?challengeKey="+challengeKey);

     
 }
}
