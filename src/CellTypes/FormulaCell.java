package src.CellTypes;

import src.Location;
import src.Spreadsheet.*;

public class FormulaCell extends RealCell{
    private Cell[][] Copycells;
    public FormulaCell(String str, Cell[][] cells ) {
        super(str);
        this.Copycells = cells;
    }


    public String abbreviatedCellText() {
        String value = super.fullCellText().toLowerCase();

        // do calculation of formulas that have AVG or SUM
        String strResult;
        if(value.contains("avg") || value.contains("sum")) {
            Double calculatedResult = performCalculationAVGSUM(value, Copycells);
            strResult = calculatedResult + "";
        } else {
            double calculatedResult = calculateExpression(value, Copycells);
            strResult = calculatedResult + "";
        }
        // correct the spaces
        if (strResult.length() <= 10) {
            // If the value is less than 10 characters, add spaces to make it 10 characters long
            while (strResult.length() < 10) {
                strResult += " ";
            }
            return strResult;
        } else {
            // If the value is more than 10 characters, truncate it to 10 characters
            return strResult.substring(0, 10);
        }
    }


    public String fullCellText() { return super.fullCellText(); }
    public double getDoubleValue() { return 0.0; }



    // method for calculation expression with avg and sum, 
    // I have access to live board, using it as a parameter, so every time I can update the formula values when they change
	private Double performCalculationAVGSUM(String exp, Cell[][] cells) {
		String expL = exp.toLowerCase();
		String[] trimmed = exp.split(" ");
		String[] trimmed2 = trimmed[2].split("-"); 	// f3 = ( avg B10-B7 )

		Location loc = new SpreadsheetLocation(trimmed2[0]);
		int row1 = loc.getRow();
		int col1 = loc.getColumn();
	
		Location loc2 = new SpreadsheetLocation(trimmed2[1]);
		int row2 = loc2.getRow();
		int col2 = loc2.getColumn();
	
		double sum = 0;
		int count = 0;
	
		// Loop over rows and columns
		for (int i = row1; i <= row2; i++) {
			for (int k = col1; k <= col2; k++) {
				// Try to parse the value of the cell
					Double value = Double.parseDouble(cells[i][k].abbreviatedCellText());
					sum += value;
					count++;
			}
		}
		// Calculate and return result based on the expression type
		if (expL.contains("avg")) {
			if (count == 0) {
				return null;
			}
			return sum / count;
		} else if (expL.contains("sum")) {
			return sum;
		}
	
        return null;

	}
    
    // again here I have access to live board cells to keep updated the refernce cells
    private Double calculateExpression(String exp, Cell[][] cells) {
        double value;
        String[] trimmed = exp.split(" "); //  ex ( B3 * 6 + 3 )  ---> ["(", "B3", "*", "6", "+", "3", ")"]
        for ( int i = 1; i < trimmed.length - 1; i++ ) {

            // try to find reference Cells and replace it with the value from board
            if(trimmed[i].contains("A") || trimmed[i].contains("B") || trimmed[i].contains("C")
                || trimmed[i].contains("D") || trimmed[i].contains("E") || trimmed[i].contains("F")
                || trimmed[i].contains("G") || trimmed[i].contains("H") || trimmed[i].contains("I")
                || trimmed[i].contains("J") || trimmed[i].contains("K") || trimmed[i].contains("L")) 
              {
                int col = Spreadsheet.getColumnNumberFromColumnLetter(trimmed[i].substring(0,1));
			    int row = Integer.parseInt(trimmed[i].substring(1)) - 1;
                value = cells[row][col].getDoubleValue();
                trimmed[i] = value + "";
              }
        }

        String expression = "";
        for(int i = 1; i < trimmed.length - 1; i++) {
            expression += trimmed[i];
        }

        return calculateExpressionfromString(expression);



    }


    public static double calculateExpressionfromString(String expression) {
        // Remove spaces from the expression
        expression = expression.replaceAll(" ", "");

        // Initialize variables
        double operand1 = 0;
        double operand2 = 0;
        char operator = ' ';
        String numBuffer = "";

        // Iterate through the expression
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            // If character is a digit, add it to the number buffer
            if (Character.isDigit(c) || c == '.') {
                numBuffer += c;
            } else { // Character is an operator
                // If numBuffer is not empty, parse it as operand
                if (!numBuffer.isEmpty()) {
                    if (operand1 == 0) {
                        operand1 = Double.parseDouble(numBuffer);
                    } else {
                        operand2 = Double.parseDouble(numBuffer);
                    }
                    numBuffer = ""; // Clear the buffer
                }

                // Store the operator
                operator = c;
            }
        }

        // Parse the last operand
        if (!numBuffer.isEmpty()) {
            operand2 = Double.parseDouble(numBuffer);
        }

        // Perform calculation based on the operator
        double result = 0;
        if (operator == '+') {
            result = operand1 + operand2;
        } else if (operator == '-') {
            result = operand1 - operand2;
        } else if (operator == '*') {
            result = operand1 * operand2;
        } else if (operator == '/') {
            if (operand2 != 0) { // Avoid division by zero
                result = operand1 / operand2;
            } else {
                //System.out.println("Error: Division by zero.");
                return Double.NaN;
            }
        } else {
            // No operator found, return single operand as result
            result = operand1;
        }

        return result;
    }
}
