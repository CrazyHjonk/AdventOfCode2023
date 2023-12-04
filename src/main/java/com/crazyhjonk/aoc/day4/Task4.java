package com.crazyhjonk.aoc.day4;

import com.crazyhjonk.aoc.day1.Task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Task4 {

    private static int lineIndex = 1;
    private static final Map<Integer, Integer> amountOfCards = new HashMap<>();

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
        int processedCards = 0;

        String numbers = line.split(": ")[1];
        String[] numbersSplit = numbers.split(" \\| ");
        int[] winningsNumbers = Arrays.stream(numbersSplit[0].split(" "))
            .filter(s -> !s.isEmpty()).mapToInt(Integer::parseInt).toArray();
        int[] foundNumbers = Arrays.stream(numbersSplit[1].split(" "))
            .filter(s -> !s.isEmpty()).mapToInt(Integer::parseInt).toArray();

        int score = 0;
        for (int number : foundNumbers) {
            if (Arrays.stream(winningsNumbers).anyMatch(i -> i == number)) {
                score++;
            }
        }
        processedCards++; //count the original card

        for (int i = 1; i <= score; i++) {
            increaseOrPlace(lineIndex + i, amountOfCards.getOrDefault(lineIndex, 1));
            // only original cards are processed in reality, therefore add the copied cards' amount to score when they're created
            processedCards+= amountOfCards.getOrDefault(lineIndex, 1);
        }
        lineIndex++;

        return processedCards;
    }

    private static void increaseOrPlace(int key, int amount) {
        amountOfCards.put(key, amountOfCards.getOrDefault(key, 1) + amount);
    }
}
