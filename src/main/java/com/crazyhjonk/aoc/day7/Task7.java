package com.crazyhjonk.aoc.day7;

import com.crazyhjonk.aoc.day1.Task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task7 {

    private final static List<Hand> hands = new ArrayList<>();

    static class Hand {
        private final List<Integer> cards;
        private final int bet;
        private final int rank;

        public Hand(char[] cards, int bet) {
            this.cards = getCardsAsInts(cards);
            this.bet = bet;
            this.rank = computeRank(this.cards);
        }

        private List<Integer> getCardsAsInts(char[] cardChars) {
            List<Integer> cardsAsInts = new ArrayList<>();
            for (Character card : cardChars) {
                switch (card) {
                    case 'T':
                        cardsAsInts.add(10);
                        break;
                    case 'J':
                        cardsAsInts.add(1);
                        break;
                    case 'Q':
                        cardsAsInts.add(12);
                        break;
                    case 'K':
                        cardsAsInts.add(13);
                        break;
                    case 'A':
                        cardsAsInts.add(14);
                        break;
                    default:
                        cardsAsInts.add(Integer.parseInt(String.valueOf(card)));
                        break;
                }
            }
            return cardsAsInts;
        }

        private int computeRank(List<Integer> cards) {
            int jokers = (int) cards.stream().filter(c -> c == 1).count();
            Map<Integer, Integer> foundTuples = new HashMap<>();
            for (int i = 0; i < cards.size(); i++) {
                int card = cards.get(i);
                if (card == 1) { // don't consider jokers here
                    continue;
                }
                int tupleSize = (int) cards.stream().filter(c -> c == card).count();
                if (tupleSize >= 2) {
                    foundTuples.put(card, tupleSize);
                }
            }

            boolean hasPair = foundTuples.containsValue(2);
            boolean hasTwoPair = foundTuples.values().stream().filter(v -> v == 2).toList().size() > 1;
            boolean hasTriple = foundTuples.containsValue(3);
            boolean hasFullHouse = hasPair && hasTriple;
            boolean hasQuadruple = foundTuples.containsValue(4);
            boolean hasQuintuple = foundTuples.containsValue(5);
            if (hasQuintuple) return 7;

            return switch (jokers) {
                case 0 -> {
                    if (hasQuadruple) {
                        yield 6;
                    } else if (hasFullHouse) {
                        yield 5;
                    } else if (hasTriple) {
                        yield 4;
                    } else if (hasTwoPair) {
                        yield 3;
                    } else if (hasPair) {
                        yield 2;
                    } else {
                        yield 1;
                    }
                }
                case 1 -> {
                    if (hasQuadruple) {
                        yield 7;
                    } else if (hasTriple) {
                        yield 6;
                    } else if (hasTwoPair) {
                        yield 5;
                    } else if (hasPair) {
                        yield 4;
                    } else {
                        yield 2;
                    }
                }
                case 2 -> {
                    if (hasQuadruple || hasTriple) {
                        yield 7;
                    } else if (hasPair) {
                        yield 6;
                    } else {
                        yield 4;
                    }
                }
                case 3 -> {
                    if (hasQuadruple || hasTriple || hasPair) {
                        yield 7;
                    } else {
                        yield 6;
                    }
                }
                case 4, 5 -> 7;
                default -> throw new RuntimeException("Invalid amount of jokers: " + jokers);
            };
        }

        public boolean isHigher(Hand other) {
            if (this.rank > other.rank) {
                return true;
            } else if (this.rank < other.rank) {
                return false;
            } else {
                for (int i = 0; i < this.cards.size(); i++) {
                    if (this.cards.get(i) > other.cards.get(i)) {
                        return true;
                    } else if (this.cards.get(i) < other.cards.get(i)) {
                        return false;
                    }
                }
                return false;
            }
        }
    }

    public static void main(String[] args) {
        InputStream input = Task1.class.getClassLoader().getResourceAsStream("input7.1.txt");
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

            lines.forEach(Task7::processLine);
            hands.sort((o1, o2) -> {
                if (o1.isHigher(o2)) {
                    return 1;
                } else if (o2.isHigher(o1)) {
                    return -1;
                } else {
                    return 0;
                }
            });
            System.out.println(hands.stream().map(h -> "\n" + h.cards).toList());
            int sum = 0;
            for (int i = 0; i < hands.size(); i++) {
                sum += hands.get(i).bet * (i + 1);
            }
            System.out.println("Part 1: " + sum);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void processLine(String line) {
        String[] split = line.split(" ");

        hands.add(new Hand(split[0].toCharArray(), Integer.parseInt(split[1])));
    }
}
