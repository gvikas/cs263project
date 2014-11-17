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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

public class ReadData extends HttpServlet {
	
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	ImagesService imagesService = ImagesServiceFactory.getImagesService();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		response.getWriter().println("Hello, this is a Challenge List. \n\n");
		
       // BlobKey blobKey = new BlobKey(request.getParameter("image"));
       //  blobstoreService.serve(blobKey, response);

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//   // Key taskDataKey = KeyFactory.createKey("Guestbook", guestbookName);
//    // Run an ancestor query to ensure we see the most up-to-date
//    // view of the Greetings belonging to the selected Guestbook.
		Query query = new Query("ChallengePost").addSort("date", Query.SortDirection.DESCENDING);
		List<Entity> challenges = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
		
		String message = "<!DOCTYPE html><html><body>";
		for (Entity challenge : challenges) {
			String printKey = challenge.getKey().toString();
			String printTitle = challenge.getProperty("title").toString();
			String printValue = challenge.getProperty("description").toString();
			String printDate = challenge.getProperty("date").toString();
	        BlobKey blobKey = new BlobKey((String) challenge.getProperty("blobKey"));
	        String imageUrl = imagesService.getServingUrl(blobKey);
	      
			
			message += "<p>"+printKey+": "+ "Title: " + printTitle + " Description: " + printValue + ", Date: "+printDate+"</p>"+ "<img src=\""+imageUrl+"\" height=42 width=42>";
		    	      
	        System.out.println("Key"+printKey+": "+ "Title: " + printTitle + " Description: " + printValue + ", Date: "+printDate+"blobKey :" +blobKey);
		}
		
		message+="</body></html>";
       

		
		PrintWriter out = response.getWriter();
		out.println(message);
     
	}
}
