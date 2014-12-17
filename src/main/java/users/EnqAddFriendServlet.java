package users;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import validate.Validator;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * This EnqAddFriendServlet requests the parameters from the friends.jsp,
 * and adds them to a TaskQueue
 * @author Vikas
 *
 */
public class EnqAddFriendServlet extends HttpServlet {
	
	/**
	 * This doPost method request parameters to friends.jsp,
	 * validate the friendEmail by Validator-class and Jsoup check for XSS and malicious content. 
	 * and them to a Queue as a task, and sent to WorkerAddFriendServlet.
	 * Afterwards it will redirect the client to friends.jsp, where the user can view their friendslist
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	         throws ServletException, IOException { 
		UserService userService = UserServiceFactory.getUserService();
		User currentUser = userService.getCurrentUser();
		String friendEmail = request.getParameter("user_mail").trim();
		
		if (!(Jsoup.isValid(friendEmail, Whitelist.none()) && Validator.isValidEmail(friendEmail))) {
			response.sendRedirect("/friends.jsp");
			return;
		}

		Queue queue = QueueFactory.getDefaultQueue();
	    queue.add(withUrl("/friendworker").param("user", currentUser.getEmail()).param("friendEmail",friendEmail));
	    
	    response.sendRedirect("/friends.jsp");
	}
}
