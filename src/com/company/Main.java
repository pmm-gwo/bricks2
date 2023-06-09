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
            System.out.println("klpops");
            return;
        }

        String path = args[0];
        readFile(path, bricksInBox, instructions, unusedBricks);

    }

//    private static void readFile(String path, ArrayList<String> bricksInBox, ArrayList<String> instructions, int unusedBricks) {
//        try (FileReader fileReader = new FileReader(path); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
//            String line = bufferedReader.readLine();
//            String[] parts = line.split(":");
//            int number = Integer.parseInt(parts[0]);
//            line = parts[1];
//            line = line.replaceAll("[\\\\r\\\\n]", "");
//            if (isValid(line)) {
//                while (line != null) {
//                    filterNotUsed(line, unusedBricks);
//                    putBricksInBox(line, bricksInBox);
//                    addToInstructions(line, instructions);
//                    line = bufferedReader.readLine();
//                }
//            }
//        } catch (IOException exception) {
//            System.out.println("klopps");
//        }
//    }
private static void readFile(String path, ArrayList<String> bricksInBox, ArrayList<String> instructions, int unusedBricks) {
    try (FileReader fileReader = new FileReader(path); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
        String line;
        int lineCount = 0;
        while ((line = bufferedReader.readLine()) != null) {
            lineCount++;
            if (lineCount > 15000000) {
                System.out.println("Limit of 15,000,000 lines exceeded.");
                break;
            }
            line = line.trim();
            if (isValid(line)) {
                filterNotUsed(line, unusedBricks);
                putBricksInBox(line, bricksInBox);
                addToInstructions(line, instructions);
            } else {
                System.out.println("Invalid line: " + line);
            }
        }
    } catch (IOException exception) {
        System.out.println("Error reading the file: " + exception.getMessage());
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
        instructions.add(line);
    }

    private static boolean isValid(String line) {
        String regex = "^\\d+:[A-O]{4}$";
        int maxLines = 150_000_000;
        int lineCount = 0;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            return true;
        } else {
            System.out.println("Invalid line: " + line);
            return false;
        }
    }

    private static void removeBricksFromBox(List<String> instructionList, ArrayList<String> bricksInBox) {
        bricksInBox.remove(instructionList);
    }

    private boolean checkBricksInBox(List<String> instructionList, ArrayList<String> bricksInBox) {
        return bricksInBox.contains(instructionList);
    }

    private int extractNumber(String str) {
        String numberString = str.split("[^\\d]")[0];
        return Integer.parseInt(numberString);
    }

    private boolean isBolekPriority(int instructionNumber) {
        return instructionNumber % 3 == 0;
    }

    private void assignToInstructions(String instruction, ArrayList<String> bricksInBox) {
        if (bricksInBox.contains(instruction)) {

        }
    }

    private void assignBricksToInstructions(ArrayList<String> instructions, ArrayList<String> bricksInBox) {
        Map<Integer, List<String>> groupedMap = instructions.stream().collect(Collectors.groupingBy(this::extractNumber));

        for (List<String> instructionList : groupedMap.values()) {
            boolean hasAllBricks = checkBricksInBox(instructionList, bricksInBox);
            if (hasAllBricks) {
                removeBricksFromBox(instructionList, bricksInBox);
            }
        }
        List<String> otherInstructions = instructions.stream().filter(instruction -> !isBolekPriority(instruction.charAt(0))).collect(Collectors.toList());
    }
}


//    Liczbę klocków użytych w etapie I
//    Liczbę klocków użytych w etapie II
//    Liczbę klocków, które pozostały w pudełku po zakończeniu budowania
//    Łączną liczbę klocków, których brakowało w pudełku podczas realizacji poszczególnych instrukcji
//    Liczbę budowli, które udało się zbudowac
//    Liczbę budowli, których nie udało się zbudować
