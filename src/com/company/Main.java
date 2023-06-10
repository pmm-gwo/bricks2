package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {


    public static void main(String[] args) {
        ArrayList<String> bricksInBox = new ArrayList<>();
        ArrayList<String> instructions = new ArrayList<>();
        int unusedBricks = 0;
        if (args.length != 1) {
            System.out.println("klops");
            return;
        }
        String path = args[0];
        readFile(path, bricksInBox, instructions, unusedBricks);

    }

    private static void readFile(String path, ArrayList<String> bricksInBox, ArrayList<String> instructions, int unusedBricks) {
        try (FileReader fileReader = new FileReader(path); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            //fileReader.isValid;
            String line = bufferedReader.readLine();
            //Validation

            while (line != null){
                line=line.replaceAll("[\\\\r\\\\n]", "");
                System.out.println(line);
                filterNotUsed(line, unusedBricks);
                putBricksInBox(line, bricksInBox);
                addToInstructions(line, instructions);
                line = bufferedReader.readLine();
            }
            assignBricksToInstructions(instructions, bricksInBox);
        } catch (IOException exception) {

            System.out.println("klops");
        }
    }



    private static void putBricksInBox(String line, ArrayList<String> bricksInBox) {
        if (line.startsWith("0")) {
            bricksInBox.add(line);
        }
    }

    private static void filterNotUsed(String line, int unusedBricks) {
        if (line.contains("O")) {
            unusedBricks++;
        }
    }

    private static void addToInstructions(String line, ArrayList<String> instructions) {
        if (!line.contains("0")) {
            instructions.add(line);
        }
    }

    private static void assignBricksToInstructions(ArrayList<String> instructions, ArrayList<String> bricksInBox) {

//        Map<Integer, List<String>> groupedMap = instructions.stream().collect(Collectors.groupingBy(this::extractNumber));

        Map<Integer, List<String>> groupedMap = instructions.stream().collect(Collectors.groupingBy(str -> extractNumber(str)));

        Map<Integer, List<String>> filteredMapIsBolekPriorityInstructions = groupedMap.entrySet().stream().filter(entry -> entry.getKey() % 3 == 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println(filteredMapIsBolekPriorityInstructions);


        Map<Integer, List<String>> filteredMapOtherInstructions = groupedMap.entrySet().stream().filter(entry -> entry.getKey() % 3 != 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println(filteredMapOtherInstructions);
//        System.out.println(groupedMap);

        for (List<String> instructionList : filteredMapIsBolekPriorityInstructions.values()) {
            boolean hasAllBricks = checkBricksInBox(instructionList, bricksInBox);
            if (hasAllBricks) {
                removeBricksFromBox(instructionList, bricksInBox);
                System.out.println(bricksInBox);
            } else {
                //wypisz i zlicz ile klockow braklo do wykonania danej instrukcji
                //countMissingBricks()
            }    System.out.println(bricksInBox);
        }
//        List<String> bolekInstructions = instructions.stream().filter(instruction -> isBolekPriority(instruction.charAt(0))).collect(Collectors.toList());
//        System.out.println("Bolek" + bolekInstructions);
//        Map<Integer, List<String>> groupedMapB = bolekInstructions.stream().collect(Collectors.groupingBy(str -> extractNumber(str)));
//        System.out.println("B" + groupedMapB);
//        List<String> otherInstructions = instructions.stream().filter(instruction -> !isBolekPriority(instruction.charAt(0))).collect(Collectors.toList());
//        System.out.println(otherInstructions);

    }


    private static void removeBricksFromBox(List<String> instructionList, ArrayList<String> bricksInBox) {
//        bricksInBox.removeIf(element -> {
        System.out.println("przed"+instructionList);
        System.out.println("przed"+bricksInBox);
            for (String pattern : instructionList) {
//                if (element.matches(".*:" + pattern.split(":")[1])) {
//                    return true;
//                }
//            }
//            return false;
  bricksInBox.removeIf(element -> element.matches(".*:" + pattern.split(":")[1]));




            }
        System.out.println("po"+bricksInBox);
        System.out.println("po"+instructionList);
        }

//    }


    private static boolean checkBricksInBox(List<String> instructionList, ArrayList<String> bricksInBox) {
        return bricksInBox
                .stream()
                .map(String->String
                        .split(":")[1])
                .collect(Collectors.toList())
                .containsAll(instructionList
                        .stream().map(String->String
                                .split(":")[1]).collect(Collectors.toList()));
    }

    private static boolean isBolekPriority(int instructionNumber) {
        return instructionNumber % 3 == 0;
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