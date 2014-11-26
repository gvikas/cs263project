package enqueue;

//The Enqueue servlet should be mapped to the "/enqueue" URL.
import java.io.IOException;
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
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

import cs263project.cs263project.KeyGenerator;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;



public class Enqueue extends HttpServlet {
	
	protected KeyGenerator challengeID = new KeyGenerator(0);
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
     String challengeKey = RandomStringUtils.random(16,true,true);
		//Key challengeKey = KeyFactory.ma
     String title = request.getParameter("title");
     String description = request.getParameter("description");
     
     Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(request);
     BlobKey blobKey = blobs.get("image");
     
     //System.out.println("Blobkey of image:" + blobKey);
     //System.out.println("Enqueue: challengeKey: " + challengeKey + "title:  "+ title + "description: " + description);
     // Add the task to the default queue.
     Queue queue = QueueFactory.getDefaultQueue();
     queue.add(withUrl("/worker").param("challengeKey", challengeKey).param("title", title).param("description", description).param("blobKey", blobKey.getKeyString()).param("date", new Date().toString()));
 
     //response.sendRedirect("/done.html");
     //response.sendRedirect("/test");
     //response.sendRedirect("/tqueue.jsp?key="+key);
     
     if (blobKey == null) {
         response.sendRedirect("/");
     } else {
         //response.sendRedirect("/challenges");
         response.sendRedirect("/challengeview.jsp?challengeKey="+challengeKey);
     }
 }
}
