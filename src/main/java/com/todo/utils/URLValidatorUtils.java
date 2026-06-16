package com.todo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLValidatorUtils {

    private static final String URL_PATTERN = "^(https?)://(?:[\\w.-]+(?:\\.[\\w.-]+)*|\\[[\\da-fA-F:]+])(?::\\d{1,5})?(?:/\\S*)?$";
    private static final Pattern pattern = Pattern.compile(URL_PATTERN);

    public static boolean isValid(String url) {
        if (url == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }
}
