package src;

import src.Spreadsheet.Spreadsheet;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Spreadsheet spreadsheet = new Spreadsheet();
        System.out.println("Please input the preferred application type: \"Console\" or \"GUI\" ");
        String appType = scanner.nextLine();
        if (appType.trim().equalsIgnoreCase("console")) {
            System.out.println(spreadsheet.getGridText());

            String command = scanner.nextLine();
            while (!command.equalsIgnoreCase("quit")) {
                String output = spreadsheet.processCommand(command.toLowerCase());
                if (output != null) {
                    System.out.println(output);
                }
                command = scanner.nextLine();
            }
        } else if (appType.equalsIgnoreCase("GUI")) {
            // Redirect System.out to a null OutputStream to prevent console output in GUI mode
            System.setOut(new PrintStream(new OutputStream() {
                public void write(int b) {
                    // NO-OP
                }
            }));
            new SpreadsheetGUI();
        }
        else {
            System.out.println("There are 2 possible inputs: \"Console\" or \"GUI\" ");
            System.exit(0);
        }
        scanner.close();
    }
}
