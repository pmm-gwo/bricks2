package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Bricks {

    private static int countCompletedBolekInstructions = 0;
    private static int countCompletedOtherInstructions = 0;
    private static int countUncompletedInstructions = 0;
    private static int countBricksUsedForBolekBuildings = 0;
    private static int countBricksUsedForOtherBuildings = 0;
    private static int unusedBricks = 0;

    public static void main(String[] args) {
        ArrayList<String> bricksInBox = new ArrayList<>();
        ArrayList<String> instructions = new ArrayList<>();


        if (args.length != 1) {
            System.out.println("klops");
            return;
        }
        String path = args[0];
        readFile(path, bricksInBox, instructions);
        assignBricksToInstructions(instructions, bricksInBox);
        printResults(instructions, bricksInBox);


    }

    private static void readFile(String path, ArrayList<String> bricksInBox, ArrayList<String> instructions) {
        try (FileReader fileReader = new FileReader(path); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line = bufferedReader.readLine();

            while (line != null) {
                line = line.replaceAll("[\\\\rn]", "");
                if (!isValidLine(line)) {
                    System.out.println("klops");
                    line = bufferedReader.readLine();
                    continue;
                }
                if (!filterNotUsed(line)) {
                    putBricksInBox(line, bricksInBox);

                    addToInstructions(line, instructions);
                }
                line = bufferedReader.readLine();

            }
        } catch (IOException exception) {
            System.out.println("klops");
        }
    }

    private static boolean isValidLine(String line) {
        if (line.startsWith("0")) {
            String regex = "^\\d+:[(A-O)]{4}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(line);
            return matcher.matches();
        }
        String regex = "^\\d+:[(A-N)]{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        return matcher.matches();
    }

    private static void putBricksInBox(String line, ArrayList<String> bricksInBox) {
        if (line.startsWith("0") && (bricksInBox.size() < 10000000)) {
            bricksInBox.add(line);
        }
    }

    private static void addToInstructions(String line, ArrayList<String> instructions) {
        if (!line.contains("0") && (instructions.size() < 1000)) {
            instructions.add(line);
        }
    }

    private static void assignBricksToInstructions(ArrayList<String> instructions, ArrayList<String> bricksInBox) {

        Map<Integer, List<String>> groupedMap = instructions.stream().collect(Collectors.groupingBy(Bricks::extractNumber));

        Map<Integer, List<String>> filteredMapIsBolekPriorityInstructions = groupedMap.entrySet().stream().filter(entry -> entry.getKey() % 3 == 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<Integer, List<String>> filteredMapOtherInstructions = groupedMap.entrySet().stream().filter(entry -> entry.getKey() % 3 != 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        for (List<String> instructionList : filteredMapIsBolekPriorityInstructions.values()) {
            boolean hasAllBricks = checkBricksInBox(instructionList, bricksInBox);
            if (hasAllBricks) {
                removeBricksFromBox(instructionList, bricksInBox);
                countBricksUsedForBolekBuildings += instructionList.size();
                countCompletedBolekInstructions++;

            } else {
                System.out.println(instructionList);
                countUncompletedInstructions++;
                countMissingBricks(instructionList, bricksInBox);
                System.out.println("missing2 " +countMissingBricks(instructionList, bricksInBox));
            }
        }
        for (List<String> instructionList : filteredMapOtherInstructions.values()) {
            boolean hasAllBricks = checkBricksInBox(instructionList, bricksInBox);
            if (hasAllBricks) {

                removeBricksFromBox(instructionList, bricksInBox);
                countBricksUsedForOtherBuildings += instructionList.size();
                countCompletedOtherInstructions++;

            } else {
                countUncompletedInstructions++;
                countMissingBricks(instructionList, bricksInBox);
                System.out.println("missing1 " + countMissingBricks(instructionList, bricksInBox));
            }

        }

    }

    private static void removeBricksFromBox(List<String> instructionList, ArrayList<String> bricksInBox) {

        System.out.println("przed" + instructionList);
        System.out.println("przed" + bricksInBox);

        Map<String, Long> instructionCountMap = instructionList.stream().map(pattern -> pattern.split(":")[1]).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        bricksInBox.removeIf(element -> {
            String targetPattern = element.split(":")[1];
            return instructionCountMap.containsKey(targetPattern) && (instructionCountMap.get(targetPattern) > 0) && (instructionCountMap.compute(targetPattern, (key, count) -> count - 1) >= 0);
        });

        System.out.println("po" + bricksInBox);
    }

    private static boolean checkBricksInBox(List<String> instructionList, ArrayList<String> bricksInBox) {
        return bricksInBox
                .stream()
                .map(String -> String.split(":")[1])
                .toList()
                .containsAll(instructionList
                        .stream()
                        .map(String -> String.split(":")[1])
                        .toList());
    }
    private static int countMissingBricks(List<String> instructionList, ArrayList<String> bricksInBox) {
        List<String> bricksInBoxCodes = bricksInBox.stream()
                .map(brick -> brick.split(":")[1])
                .toList();

        return (int) instructionList.stream()
                .map(instruction -> instruction.split(":")[1])
                .filter(bricksInBoxCodes::contains)
                .count();
    }

    private static int extractNumber(String str) {
        String numberString = str.split("[^\\d]")[0];
        return Integer.parseInt(numberString);
    }

    private static void printResults(ArrayList<String> bricksInBox, List<String> instructionList) {
        int countBricksUsedToCompleteInstructions = countCompletedBolekInstructions + countCompletedOtherInstructions;

        System.out.println("1 -> " + countBricksUsedForBolekBuildings);
        System.out.println("2 -> " + countBricksUsedForOtherBuildings);
        System.out.println("3 -> " + countRemainingBricks(bricksInBox));
        System.out.println("3 -> " + countMissingBricks(instructionList, bricksInBox));
        System.out.println("5 -> " + countBricksUsedToCompleteInstructions);
        System.out.println("6 -> " + countUncompletedInstructions);

    }

    private static boolean filterNotUsed(String line) {
        if ((line.startsWith("0")) && (line.contains("O"))) {
            unusedBricks++;
            return true;
        }
        return false;
    }

    public static int countRemainingBricks(ArrayList<String> bricksInBox) {

        return bricksInBox.size() + unusedBricks;
    }
}

