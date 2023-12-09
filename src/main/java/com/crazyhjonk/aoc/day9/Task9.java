package com.crazyhjonk.aoc.day9;

import com.crazyhjonk.aoc.day1.Task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Task9 {

    public static void main(String[] args) {
        InputStream input = Task1.class.getClassLoader().getResourceAsStream("input9.1.txt");
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

            System.out.println("Part 1: " + lines.stream().map(Task9::processLine).mapToLong(Long::longValue).sum());
            System.out.println("Part 2: " + lines.stream().map(Task9::processLine2).mapToLong(Long::longValue).sum());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static long processLine(String line) {
        List<Long> numbers = Arrays.stream(line.split(" "))
            .filter(s -> !s.isEmpty()).map(Long::parseLong).toList();
        List<Long> lastNumbers = new ArrayList<>(); // List of the last numbers in each line
        List<Long> currentNumbers = numbers;

        while (true) {
            lastNumbers.add(currentNumbers.get(currentNumbers.size() - 1));
            if (currentNumbers.stream().allMatch(l -> l == 0)) break;
            List<Long> increments = new ArrayList<>();
            for (int i = 0; i < currentNumbers.size() - 1; i++) {
                increments.add(currentNumbers.get(i + 1) - currentNumbers.get(i));
            }
            currentNumbers = increments;
        }
        long extrapolatedNumber = 0;
        for (int i = lastNumbers.size() - 1; i >= 0; i--) {
            extrapolatedNumber = extrapolatedNumber + lastNumbers.get(i);
        }

        return extrapolatedNumber;
    }

    private static long processLine2(String line) {
        List<Long> numbers = new ArrayList<>(Arrays.stream(line.split(" "))
            .filter(s -> !s.isEmpty()).map(Long::parseLong).toList());

        Collections.reverse(numbers);
        return processLine(numbers.stream().map(Object::toString).reduce("", (s, s2) -> s + s2 + " "));
    }
}
