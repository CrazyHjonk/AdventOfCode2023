package com.crazyhjonk.aoc.day5;

import com.crazyhjonk.aoc.day1.Task1;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Task5 {

    public record MapQuadruple(LongMap leftLowerBounds, LongMap rightLowerBounds, LongMap runningLengths, LongMap distances) {
    }

    static class LongMap extends HashMap<Long, Long> {
        public LongMap() {
            super();
        }
    }

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

    private static final Map<Maps, Pair<MapQuadruple, List<Triple<Long, Long, Long>>>> conversions = new HashMap<>();

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
                        conversions.put(currentConversionList.getKey(), Pair.of(null, currentConversionList.getValue()));
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
            conversions.put(currentConversionList.getKey(), Pair.of(null, currentConversionList.getValue()));
            reader.close();

            for (var conversion : conversions.entrySet()) {
                var value = conversion.getValue().getValue();
                System.out.println("Processing map: " + conversion.getKey().toString());
                MapQuadruple key = populateMap(value);
                conversions.put(conversion.getKey(), Pair.of(key, value));
            }

            System.out.println("Result Part 1: " + getLowestIndex());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Triple<Long, Long, Long> processLine(String line) {
        Long[] ints = Arrays.stream(line.split(" ")).mapToLong(Long::parseLong).boxed()
            .toArray(Long[]::new);
        return Triple.of(ints[0], ints[1], ints[2]);
    }

    public static MapQuadruple populateMap(List<Triple<Long, Long, Long>> input) {
        LongMap leftBounds = new LongMap();
        LongMap rightBounds = new LongMap();
        LongMap runningLengths = new LongMap();
        LongMap distances = new LongMap();
        for (Triple<Long, Long, Long> triple : input) {
            maxIndex = maxIndex < triple.getLeft() ? triple.getLeft() : maxIndex;
            leftBounds.put(triple.getMiddle(), triple.getLeft());
            rightBounds.put(triple.getLeft(), triple.getMiddle());
            runningLengths.put(triple.getMiddle(), triple.getRight());
            distances.put(triple.getMiddle(), triple.getLeft() - triple.getMiddle());
            //leftBounds.put(triple.getLeft(), triple.getMiddle());
            //rightBounds.put(triple.getMiddle(), triple.getLeft());
            //runningLengths.put(triple.getLeft(), triple.getRight());
            //distances.put(triple.getLeft(), triple.getMiddle() - triple.getLeft());
        }
        System.out.println(input);
        System.out.println(leftBounds);
        System.out.println(distances);
        return new MapQuadruple(leftBounds, rightBounds, runningLengths, distances);
    }

    public static long getLowestIndex() {
        long min = 0;
        long count = 0;
        for (Long seed : seeds) {
            System.out.println("Processing seed: " + (count + 1) + "/" + seeds.size());
            long result = tracePath(seed);
            min = min == 0 || result < min ? result : min;
            count++;
        }
        return min;
    }

    public static long tracePath(long input) {
        long currentNumber = input;
        int count = 0;
        for (var conversion : conversions.entrySet().stream()
            .sorted(Comparator.comparingInt(map -> map.getKey().INDEX)).toList()) {

            var maps = conversion.getValue().getLeft();
            long distance = getDistance(maps, currentNumber);

            System.out.println("Distance for Map " + (count + 1) + ": " + distance + " (= " + (currentNumber + distance) + ")");
            System.out.println("Name of Map: " + conversion.getKey().toString());

            currentNumber += distance;
            count++;
        }
        return currentNumber;
    }

    private static long getDistance(MapQuadruple maps, long currentNumber) {
        Map<Long, Long> leftBounds = maps.leftLowerBounds();
        Map<Long, Long> rightBounds = maps.rightLowerBounds();
        Map<Long, Long> runningLengths = maps.runningLengths();
        Map<Long, Long> distances = maps.distances();

        long leftIndex = -1;
        long rightIndex = -1;
        long distance = 0;

        for (long i = 0; currentNumber - i >= 0; i++) {
            if (leftBounds.containsKey(currentNumber - i)) {
                leftIndex = currentNumber - i;
                break;
            }
        }

        for (long i = 0; currentNumber - i >= 0; i++) {
            if (rightBounds.containsKey(currentNumber - i)) {
                rightIndex = currentNumber - i;
                break;
            }
        }

        long runningLength = runningLengths.containsKey(leftIndex) ? runningLengths.get(leftIndex) : 0;

        System.out.println("currentNumber: " + currentNumber);
        System.out.println("leftIndex: " + leftIndex);
        System.out.println("runningLengths.get(leftIndex): " + runningLength);
        System.out.println("= " + (leftIndex + runningLength));

        if (leftIndex != -1 && distances.containsKey(leftIndex) && leftIndex + runningLengths.get(leftIndex) > currentNumber) {
            distance = distances.get(leftIndex);
        } else if (rightIndex != -1 && distances.containsKey(rightBounds.get(rightIndex))
            && rightIndex - runningLengths.get(rightBounds.get(rightIndex)) < currentNumber) {
            distance = distances.get(rightBounds.get(rightIndex));
        }
        return distance;
    }
}
