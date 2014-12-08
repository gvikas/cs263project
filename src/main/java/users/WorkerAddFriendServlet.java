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

public class WorkerAddFriendServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		      throws ServletException, IOException {
		
		String user = request.getParameter("user");
		String frienduser = request.getParameter("friendEmail");
		
		Key userKey = KeyFactory.createKey("User", user);
		
		Entity friendship = new Entity("Friendship", frienduser, userKey);
			friendship.setProperty("friendemail", frienduser);
			
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		datastore.put(friendship);
		
	}
}
