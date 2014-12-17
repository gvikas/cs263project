package validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is for validating the email-address.
 * 
 * @author Vikas
 *
 */
public class Validator {

	private static final Pattern emailPATTERN = Pattern.compile("(?:[-_a-zA-Z0-9]+@[-_a-zA-Z0-9]+\\.[a-z]{3}(?:,(?!$))?)+");
		
	public static boolean isValidEmail(String email) {
        Matcher emailMatcher = emailPATTERN.matcher(email);
        return emailMatcher.matches() && email.length()>0;
	}

}
