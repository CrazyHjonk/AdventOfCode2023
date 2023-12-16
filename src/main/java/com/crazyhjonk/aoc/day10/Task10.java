package com.crazyhjonk.aoc.day10;

import com.crazyhjonk.aoc.day1.Task1;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task10 {

    private static Pipe[][] pipes;

    private static Pair<Integer, Integer> startLocation = null;

    public enum Pipe {
        EMPTY,
        VERT,
        HOR,
        NW,
        NE,
        SW,
        SE,
        START
    }

    public enum Direction {
        NORTH(new int[]{-1, 0}),
        EAST(new int[]{0, 1}),
        SOUTH(new int[]{1, 0}),
        WEST(new int[]{0, -1});

        private final int[] direction;
        Direction(int[] direction) {
            this.direction = direction;
        }

        public int[] getDirection() {
            return direction;
        }

        public int x() {
            return direction[0];
        }

        public int y() {
            return direction[1];
        }
    }


    public static void main(String[] args) {
        InputStream input = Task1.class.getClassLoader().getResourceAsStream("input10.1.txt");
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

        pipes = new Pipe[lines.size()][lines.get(0).length()];

        for (int i = 0; i < lines.size(); i++) {
            pipes[i] = processPipes(lines.get(i), i);
        }

        System.out.println("Part 1: " + navigatePipes());
    }

    public static Pipe[] processPipes(String line, int lineIndex) {
        char[] chars = line.toCharArray();
        Pipe[] pipes = new Pipe[chars.length];
        for (int i = 0; i < chars.length; i++) {
            pipes[i] = switch (chars[i]) {
                case '.' -> Pipe.EMPTY;
                case '|' -> Pipe.VERT;
                case '-' -> Pipe.HOR;
                case 'L' -> Pipe.NE;
                case 'J' -> Pipe.NW;
                case '7' -> Pipe.SW;
                case 'F' -> Pipe.SE;
                case 'S' -> {
                    startLocation = Pair.of(lineIndex, i);
                    yield Pipe.START;
                }
                default -> null;
            };
        }
        return pipes;
    }

    public static int navigatePipes() {
        int[] lengths = new int[4];
        lengths[0] = tryLoop(Direction.EAST);
        lengths[1] = tryLoop(Direction.SOUTH);
        lengths[2] = tryLoop(Direction.WEST);
        lengths[3] = tryLoop(Direction.NORTH);
        System.out.println(Arrays.toString(lengths));
        return (int) Math.ceil(Arrays.stream(lengths).max().orElseThrow() / 2D);
    }

    private static int tryLoop(Direction startDirection) {
        int steps = 0;
        Pair<Integer, Integer> nextLocation = startLocation;
        Direction currentDirection = startDirection;


        while (true) {
            Pipe nextPipe = pipes[nextLocation.getLeft() + currentDirection.x()][nextLocation.getRight() + currentDirection.y()];
            nextLocation = Pair.of(nextLocation.getLeft() + currentDirection.x(), nextLocation.getRight() + currentDirection.y());
            switch (nextPipe) {
                case EMPTY -> {
                    System.out.println("You fell off the pipe at " + nextLocation + "; last direction: " + currentDirection + ", ended on EMPTY");
                    return steps;
                }
                case VERT -> {
                    if (currentDirection.y() != 0) {
                        System.out.println("You fell off the pipe at " + nextLocation + "; last direction: " + currentDirection + ", ended on VERT");
                        return steps;
                    }
                }
                case HOR -> {
                    if (currentDirection.x() != 0) {
                        System.out.println("You fell off the pipe at " + nextLocation + "; last direction: " + currentDirection + ", ended on HOR");
                        return steps;
                    }
                }
                case NW -> {
                    if (currentDirection == Direction.EAST) { //comes from west
                        currentDirection = Direction.NORTH; //goes north
                    } else if (currentDirection == Direction.SOUTH) { //comes from north
                        currentDirection = Direction.WEST; //goes west
                    } else {
                        System.out.println("You fell off the pipe at " + nextLocation + "; last direction: " + currentDirection + ", ended on NW");
                        return steps;
                    }
                }
                case NE -> {
                    if (currentDirection == Direction.WEST) { //comes from east
                        currentDirection = Direction.NORTH;
                    } else if (currentDirection == Direction.SOUTH) { //comes from north
                        currentDirection = Direction.EAST;
                    } else {
                        System.out.println("You fell off the pipe at " + nextLocation + "; last direction: " + currentDirection + ", ended on NE");
                        return steps;
                    }
                }
                case SW -> {
                    if (currentDirection == Direction.EAST) { //comes from west
                        currentDirection = Direction.SOUTH;
                    } else if (currentDirection == Direction.NORTH) { //comes from south
                        currentDirection = Direction.WEST;
                    } else {
                        System.out.println("You fell off the pipe at " + nextLocation + "; last direction: " + currentDirection + ", ended on SW");
                        return steps;
                    }
                }
                case SE -> {
                    if (currentDirection == Direction.WEST) { //comes from east
                        currentDirection = Direction.SOUTH;
                    } else if (currentDirection == Direction.NORTH) { //comes from south
                        currentDirection = Direction.EAST;
                    } else {
                        System.out.println("You fell off the pipe at " + nextLocation + "; last direction: " + currentDirection + ", ended on SE");
                        return steps;
                    }
                }
                case START -> {
                    System.out.println("You found the end of the pipe at " + nextLocation + "; last direction: " + currentDirection + ", ended on START");
                    return steps;
                }
            }
            steps++;
        }
    }
}
