package users;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


/**
 * This WorkerAddFriendServlet requests the friend email from the Queue.
 * The logged-in user becomes the key, and the friend email become a property. 
 * This is being put in the datastore, as a friendship entity. 
 * 
 */

public class WorkerAddFriendServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		      throws ServletException, IOException {
		 DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		
		String user = request.getParameter("user");
		String frienduser = request.getParameter("friendEmail");
		
		Key userKey = KeyFactory.createKey("User", user);
		
		Entity friendship = new Entity("Friendship", frienduser, userKey);
			friendship.setProperty("friendemail", frienduser);
			
		
		
		datastore.put(friendship);
	}
}
