package src;

import src.Spreadsheet.Spreadsheet;
import src.Spreadsheet.SpreadsheetLocation;
import src.CellTypes.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import java.awt.*;;

public class SpreadsheetGUI extends JFrame {
    private Spreadsheet spreadsheet;
    private JTable table;
    private JTextField commandField;
    private JButton executeButton;
    private JTextArea outputArea;
    private boolean isUpdating = false;

    public SpreadsheetGUI() {
        super("Spreadsheet Application");
        spreadsheet = new Spreadsheet();
        initializeComponents();
        initializeTable();
        setupTableListeners(); // Set up listeners for table editing
        setupLayout();
        addActionListeners();
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    

    private void initializeComponents() {
        table = new JTable(20, 12);
        table.setGridColor(Color.BLACK);
        table.setShowGrid(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(100);
        }

        commandField = new JTextField(30);
        executeButton = new JButton("Execute");
        outputArea = new JTextArea(5, 10);
        outputArea.setEditable(false);
    }

    private void setupTableListeners() {
        table.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (table.isEditing()) {
                    table.getCellEditor().stopCellEditing();  // Force stop editing
                }
                String cellLocation = Character.toString((char)('A' + column)) + (row + 1);
                Object cellValue = table.getValueAt(row, column);
                if (cellValue == null) cellValue = "";  // Handle null values
                String command = cellLocation + " = " + cellValue.toString().trim();
                executeCommand(command);  // Process the command
            }
        });
    }
    
    
    private void processCellUpdate(int row, int column) {
    if (isUpdating) return; // Prevent recursive updates
    isUpdating = true;
    try {
        Object cellValue = table.getValueAt(row, column);
        String value = (cellValue != null) ? cellValue.toString() : "";
        Cell newCell;
        try {
            if (value.matches("-?\\d+(\\.\\d+)?")) {  // Numeric values
                newCell = new ValueCell(value);
            } else {  // Non-numeric values
                newCell = new TextCell(value);
            }
            spreadsheet.setCell(row, column, newCell);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid value.");
        }
        refreshSpecificCell(row, column); // Refresh the GUI to reflect the new cell value
    } finally {
        isUpdating = false;
    }
}
    
    private void refreshSpecificCell(int row, int column) {
        Cell cell = spreadsheet.getCell(new SpreadsheetLocation(row, column));
        String updatedText = (cell != null) ? cell.abbreviatedCellText() : "          ";
        table.setValueAt(updatedText, row, column);
        table.repaint();
    }
    
    private void refreshTable() {
        for (int row = 0; row < table.getRowCount(); row++) {
            for (int col = 0; col < table.getColumnCount(); col++) {
                refreshSpecificCell(row, col);  // Refresh each cell individually
            }
        }
    }
    
    
    private void setupLayout() {
        initializeTable();  // Initialize the main table
        JScrollPane scrollPane = new JScrollPane(table);
    
        addRowHeader(scrollPane);  // Add row headers to the scroll pane
    
         JScrollPane outputScrollPane = new JScrollPane(outputArea);
    
        JPanel commandPanel = new JPanel(new BorderLayout());
        commandPanel.add(commandField, BorderLayout.CENTER);
        commandPanel.add(executeButton, BorderLayout.EAST);
    
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(commandPanel, BorderLayout.SOUTH);
        mainPanel.add(outputScrollPane, BorderLayout.EAST);
    
        this.getContentPane().add(mainPanel);
    }
    
    private void addRowHeader(JScrollPane scrollPane) {
        JTable headerTable = new JTable(20, 1);
        headerTable.setPreferredScrollableViewportSize(new Dimension(50, 0));
        headerTable.setRowHeight(table.getRowHeight());
        headerTable.setModel(new AbstractTableModel() {
            public int getRowCount() { return table.getRowCount(); }
            public int getColumnCount() { return 1; }
            public Object getValueAt(int row, int column) { return String.valueOf(row + 1); }
        });
    
        headerTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(235, 235, 235));  // a light gray to distinguish
                setHorizontalAlignment(SwingConstants.CENTER);
                return this;
            }
        });
    
        headerTable.setEnabled(false);  // Make the header non-editable
        scrollPane.setRowHeaderView(headerTable);
        scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, headerTable.getTableHeader());
    }
    

    private void addActionListeners() {
        // Listener for the execute button
        executeButton.addActionListener(e -> {
            // Retrieve the command from the commandField and trim any leading or trailing whitespace
            String command = commandField.getText().trim();
            // Check if the command is not empty
            if (!command.isEmpty()) {
                executeCommand(command); // Execute the command
                commandField.setText(""); // Optionally clear the command field after execution
            }
        });
    
        // Listener for the command field to handle enter key press
        commandField.addActionListener(e -> {
            // Retrieve the command from the commandField and trim any leading or trailing whitespace
            String command = commandField.getText().trim();
            // Check if the command is not empty
            if (!command.isEmpty()) {
                executeCommand(command); // Execute the command
                commandField.setText(""); // Optionally clear the command field after execution
            }
        });
    }
    
    private void processCellChange(int row, int column) {
        if (isUpdating) return;
        isUpdating = true;
        try {
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing(); // Make sure editing is stopped
            }
            String cellLocation = Character.toString((char)('A' + column)) + (row + 1);
            Object cellValue = table.getValueAt(row, column);
            String command = cellLocation + " = " + (cellValue != null ? cellValue.toString() : "").trim();
            executeCommand(command);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            isUpdating = false;
        }
    }
    
    private void executeCommand(String command) {
        try {
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();  // Ensure any ongoing edits are committed
            }
            String result = spreadsheet.processCommand(command);
            // Check if the result starts with the grid layout indication, adjust based on your application's specific output
            if (!result.startsWith("   |")) {
                outputArea.setText(result); // Display the result in the output area only if it's not the grid text
            }
            refreshTable(); // Refresh the table to reflect any changes
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Execution Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    

    private void updateTable() {
        for (int row = 0; row < table.getRowCount(); row++) {
            for (int col = 0; col < table.getColumnCount(); col++) {
                SpreadsheetLocation loc = new SpreadsheetLocation(Character.toString((char)('A' + col)) + (row + 1));
                Cell cell = spreadsheet.getCell(loc);
                String cellText = (cell != null) ? cell.abbreviatedCellText() : "          ";
                Object valueAt = table.getValueAt(row, col);
                if (valueAt == null || !valueAt.equals(cellText)) {
                    table.setValueAt(cellText, row, col);
                }
            }
        }
        table.repaint(); // Refresh the UI
    }

    private void setupTableColumnModel() {
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    setHorizontalAlignment(SwingConstants.LEFT); // Align text left
                    return c;
                }
            });
        }
    }

    private void initializeTable() {
        table = new JTable(new DefaultTableModel(20, 12) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true; // Allowing cell edit
            }
        });
    
        table.setGridColor(Color.BLACK);
        table.setShowGrid(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        setupTableColumnModel();
    
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    }
}
