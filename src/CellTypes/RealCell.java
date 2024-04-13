package src.CellTypes;

public class RealCell extends Cell{
    private String value;

    public RealCell(String str) {
        
        this.value =  str;
    }

    public String abbreviatedCellText() {

        return value;
    }
    
	public String fullCellText() { 
        
        return value; 
    }
	public double getDoubleValue() { return Double.parseDouble(value); }
}
