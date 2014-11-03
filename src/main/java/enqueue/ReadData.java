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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

public class ReadData extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		response.getWriter().println("Hello, this is a TaskData List. \n\n");
		//String key = request.getParameter("key");
		//String value = request.getParameter("value");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//   // Key taskDataKey = KeyFactory.createKey("Guestbook", guestbookName);
//    // Run an ancestor query to ensure we see the most up-to-date
//    // view of the Greetings belonging to the selected Guestbook.
		Query query = new Query("TaskData").addSort("date", Query.SortDirection.DESCENDING);
		List<Entity> tasks = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
		
		String message = "";
		for (Entity task : tasks) {
			String printKey = task.getKey().toString();
			String printValue = task.getProperty("value").toString();
			String printDate = task.getProperty("date").toString();
			
			message += "<p>"+printKey+": "+ printValue + ", Date: "+printDate+"</p>";
		}
		
		
		PrintWriter out = response.getWriter();
		out.println(message);
     
	}
}
