package com.crazyhjonk.aoc.day5;

import com.crazyhjonk.aoc.day1.Task1;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Task5Attempt2 {

    enum Maps {
        SEED_TO_SOIL(0),
        SOIL_TO_FERTILIZER(1),
        FERTILIZER_TO_WATER(2),
        WATER_TO_LIGHT(3),
        LIGHT_TO_TEMPERATURE(4),
        TEMPERATURE_TO_HUMIDITY(5),
        HUMIDITY_TO_LOCATION(6);

        Maps(int index) {
            INDEX = index;
        }

        public final int INDEX;
    }

    private static final Map<Maps, List<Triple<Long, Long, Long>>> conversions = new HashMap<>();

    private static List<List<Triple<Long, Long, Long>>> orderedConversions;

    private static List<Long> seeds;

    private static long maxIndex;

    public static void main(String[] args) {
        InputStream input = Task1.class.getClassLoader().getResourceAsStream("input5.1.txt");
        assert input != null;

        Pair<Maps, List<Triple<Long, Long, Long>>> currentConversionList = Pair.of(null, null);

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        try {
            String line = reader.readLine();

            while (line != null) {
                System.out.println(line);
                if (line.contains("seeds: ")) {
                    seeds = Arrays.stream(line.split(": ")[1].split(" "))
                        .mapToLong(Long::parseLong).boxed().toList();
                    line = reader.readLine();
                    continue;
                }
                if (line.contains(":")) {
                    if (currentConversionList.getKey() != null)
                        conversions.put(currentConversionList.getKey(), currentConversionList.getValue());
                    currentConversionList = Pair.of(switch (line) {
                        case "seed-to-soil map:" -> Maps.SEED_TO_SOIL;
                        case "soil-to-fertilizer map:" -> Maps.SOIL_TO_FERTILIZER;
                        case "fertilizer-to-water map:" -> Maps.FERTILIZER_TO_WATER;
                        case "water-to-light map:" -> Maps.WATER_TO_LIGHT;
                        case "light-to-temperature map:" -> Maps.LIGHT_TO_TEMPERATURE;
                        case "temperature-to-humidity map:" -> Maps.TEMPERATURE_TO_HUMIDITY;
                        case "humidity-to-location map:" -> Maps.HUMIDITY_TO_LOCATION;
                        default -> null;
                    }, new ArrayList<>());
                    line = reader.readLine();
                    continue;
                }
                if (line.isEmpty()) {
                    line = reader.readLine();
                    continue;
                }
                currentConversionList.getValue().add(processLine(line));
                line = reader.readLine();
            }
            conversions.put(currentConversionList.getKey(), currentConversionList.getValue());
            reader.close();
            orderedConversions = conversions.entrySet().stream().sorted(Comparator.comparingInt(map -> map.getKey().INDEX))
                .map(Map.Entry::getValue).toList();

            System.out.println("Result Part 1: " + getLowestIndex());
            System.out.println("Result Part 2: " + getLowestIndexPart2());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Triple<Long, Long, Long> processLine(String line) {
        Long[] ints = Arrays.stream(line.split(" ")).mapToLong(Long::parseLong).boxed()
            .toArray(Long[]::new);
        return Triple.of(ints[0], ints[1], ints[2]);
    }

    public static long getLowestIndex() {
        long min = 0;
        for (Long seed : seeds) {
            long result = tracePath(seed);
            min = min == 0 || result < min ? result : min;
        }
        return min;
    }

    public static long getLowestIndexPart2() {
        long min = 0;
        int count = 0;
        List<Pair<Long, Long>> ranges = new ArrayList<>();
        for (int i = 0; i < seeds.size(); i += 2) {
            ranges.add(Pair.of(seeds.get(i), seeds.get(i) + seeds.get(i + 1)));
        }

        for (Pair<Long, Long> range : ranges) {
            for (long seed = range.getLeft(); seed < range.getRight(); seed++) {
                long result = tracePath(seed);
                min = min == 0 || result < min ? result : min;
            }
            count++;
            System.out.println("Processed Range " + count);
        }
        return min;
    }

    public static long tracePath(long input) {
        long currentNumber = input;
        for (List<Triple<Long, Long, Long>> conversion : orderedConversions) {
            long distance = getDistance(conversion, currentNumber);
            currentNumber += distance;
        }
        return currentNumber;
    }

    private static long getDistance(List<Triple<Long, Long, Long>> triples, long currentNumber) {
        long distance = 0;
        for (Triple<Long, Long, Long> triple : triples) {
            long destination = triple.getLeft();
            long source = triple.getMiddle();
            long range = triple.getRight();

            if (currentNumber >= source && currentNumber <= source + range) {
                distance = destination - source;
            }
        }
        return distance;
        //last result: 57451710
    }
}
