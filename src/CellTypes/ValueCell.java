package src.CellTypes;

public class ValueCell extends RealCell{
    public ValueCell(String str) {
        super(str);
    }

    public String abbreviatedCellText() {

        String value = super.fullCellText();
        double doubleValue = Double.parseDouble(value);
        value = doubleValue + ""; // we need this in case of integer inpput 4, store it liek 4.0
        if (value.length() <= 10) {
            // If the value is less than 10 characters, add spaces to make it 10 characters long
            while (value.length() < 10) {
                value += " ";
            }
            return value;
        } else {
            // If the value is more than 10 characters, truncate it to 10 characters
            return value.substring(0, 10);
        }
    }

    public String fullCellText() { 
        return getDoubleValue() + "";
    }

    public double getDoubleValue() { return super.getDoubleValue(); }
}
