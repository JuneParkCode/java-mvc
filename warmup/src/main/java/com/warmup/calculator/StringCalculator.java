package com.warmup.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {
    public StringCalculator() {
    }

    public int add(String text) throws RuntimeException {
        if (text == null || text.length() == 0) return 0;
        final String delimiters = parseDelimiters(text);
        final String command = parseCommandString(text);
        final List<Integer> numbers = parseNumbersFromText(command, delimiters);

        if (numbers.size() == 0)
            throw new RuntimeException("number format is invalid");
        return sum(numbers);
    }

    private List<Integer> parseNumbersFromText(String text, String delimiters) throws RuntimeException {
        final String regex = String.format("[%s]", delimiters);
        final String[] stringNumbers = text.split(regex);
        final List<Integer> result = new ArrayList<>(stringNumbers.length);

        for (String number : stringNumbers) {
            result.add(Integer.parseUnsignedInt(number));
        }
        return result;
    }

    private String parseCommandString(String text) {
        Matcher matcher = Pattern.compile("//.\n(.*)").matcher(text);

        if (!matcher.find())
            return text;
        return matcher.group(1);
    }

    private String parseDelimiters(String text) {
        final String DEFAULT_DELIMITERS = ",:";
        return parseDelimiters(text, DEFAULT_DELIMITERS);
    }
    private String parseDelimiters(String text, String defaultDelimiter) {
        Matcher matcher = Pattern.compile("//(.)\n.*").matcher(text);

        if (!matcher.find())
            return defaultDelimiter;
        return defaultDelimiter + matcher.group(1);
    }

    private int sum(List<Integer> numbers) {
        int result = 0;
        for (Integer number : numbers) {
            result += number;
        }
        return result;
    }
}
