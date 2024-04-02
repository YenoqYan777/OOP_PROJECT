package src;

import src.Spreadsheet.Spreadsheet;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Spreadsheet spreadsheet = new Spreadsheet();
        System.out.println(spreadsheet.getGridText());
        String command = scanner.nextLine();
        while (!command.equalsIgnoreCase("quit")) {
            command = scanner.nextLine();
            if (command.equalsIgnoreCase("quit")) {
                return;
            }
            String output = spreadsheet.processCommand(command.toLowerCase());
            if (output != null) {
                System.out.println(output);
            }
        }

        scanner.close();
    }
}
