package validate;

import java.util.regex.Pattern;

public class Validator {

	private static final Pattern namePattern = Pattern.compile("[^a-zA-Z0-9]+$");
	
	public static boolean isValidName(String name) {
        boolean hasSpecialChar = namePattern.matcher(name).find();
        return !hasSpecialChar && name.length()>0;
}
}
