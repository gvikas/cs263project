package enqueue;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;


//The Worker servlet should be mapped to the "/worker" URL.

public class Worker extends HttpServlet {
 protected void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
	 String challengeKey;
     String title = request.getParameter("title");
     String description = request.getParameter("description");
     String date = request.getParameter("date");
     
     Entity challengePost = new Entity("ChallengePost", challengeKey);
     challengePost.setProperty("title", title);
     challengePost.setProperty("description", description);
     challengePost.setProperty("date", date);
     
     System.out.println("Worker: title:  "+ title +"description: "+ description);

     DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
     datastore.put(taskdata);
 }
}
