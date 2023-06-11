package com.company;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        ArrayList<String> bricksInBox = new ArrayList<>();
        ArrayList<String> instructions = new ArrayList<>();
        AtomicInteger unusedBricks = new AtomicInteger(0);

        if (args.length != 1) {
            System.out.println("klops");
            return;
        }
        String path = args[0];
        readFile(path, bricksInBox, instructions, unusedBricks);
        assignBricksToInstructions(instructions, bricksInBox);
        System.out.println("lefted"+countLeftedBricks(unusedBricks, bricksInBox));
//        showResults

    }

    private static void readFile(String path, ArrayList<String> bricksInBox, ArrayList<String> instructions, AtomicInteger unusedBricks) {
        try (FileReader fileReader = new FileReader(path); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line = bufferedReader.readLine();

            while (line != null) {
                line = line.replaceAll("[\\\\rn]", "");
                if (!isValidLine(line)) {
                    System.out.println("klops");
                    line = bufferedReader.readLine();
                    continue;
                }
                System.out.println(line);
                filterNotUsed(line, unusedBricks);
                putBricksInBox(line, bricksInBox);
                addToInstructions(line, instructions);
                line = bufferedReader.readLine();
            }
        } catch (IOException exception) {
            System.out.println("klops");
        }
    }

    private static boolean isValidLine(String line) {
        String regex = "^\\d+:[(A-O)]{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        return matcher.matches();
    }

    private static void putBricksInBox(String line, ArrayList<String> bricksInBox) {
        if (line.startsWith("0") && (bricksInBox.size() < 10000000)) {
            bricksInBox.add(line);
        }
    }

    private static void filterNotUsed(String line, AtomicInteger unusedBricks) {
        if (line.contains("O")) {
            unusedBricks.incrementAndGet();
        }
    }

    private static void addToInstructions(String line, ArrayList<String> instructions) {
        if (!line.contains("0") && (instructions.size() < 1000)) {
            instructions.add(line);
        }
    }

    private static void assignBricksToInstructions(ArrayList<String> instructions, ArrayList<String> bricksInBox) {

        Map<Integer, List<String>> groupedMap = instructions.stream().collect(Collectors.groupingBy(Main::extractNumber));

        Map<Integer, List<String>> filteredMapIsBolekPriorityInstructions = groupedMap.entrySet().stream().filter(entry -> entry.getKey() % 3 == 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<Integer, List<String>> filteredMapOtherInstructions = groupedMap.entrySet().stream().filter(entry -> entry.getKey() % 3 != 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        for (List<String> instructionList : filteredMapIsBolekPriorityInstructions.values()) {
            boolean hasAllBricks = checkBricksInBox(instructionList, bricksInBox);
            if (hasAllBricks) {
                removeBricksFromBox(instructionList, bricksInBox);

            } else {
                //wypisz i zlicz ile klockow braklo do wykonania danej instrukcji
                //countMissingBricks()
            }
        }
        for (List<String> instructionList : filteredMapOtherInstructions.values()) {
            boolean hasAllBricks = checkBricksInBox(instructionList, bricksInBox);
            if (hasAllBricks) {

                removeBricksFromBox(instructionList, bricksInBox);
//        List<String> bolekInstructions = instructions.stream().filter(instruction -> isBolekPriority(instruction.charAt(0))).collect(Collectors.toList());
//        System.out.println("Bolek" + bolekInstructions);
//        Map<Integer, List<String>> groupedMapB = bolekInstructions.stream().collect(Collectors.groupingBy(str -> extractNumber(str)));
//        System.out.println("B" + groupedMapB);
//        List<String> otherInstructions = instructions.stream().filter(instruction -> !isBolekPriority(instruction.charAt(0))).collect(Collectors.toList());
//        System.out.println(otherInstructions);

            } else {
                //wypisz i zlicz ile klockow braklo do wykonania danej instrukcji
                //countMissingBricks()
            }
        }
    }

    private static void removeBricksFromBox(List<String> instructionList, ArrayList<String> bricksInBox) {

        System.out.println("przed" + instructionList);
        System.out.println("przed" + bricksInBox);

        Map<String, Long> instructionCountMap = instructionList.stream()
                .map(pattern -> pattern.split(":")[1])
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        bricksInBox.removeIf(element -> {
            String targetPattern = element.split(":")[1];
            return instructionCountMap
                    .containsKey(targetPattern) && (instructionCountMap
                    .get(targetPattern) > 0) &&
                    (instructionCountMap.compute(targetPattern, (key, count) -> count - 1) >= 0);
        });

        System.out.println("po" + bricksInBox);
        System.out.println("po" + instructionList);
    }

    private static boolean checkBricksInBox(List<String> instructionList, ArrayList<String> bricksInBox) {
        return bricksInBox
                .stream()
                .map(String -> String
                        .split(":")[1]).toList()
                .containsAll(instructionList
                        .stream().map(String -> String
                                .split(":")[1]).toList());
    }
    public static int countLeftedBricks(AtomicInteger unusedBricks, ArrayList<String> bricksInBox ){
        int leftedBricks = bricksInBox.size() + unusedBricks.get();
        System.out.println("unused"+unusedBricks);
        return leftedBricks;
    }

    private static int extractNumber(String str) {
        String numberString = str.split("[^\\d]")[0];
        return Integer.parseInt(numberString);
    }
}

//    Liczbę klocków użytych w etapie I
//    Liczbę klocków użytych w etapie II
//    Liczbę klocków, które pozostały w pudełku po zakończeniu budowania
//    Łączną liczbę klocków, których brakowało w pudełku podczas realizacji poszczególnych instrukcji
//    Liczbę budowli, które udało się zbudowac
//    Liczbę budowli, których nie udało się zbudować