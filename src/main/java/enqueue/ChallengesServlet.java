package enqueue;

//The Enqueue servlet should be mapped to the "/enqueue" URL.
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;

/**
 * This ChallengesServlet handles few functions for the application.
 * It checks if a challenges belongs to a user. 
 * It updated the receiving challenges list for users by removing 
 * those challenges who have be accepted or deleted by the them false. 
 * @author Vikas
 *
 */
public class ChallengesServlet extends HttpServlet {
	ImagesService imagesService = ImagesServiceFactory.getImagesService();
	
	/**
	 * This method checks if a challenges belongs to a specific user.
	 * This is done by checking if the user is listed in the receiving challenges. 
	 * Then it checks if the challengesKey exists for the specific user.  
	 * @param cKey
	 * @param user
	 * @return
	 */
	public static boolean belongPosttoUser(String cKey, String user){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		 Filter filter = new FilterPredicate("toUser",FilterOperator.EQUAL,user);
	     Query query = new Query("ReceivedChallenges").addSort("toUser", Query.SortDirection.DESCENDING).setFilter(filter);
	     List<Entity> challengeKeysList = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(20));
		 for (Entity challKey : challengeKeysList){
			 if(challKey.getProperty("challengeKey").toString().equals(cKey)){
				 return true;
			 }
		 }
		 return false;
	}
	
	public static void reDirectToHomepage (HttpServletResponse response) throws IOException, ServletException { 
			response.sendRedirect("/");
		    response.setStatus(response.SC_MOVED_TEMPORARILY);
		    response.setHeader("Location", "/");
	}
	
	/**
	 * This doPut method is invoked only when a user accepts or deletes a challenges from the receiving challenges 
	 * What happens is that the look up the post that belongs to the user and sets the showChallenge-property to false.
	 * Next time the user goes to Challenges, the user will not see the challenges he/she have accepted or deleted. 
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String challengeKey = request.getParameter("challengeKey");
		String user = request.getParameter("user");
		if (challengeKey == null || challengeKey.isEmpty()){
			response.sendRedirect("/miss.html");
		}
		else{ 
			 DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			 Filter filter = new FilterPredicate("toUser",FilterOperator.EQUAL,user);
		     Query query = new Query("ReceivedChallenges").addSort("toUser", Query.SortDirection.DESCENDING).setFilter(filter);
			 List<Entity> challengeKeysList = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(20));
			 for (Entity challKey : challengeKeysList){
				 if(challKey.getProperty("challengeKey").toString().equals(challengeKey)){
					 Boolean showChallenge = false;
					 long cKey = challKey.getKey().getId();
					 Entity receivedChallenge;
					try {
						receivedChallenge = datastore.get(KeyFactory.createKey("ReceivedChallenges", cKey));
						receivedChallenge.setProperty("showChallenge", showChallenge);
						datastore.put(receivedChallenge);
					} catch (EntityNotFoundException e) {
						e.printStackTrace();
					}				 
				 }
			 }
		}
	}
	
}
