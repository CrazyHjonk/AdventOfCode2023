package com.crazyhjonk.aoc.day4;

import com.crazyhjonk.aoc.day1.Task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task4 {

    public static void main(String[] args) {
        InputStream input = Task1.class.getClassLoader().getResourceAsStream("input4.1.txt");
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
        System.out.println("Part 1: " + lines.stream().map(Task4::processLine).mapToInt(Integer::intValue).sum());
        System.out.println("Part 2: " + lines.stream().map(Task4::processLinePt2).mapToInt(Integer::intValue).sum());
    }

    private static int processLine(String line) {
        String numbers = line.split(": ")[1];
        String[] numbersSplit = numbers.split(" \\| ");
        int[] winningsNumbers = Arrays.stream(numbersSplit[0].split(" "))
            .filter(s -> !s.isEmpty()).mapToInt(Integer::parseInt).toArray();
        int[] foundNumbers = Arrays.stream(numbersSplit[1].split(" "))
            .filter(s -> !s.isEmpty()).mapToInt(Integer::parseInt).toArray();

        int score = 0;
        for (int number : foundNumbers) {
            if (Arrays.stream(winningsNumbers).anyMatch(i -> i == number)) {
                score = score == 0 ? score + 1 : score * 2;
            }
        }
        return score;
    }

    private static int processLinePt2(String line) {
        return 0;
    }
}
