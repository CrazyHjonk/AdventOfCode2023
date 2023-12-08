package com.crazyhjonk.aoc.day8;

import com.crazyhjonk.aoc.day1.Task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task8 {

    private static int steps;
    private static String instructions;
    private static final Map<String, Mapping> mappings = new HashMap<>();

    static class Mapping {
        public final String name;
        public final String leftString;
        public final String rightString;

        public Mapping left;
        public Mapping right;

        public Mapping(String name, String leftString, String rightString) {
            this.name = name;
            this.leftString = leftString;
            this.rightString = rightString;
        }


    }


    public static void main(String[] args) {
        InputStream input = Task1.class.getClassLoader().getResourceAsStream("input8.1.txt");
        assert input != null;

        List<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        try {
            String line = reader.readLine();

            while (line != null) {
                lines.add(line);
                if (lines.size() == 1) {
                    instructions = line;
                    line = reader.readLine();
                    continue;
                }
                if (line.isEmpty()) {
                    line = reader.readLine();
                    continue;
                }
                Mapping mapping = processLine(line);
                mappings.put(mapping.name, mapping);

                line = reader.readLine();
            }
            reader.close();

            registerLR();
            run(mappings.get("AAA"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Mapping processLine(String line) {
        String[] split = line.split(" = ");
        String[] mappings = split[1].substring(1, split[1].length() - 1).split(", ");
        return new Mapping(split[0], mappings[0], mappings[1]);
    }

    private static void registerLR() {
        for (Mapping mapping : mappings.values()) {
            mapping.left = mappings.get(mapping.leftString);
            mapping.right = mappings.get(mapping.rightString);
        }
    }

    private static void run(Mapping current) {
        for (char c : instructions.toCharArray()) {
            steps++;
            if (c == 'L') {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        if (current.name.equals("ZZZ")) {
            System.out.println("Part 1: " + steps);
        } else {
            run(current);
        }
    }
}
