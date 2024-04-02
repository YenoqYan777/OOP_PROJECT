package src.Spreadsheet;

import src.Location;

public class SpreadsheetLocation extends Location {
    private int row;
    private int column;

    public SpreadsheetLocation(String cellName){

        String firstPartUpperCase = cellName.substring(0,1).toLowerCase();
        String secondPart = cellName.substring(1);
        this.column = Spreadsheet.getColumnNumberFromColumnLetter (firstPartUpperCase);
        this.row = Integer.parseInt(secondPart) -1;
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
