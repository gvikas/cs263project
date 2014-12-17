package users;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * This is just a simple UserHelper. 
 * @author Vikas
 *
 */
public class UserHelper {
	
	private static User currentUser;
	private static UserService userService;

	private static void thisCurrentUser(){
		userService = UserServiceFactory.getUserService();
		currentUser = userService.getCurrentUser();
	}
	
	public static String getLogoutURL(String currentURL){
		thisCurrentUser();
		return userService.createLogoutURL(currentURL);
	}
	
	public static String getLoginURL(String currentURL){
		thisCurrentUser();
		return userService.createLoginURL(currentURL);
	}
	
	public static String getUserEmail(){
		thisCurrentUser();
		return currentUser != null ? currentUser.getEmail() : null;
	}
	
	public static String getUserNickname(){
		thisCurrentUser();
		return currentUser.getNickname();
	}

	public static String getCurrentUserId(){
		thisCurrentUser();
		return currentUser != null ? currentUser.getUserId() : null;
	}
	
	public static boolean isThisUserLoggedIn(){
		return userService.isUserLoggedIn();
	}
	
	public static User getUser(){
		return currentUser;
	}
	
	public static boolean isItAnotherUser(String loggedInUser, Object user){
	 return !(loggedInUser.equals(user));
	}

}
