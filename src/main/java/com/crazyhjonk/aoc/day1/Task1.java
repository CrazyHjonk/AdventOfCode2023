package com.crazyhjonk.aoc.day1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task1 {

    static Map<String, String> map = Map.of(
        "one", "1",
        "two", "2",
        "three", "3",
        "four", "4",
        "five", "5",
        "six", "6",
        "seven", "7",
        "eight", "8",
        "nine", "9"
    );

    public static void main(String[] args) {
        InputStream input = Task1.class.getClassLoader().getResourceAsStream("input1.1.txt");
        assert input != null;

        List<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        try {
            String line = reader.readLine();

            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Part 1: " + lines.stream().map(Task1::processLine).mapToInt(Integer::intValue).sum());
        System.out.println("Part 2: " + lines.stream().map(Task1::processLinePt2).mapToInt(Integer::intValue).sum());
    }

    private static int processLine(String line) {
        List<Integer> numbers = new ArrayList<>();
        char[] charArray = line.toCharArray();
        for (char c : charArray) {
            if (!Character.isDigit(c)) continue;
            numbers.add(Integer.parseInt(Character.toString(c)));
        }
        return Integer.parseInt("" + numbers.get(0) + numbers.get(numbers.size() - 1));
    }

    private static int processLinePt2(String line) {
        // index | value
        Map<Integer, Integer> numbers = new HashMap<>();
        char[] charArray = line.toCharArray();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Pattern pattern = Pattern.compile(entry.getKey());
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                numbers.put(matcher.start(), Integer.parseInt(entry.getValue()));
            }
        }
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (!Character.isDigit(c)) continue;
            numbers.put(i, Integer.parseInt(Character.toString(c)));
        }
        List<Integer> values = new ArrayList<>(numbers.entrySet().stream().sorted(Map.Entry.comparingByKey())
            .map(Map.Entry::getValue).toList());
        return Integer.parseInt("" + values.get(0) + values.get(values.size() - 1));
    }
}
