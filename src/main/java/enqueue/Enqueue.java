package enqueue;

//The Enqueue servlet should be mapped to the "/enqueue" URL.
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

public class Enqueue extends HttpServlet {
 protected void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
     String challengeKey;
     String title = request.getParameter("title");
     String description = request.getParameter("description");
     
     
     System.out.println("Enqueue: title:  "+ title + "description: " + description);
     // Add the task to the default queue.
     Queue queue = QueueFactory.getDefaultQueue();
     queue.add(withUrl("/worker").param("title", title).param("description", description).param("date", new Date().toString()));
 

     response.sendRedirect("/done.html");
     //response.sendRedirect("/test");
     //response.sendRedirect("/tqueue.jsp?key="+key);
 }
}
