package com.crazyhjonk.aoc.day8;

import com.crazyhjonk.aoc.day1.Task1;
import com.crazyhjonk.aoc.util.MathUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Task8 {

    private static long steps;
    private static char[] instructions;
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

        @Override
        public String toString() {
            return "Mapping{" +
                "name='" + name + '\'' +
                ", leftString='" + leftString + '\'' +
                ", rightString='" + rightString + '\'' +
                '}';
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
                    instructions = line.toCharArray();
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
            steps = 0;
            //run2(init2());
            run2attempt2(init2());

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
        for (char c : instructions) {
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

    private static Mapping[] init2() {
        List<Mapping> current = new ArrayList<>();
        for (Mapping mapping : mappings.values()) {
            if (mapping.name.endsWith("A")) {
                current.add(mapping);
            }
        }
        return current.toArray(Mapping[]::new);
    }

    private static void run2(Mapping[] current) {
        while (true) {
            for (char c : instructions) {
                steps++;
                for (int i = 0; i < current.length; i++) {
                    if (c == 'L') {
                        current[i] = current[i].left;
                    } else {
                        current[i] = current[i].right;
                    }
                }
                if (Arrays.stream(current).allMatch(m -> m.name.endsWith("Z"))) {
                    System.out.println("Part 2: " + steps);
                    return;
                }
            }
            System.out.println(steps);
        }
    }

    private static void run2attempt2(Mapping[] mappings) {
        long[] steps = new long[mappings.length];
        for (int i = 0; i < mappings.length; i++) {
            steps[i] = getSteps(mappings[i]);
        }

        System.out.println("Part 2: " + MathUtil.lcm(steps));

    }

    private static int getSteps(Mapping mapping) {
        int steps = 0;
        while (true) {
            for (char c : instructions) {
                steps++;
                if (c == 'L') {
                    mapping = mapping.left;
                } else {
                    mapping = mapping.right;
                }
                if (mapping.name.endsWith("Z")) {
                    return steps;
                }
            }
        }
    }
}
