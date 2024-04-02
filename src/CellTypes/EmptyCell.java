package src.CellTypes;

public class EmptyCell extends Cell {
    @Override
    public String abbreviatedCellText() {
       return  "          ";
    }

    @Override
    public String fullCellText() {
        return "";
    }

    @Override
    public double getDoubleValue() {
        return -1.0;
    }
}
