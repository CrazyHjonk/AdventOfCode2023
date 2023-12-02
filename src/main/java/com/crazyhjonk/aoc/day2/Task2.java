package com.crazyhjonk.aoc.day2;

import com.crazyhjonk.aoc.day1.Task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Task2 {

    public static void main(String[] args) {
        InputStream input = Task1.class.getClassLoader().getResourceAsStream("input2.1.txt");
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
        System.out.println("Part 1: " + lines.stream().map(Task2::processLine).mapToInt(Integer::intValue).sum());
        System.out.println("Part 2: " + lines.stream().map(Task2::processLinePt2).mapToInt(Integer::intValue).sum());
    }

    public static int processLine(String line) {
        int id = Integer.parseInt(line.split(": ")[0].split(" ")[1]);
        line = line.split(": ")[1];
        boolean valid = true;
        String[] pulls = line.split("; ");
        for (String pull : pulls) {
            String[] cubeEntries = pull.split(", ");
            for (String cubeEntry : cubeEntries) {
                String[] cubeEntrySplit = cubeEntry.split(" ");
                switch (cubeEntrySplit[1]) {
                    case "red" -> {
                        if (Integer.parseInt(cubeEntrySplit[0]) > 12) {
                            valid = false;
                        }
                    }
                    case "green" -> {
                        if (Integer.parseInt(cubeEntrySplit[0]) > 13) {
                            valid = false;
                        }
                    }
                    case "blue" -> {
                        if (Integer.parseInt(cubeEntrySplit[0]) > 14) {
                            valid = false;
                        }
                    }
                }
            }
        }
        return valid ? id : 0;
    }

    public static int processLinePt2(String line) {
        line = line.split(": ")[1];
        int red, green, blue;
        red = green = blue = 0;

        String[] pulls = line.split("; ");
        for (String pull : pulls) {
            String[] cubeEntries = pull.split(", ");
            for (String cubeEntry : cubeEntries) {
                String[] cubeEntrySplit = cubeEntry.split(" ");
                switch (cubeEntrySplit[1]) {
                    case "red" -> {
                        if (Integer.parseInt(cubeEntrySplit[0]) > red) {
                            red = Integer.parseInt(cubeEntrySplit[0]);
                        }
                    }
                    case "green" -> {
                        if (Integer.parseInt(cubeEntrySplit[0]) > green) {
                            green = Integer.parseInt(cubeEntrySplit[0]);
                        }
                    }
                    case "blue" -> {
                        if (Integer.parseInt(cubeEntrySplit[0]) > blue) {
                            blue = Integer.parseInt(cubeEntrySplit[0]);
                        }
                    }
                }
            }
        }
        return red * green * blue;
    }
}
