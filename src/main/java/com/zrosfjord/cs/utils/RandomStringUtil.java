package com.zrosfjord.cs.utils;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RandomStringUtil {

    public static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER = UPPER.toLowerCase(Locale.ENGLISH);
    public static final String NUMERIC = "0123456789";
    public static final String ALPHA_NUM = UPPER + LOWER + NUMERIC;

    /**
     * @param codeLength
     * @param id
     * @return
     */
    public static String createRandomCode(int codeLength, String id, String regex, String replacement) {
        List<Character> temp = id.chars()
                .mapToObj(i -> (char) i)
                .collect(Collectors.toList());
        Collections.shuffle(temp, new SecureRandom());

        String str = temp.stream()
                .map(Object::toString)
                .limit(codeLength)
                .collect(Collectors.joining());

        str = str.replaceFirst(regex, replacement);

        return str;
    }

}
