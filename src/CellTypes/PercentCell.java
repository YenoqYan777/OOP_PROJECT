package src.CellTypes;

public class PercentCell extends RealCell {
    private int prec;
    public PercentCell(String str) {
        // convert string into double, divide by 100, then again convert into string
        super( (Double.parseDouble(str) / 100 ) + "");
        //super((Double.parseDouble(str) / 100 + "").substring(0, str.length() + 2) );
        prec = str.length();
    }
    public String fullCellText() {
        // double result = super.getDoubleValue();
        // return result + "";

        String result = super.fullCellText();
        if(result.length() >= prec + 2 ) {
            return result.substring(0, prec+2);
        }
        return result;
    }

    public String abbreviatedCellText() {
        // get the decimal value, multiply by 100, then truvate by storing the value in int; 0.08447 --> 8
        double result = super.getDoubleValue() * 100;
        int intResult = (int)  result;
        String strresult = intResult + "%";
        while (strresult.length() < 10) {
            strresult += " ";
        } // return "\"\""
        return strresult;
    }

    // public String fullCellText() { 
    //     //double result = super.getDoubleValue();
    //     return super.fullCellText();
    // }

    public double getDoubleValue() { return super.getDoubleValue(); }
}