package src.Spreadsheet;

import src.Location;

public class SpreadsheetLocation extends Location {
    private int row;
    private int column;

    public SpreadsheetLocation(String cellName){
        try {
            String firstPartUpperCase = cellName.substring(0,1).toLowerCase();
            String secondPart = cellName.substring(1);
            this.column = Spreadsheet.getColumnNumberFromColumnLetter (firstPartUpperCase);
            this.row = Integer.parseInt(secondPart) -1;
        }catch (Exception exception){
            System.out.println("Example input is the following\nA1 = 10\nA1 = \"string\"\nA1 = 10%\nA1 = ( SUM A1-A10 )\nA1 = ( AVG A1-A10 )");
            System.exit(0);
        }

    }

    public SpreadsheetLocation(int row, int column) {
        super();
        this.row = row;
        this.column = column;
    }



    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }
}
