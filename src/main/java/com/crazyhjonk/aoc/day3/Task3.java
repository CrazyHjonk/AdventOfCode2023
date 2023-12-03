package com.crazyhjonk.aoc.day3;


import com.crazyhjonk.aoc.day1.Task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Task3 {

    static char[][] items = new char[140][140];


    public static void main(String[] args) {
        InputStream input = Task1.class.getClassLoader().getResourceAsStream("input3.1.txt");
        assert input != null;

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        try {
            String line = reader.readLine();
            int i = 0;
            while (line != null) {
                items[i] = line.toCharArray();
                i++;
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Part 1: " + processMap(items));
        System.out.println("Part 2: " + processMapPt2(items));
    }

    static int processMap(char[][] items) {
        int sum = 0;
        for (int i = 0; i < items.length; i++) {
            char[] item = items[i];
            for (int j = 0; j < item.length; j++) {
                if (!Character.isDigit(item[j])) continue;
                int originalJ = j;
                char[] number = new char[3];
                int length = 0;
                for (; j < item.length && Character.isDigit(item[j]); j++) {
                    if (!Character.isDigit(item[j])) break;
                    number[j - originalJ] = item[j];
                    length++;
                }
                boolean isAdjacent = false;
                for (int k = j - 1; k >= originalJ; k--) {
                    if (isAdjacentToSymbol(i, k)) {
                        isAdjacent = true;
                        break;
                    }
                }
                if (isAdjacent) {
                    sum += Integer.parseInt(new String(Arrays.copyOf(number, length)));
                }
            }
        }
        return sum;
    }

    static int processMapPt2(char[][] items) {
        int sum = 0;
        for (int i = 0; i < items.length; i++) {
            char[] item = items[i];
            for (int j = 0; j < item.length; j++) {
                if (item[j] != '*') continue;
                int[] adjacentPartNumbers = getAdjacentPartNumbers(i, j);
                if (adjacentPartNumbers.length != 2) continue;
                sum += adjacentPartNumbers[0] * adjacentPartNumbers[1];
            }
        }
        return sum;
    }

    static int[] getAdjacentPartNumbers(int x, int y) {
        int[] numbers = new int[8];
        int k = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            if (i < 0 || i >= items.length) continue;
            for (int j = y - 1; j <= y + 1; j++) {
                Map<Integer, Character> digits = new HashMap<>();
                if (j < 0 || j >= items[i].length) continue;
                if (i == x && j == y) continue;
                if (Character.isDigit(items[i][j])) {
                    digits.put(0, items[i][j]);
                    if (Character.isDigit(items[i][j-1])) {
                        digits.put(-1, items[i][j-1]);
                        if (Character.isDigit(items[i][j-2])) {
                            digits.put(-2, items[i][j-2]);
                        }
                    }
                    if (Character.isDigit(items[i][j+1])) {
                        digits.put(1, items[i][j+1]);
                        if (Character.isDigit(items[i][j+2])) {
                            digits.put(2, items[i][j+2]);
                            j++;
                        }
                        j++;
                    }

                    String number = digits.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(Map.Entry::getValue)
                        .map(Object::toString).reduce("", String::concat);
                    numbers[k] = Integer.parseInt(number);
                }
                k++;
            }
        }
        return Arrays.stream(numbers).filter(i -> i != 0).toArray();
    }

    static boolean isAdjacentToSymbol(int x, int y) {
        for (int i = x - 1; i <= x + 1; i++) {
            if (i < 0 || i >= items.length) continue;
            for (int j = y - 1; j <= y + 1; j++) {
                if (j < 0 || j >= items[i].length) continue;
                if (i == x && j == y) continue;
                if (isSymbol(items[i][j])) return true;
            }
        }
        return false;
    }

    static boolean isSymbol(char c) {
        return c != '.' && !Character.isDigit(c);
    }

}
