package genieus.com.walla.v2.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by anesu on 8/21/17.
 */

public class RegexUtils {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isValidEmail(final String email){
        if (email == null || email.isEmpty()) {
            return false;
        }

        final Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }
}
