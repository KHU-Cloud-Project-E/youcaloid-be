package com.example.cloud.utils;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {
    private static final List<String> imageContentTypes = Arrays.asList("image/png", "image/jpeg", "image/gif");

    public static boolean isRegexEmail(String target) {
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    ;

    public static boolean isRegexPhone(String target) {
        String regex = "^\\d{2,3}-\\d{3,4}-\\d{4}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    public static boolean isRegexBigInteger(String target) {
        try {
            new BigInteger(target);
        } catch (Exception exception) {
            return false;
        }

        return true;
    }

    public static boolean isRegexDouble(String target) {
        try {
            Double.parseDouble(target);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static boolean isImageFile(MultipartFile file) {
        String fileContentType = file.getContentType();
        return imageContentTypes.contains(fileContentType);
    }
}

