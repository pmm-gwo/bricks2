package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
        try (FileReader fileReader = new FileReader(path);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            //zwalidowac dane we
            String line = bufferedReader.readLine();

            while (line != null) {
                filterNotUsed(line, unusedBricks);
                putBricksInBox(line, bricksInBox);
                addToInstructions(line, instructions);
            }
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
        instructions.add(line);
    }

    private void assignBricksToInstructions(ArrayList<String> instructions, ArrayList<String> bricksInBox) {
        List<String> bolekPriorityInstructions = instructions.stream()
                .filter(instruction -> isBolekPriority(instruction.charAt(0)))
                .sorted(Comparator.comparingInt(this::extractNumber))
                .collect(Collectors.toList());
        for (String instruction : instructions) {
            assignToInstructions(instruction, bricksInBox);
            if ()
                //porownywanie znakow w liscie sortowanej badz nie
                // poworo
        }
        
        List<String> otherInstructions = instructions.stream()
                .filter(instruction -> !isBolekPriority(instruction.charAt(0)))
                .collect(Collectors.toList());
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
}

//    Liczbę klocków użytych w etapie I
//    Liczbę klocków użytych w etapie II
//    Liczbę klocków, które pozostały w pudełku po zakończeniu budowania
//    Łączną liczbę klocków, których brakowało w pudełku podczas realizacji poszczególnych instrukcji
//    Liczbę budowli, które udało się zbudowac
//    Liczbę budowli, których nie udało się zbudować