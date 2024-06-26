package src.Spreadsheet;

import src.CellTypes.*;
import src.Grid;
import src.Location;

public class Spreadsheet extends Grid {
    private Cell[][] cells;

    public Spreadsheet() {

        cells = new Cell[20][12];
        for (int row = 0; row < 20; row++) {
            for (int column = 0; column < 12; column++) {
                cells[row][column] = new EmptyCell();
            }
        }
    }

    public String processCommand(String command) {
        if (command.equals("clear")) {
            clearAllCells();
            return getGridText();
        }

        String[] trimed = command.split(" ");
        if (trimed.length == 1) {

            SpreadsheetLocation tempLoc = new SpreadsheetLocation(command);
            return printCellResult(trimed[0],tempLoc.getRow(),tempLoc.getColumn());
        }


        if (trimed.length == 2 && trimed[0].equals("clear")) {
            SpreadsheetLocation tempLoc = new SpreadsheetLocation(trimed[1]);
            cells[tempLoc.getRow()][tempLoc.getColumn()] = new EmptyCell();
            return getGridText();
        }

        if (command.contains("\"")) {

            int first = command.indexOf("\"");
            int last = command.lastIndexOf("\"");

            String value = command.substring(first + 1, last);

            int column = getColumnNumberFromColumnLetter(trimed[0].substring(0, 1));
            int row = Integer.parseInt(trimed[0].substring(1)) - 1;

            cells[row][column] = new TextCell(value);

            return getGridText();

        }

        if(command.contains("%")) {

            int length = trimed[2].length();
            String value = trimed[2].substring(0, length - 1);
            int col = getColumnNumberFromColumnLetter(trimed[0].substring(0,1));
            int row = Integer.parseInt(trimed[0].substring(1)) - 1;
            cells[row][col] = new PercentCell(value);

            return getGridText();
        }

        if(trimed.length == 3 ) {

            String value = trimed[2];
            int col = getColumnNumberFromColumnLetter(trimed[0].substring(0,1));
            int row = Integer.parseInt(trimed[0].substring(1)) - 1;
            cells[row][col] = new ValueCell(value);
            return getGridText();
        }

        if(command.contains("(") && command.contains(")")) {
            // Find the opening and closing parentheses indices
            int openingIndex = command.indexOf('(');
            int closingIndex = command.indexOf(')', openingIndex);

            // Extract the expression along with the parentheses
            String expression = command.substring(openingIndex, closingIndex + 1);
            int col = getColumnNumberFromColumnLetter(trimed[0].substring(0,1));
            int row = Integer.parseInt(trimed[0].substring(1)) - 1;
            cells[row][col] = new FormulaCell(expression, cells);

            return getGridText();

        }

        return "Example input is the following\nA1 = 10\nA1 = \"string\"\nA1 = 10%\nA1 = ( SUM A1-A10 )\nA1 = ( AVG A1-A10 )";
    }

    @Override
    public int getRows()
    {
        return 20;
    }

    @Override
    public int getCols()
    {
        return 12;
    }

    @Override
    public Cell getCell(Location loc)
    {
        return cells[loc.getRow()][loc.getColumn()];
    }

    public void setCell(int row, int column, Cell newCell) {
        cells[row][column] = newCell;
    }

    private void clearAllCells() {
        // Clear all cells in the spreadsheet
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 12; col++) {
                cells[row][col] = new EmptyCell();
            }
        }
    }

    public String getGridText() {
        // Initialize the grid header
        String gridText = "   |A         |B         |C         |D         |E         |F         |G         |H         |I         |J         |K         |L         |\n";
        for (int row = 1; row <= 20; row++) {
            // Right-align the row numbers
            if (row < 10) {
                gridText += row + "  ";
            } else {
                gridText += row + " ";
            }
            for (int col = 0; col < 12; col++) {
                // Ensure each cell content is followed by a single | with spaces
                String cellText = cells[row - 1][col].abbreviatedCellText();
                gridText += "|";
                gridText += cellText; // Adjust cell content to be left-aligned within 10 spaces
            }
            gridText += "|\n"; // End each row with a newline
        }
        return gridText;
    }

    public static int getColumnNumberFromColumnLetter(String columnLetter) {
        return Character.toUpperCase(columnLetter.charAt(0)) - 'A';
    }

    private String printCellResult (String cellID, int row, int column){         
        return (cellID.toUpperCase() + " is " + cells[row][column].fullCellText() + "\n" + getGridText());     
    }


}
