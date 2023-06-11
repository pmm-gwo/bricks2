import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private static int countBricksUsedForOtherBuildings = 0;
    private static int countBricksUsedForBolekBuildings = 0;
    private static int countUncompletedInstructions = 0;
    private static int unusedBricks = 0;

    public static void main(String[] args) {
        ArrayList<String> bricksInBox = new ArrayList<>();
        ArrayList<String> instructions = new ArrayList<>();


        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                processLine(line, bricksInBox, instructions);
            }
        } catch (IOException exception) {
            System.out.println("klops");
            return;
        }

        assignBricksToInstructions(instructions, bricksInBox);
        printResults(bricksInBox, instructions);
    }

    private static void processLine(String line, ArrayList<String> bricksInBox, ArrayList<String> instructions) {
        line = line.replaceAll("[\\\\rn]", "");
        if (!isValidLine(line)) {
            System.out.println("klops");
            return;
        }
        if (!filterNotUsed(line)) {
            putBricksInBox(line, bricksInBox);
            addToInstructions(line, instructions);
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
                countUncompletedInstructions++;
                countMissingBricks(instructionList, bricksInBox);
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
            }

        }

    }


    private static int countMissingBricks(List<String> instructionList, ArrayList<String> bricksInBox) {
        List<String> bricksInBoxCodes = bricksInBox.stream()
                .map(brick -> brick.split(":")[1])
                .toList();

        List<String> missingBricks = instructionList.stream()
                .map(instruction -> instruction.split(":")[1])
                .filter(brickCode -> !bricksInBoxCodes.contains(brickCode))
                .toList();

        return missingBricks.size();
    }


    private static void removeBricksFromBox(List<String> instructionList, ArrayList<String> bricksInBox) {


        Map<String, Long> instructionCountMap = instructionList.stream().map(pattern -> pattern.split(":")[1]).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        bricksInBox.removeIf(element -> {
            String targetPattern = element.split(":")[1];
            return instructionCountMap.containsKey(targetPattern) && (instructionCountMap.get(targetPattern) > 0) && (instructionCountMap.compute(targetPattern, (key, count) -> count - 1) >= 0);
        });


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


    private static int extractNumber(String str) {
        String numberString = str.split("[^\\d]")[0];
        return Integer.parseInt(numberString);
    }


    private static void printResults(ArrayList<String> bricksInBox, List<String> instructionList) {
        int countBricksUsedToCompleteInstructions = countCompletedBolekInstructions + countCompletedOtherInstructions;

        System.out.println(countBricksUsedForBolekBuildings);
        System.out.println(countBricksUsedForOtherBuildings);
        System.out.println(countRemainingBricks(bricksInBox));
        System.out.println(countMissingBricks(instructionList, bricksInBox));
        System.out.println(countBricksUsedToCompleteInstructions);
        System.out.println(countUncompletedInstructions);

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
