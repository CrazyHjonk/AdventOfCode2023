package com.crazyhjonk.aoc.day6;

import com.crazyhjonk.aoc.day1.Task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task6 {

    public static void main(String[] args) {
        InputStream input = Task1.class.getClassLoader().getResourceAsStream("input6.1.txt");
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

            System.out.println("Part 1: " + part1(lines));
            System.out.println("Part 2: " + task2(lines));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int part1(List<String> lines) {
        int[] times = processLine(lines.get(0));
        int[] distances = processLine(lines.get(1));

        int result = 1;
        for (int i = 0; i < times.length; i++) {
            result *= calcRace(times[i], distances[i]);
        }
        return result;
    }

    private static int[] processLine(String line) {
        String[] split = Arrays.stream(line.split(": ")[1].split(" +"))
            .filter(s -> !s.isEmpty()).toArray(String[]::new);
        int[] numbers = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            numbers[i] = Integer.parseInt(split[i]);
        }
        return numbers;
    }

    private static int calcRace(int time, int distance) {
        int sum = 0;
        for (int i = 0; i < time; i++) {
            int timeCharging = i;
            int timeTravelling = time - i;
            int velocity = timeCharging;
            //for (int j = 1; j <= timeCharging; j++) {
            //    velocity++;
            //}
            if (velocity * timeTravelling >= distance) {
                sum++;
            }
        }
        return sum;
    }


    private static long task2(List<String> lines) {
        long time = processLine2(lines.get(0));
        long distance = processLine2(lines.get(1));
        return calcRace(time, distance);
    }

    private static long processLine2(String line) {
        String[] split = Arrays.stream(line.split(": ")[1].split(" +"))
            .filter(s -> !s.isEmpty()).toArray(String[]::new);
        long number = Arrays.stream(split).reduce(String::concat).stream().mapToLong(Long::parseLong).sum();
        System.out.println(number);
        return number;
    }

    private static long calcRace(long time,long distance) {
        long sum = 0;
        for (long i = 0; i < time; i++) {
            long timeCharging = i;
            long timeTravelling = time - i;
            long velocity = timeCharging;
            //for (int j = 1; j <= timeCharging; j++) {
            //    velocity++;
            //}
            if (velocity * timeTravelling >= distance) {
                sum++;
            }
        }
        return sum;
    }
}
