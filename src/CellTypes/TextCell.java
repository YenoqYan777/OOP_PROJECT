package src.CellTypes;

public class TextCell extends Cell {
    private String value;

    public TextCell(String value) {
        this.value = value;
    }

    public String abbreviatedCellText() {
        String copyString = value;

        if (copyString.length() <= 10) {

            while (copyString.length() < 10) {
                copyString += " ";
            }
            return copyString;
        } else {
            return copyString.substring(0, 10);
        }
    }

    public String fullCellText() {
        value = "\"" + value + "\"";
        return value;
    }

    public double getDoubleValue() {
        return -1.0;
    }
}


