package enqueue;

//The Enqueue servlet should be mapped to the "/enqueue" URL.
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

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
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

public class ChallengesServlet extends HttpServlet {
	ImagesService imagesService = ImagesServiceFactory.getImagesService();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		response.getWriter().println("Hello, this is a Challenge List. \n\n");
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//    // Run an ancestor query to ensure we see the most up-to-date
//    // view of the Greetings belonging to the selected Guestbook.
		Filter filter = new FilterPredicate("user",FilterOperator.EQUAL,user.getEmail());
		Query query = new Query("ChallengePost").addSort("date", Query.SortDirection.DESCENDING).setFilter(filter);
		List<Entity> challenges = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
		
		String message = "<!DOCTYPE html><html><body>";
		for (Entity challenge : challenges) {
			String printKey = challenge.getKey().toString();
			String printTitle = challenge.getProperty("title").toString();
			String printValue = challenge.getProperty("description").toString();
			String printDate = challenge.getProperty("date").toString();
	        BlobKey blobKey = new BlobKey(challenge.getProperty("blobKey").toString());
	        String imageUrl = imagesService.getServingUrl(blobKey);
	      		
			message += "<p>"+printKey+": "+ "Title: " + printTitle + " Description: " + printValue + ", Date: "+printDate+"</p>"+ "<img src=\""+imageUrl+"\" height=42 width=42>";
		    	      
	        //System.out.println("Key"+printKey+": "+ "Title: " + printTitle + " Description: " + printValue + ", Date: "+printDate+"blobKey :" +blobKey);
		}	
		message+="</body></html>";
		PrintWriter out = response.getWriter();
		out.println(message);   
	}
	
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
			 List<Entity> challengeKeysList = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
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
