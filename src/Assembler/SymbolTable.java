package Assembler;

import java.util.HashMap;
import java.util.Map;

/**
 * The table of symbols. This is in charge of assigning new symbols
 * their addresses in memory. It can also recall the addresses of
 * symbols which already exist.
 */
public class SymbolTable {

    private static final int MIN_LABEL_INDEX = 0;
    private static final int SCREEN_ADDRESS = 16384;
    private static final int KBD_ADDRESS = 24576;

    private Map<String, Integer> symbols = new HashMap<String, Integer>();
    private int nextAvailableAddress = 16; // We have R0 through R15 by default.

    public SymbolTable() {
        // Load up default symbol table
        symbols.put("SP", 0);
        symbols.put("LCL", 1);
        symbols.put("ARG", 2);
        symbols.put("THIS", 3);
        symbols.put("THAT", 4);

        // Do R0 through R15
        for(int i = 0; i <= 15; i++) {
            symbols.put("R" + Integer.toString(i), i);
        }

        symbols.put("SCREEN", SCREEN_ADDRESS);
        symbols.put("KBD", KBD_ADDRESS);
    }

    /**
     * Creates a new label which points at the given instruction.
     * @param labelName ASCII name of the label.
     * @param instructionIndex Instruction associated with label.
     * @throws NullPointerException if labelName is null/empty.
     * @throws IllegalArgumentException if instructionIndex is illegal.
     * @throws IllegalArgumentException if labelName is already in use.
     */
    public void addLabel(String labelName, int instructionIndex) {
        if(labelName == null || labelName.isBlank()) {
            throw new NullPointerException("Cannot have a null or empty label");
        }
        else if(instructionIndex < MIN_LABEL_INDEX) {
            throw new IllegalArgumentException("Instruction index out of range");
        }
        else if(symbols.containsKey(labelName)) {
            throw new IllegalArgumentException("Cannot override existing symbol");
        }
        else if(determineSymbolType(labelName) != SymbolType.Named) {
            throw new IllegalArgumentException("Labels must be a name");
        }
        else {
            System.out.println("Adding label: " + labelName + "(" + instructionIndex + ")");
            symbols.put(labelName, instructionIndex);
        }
    }

    /**
     * Grabs the given symbol. If it does not already exist, a new
     * one is created at the next available address. If the symbol
     * is supposed to match a label instead of a variable, add it
     * with addLabel.
     *
     * This will also correctly handle immediate values.
     * @param symbol Name of symbol to fetch/create.
     * @return The value associated with the label.
     */
    public int getValue(String symbol) {

        SymbolType type = determineSymbolType(symbol);

        if(type == SymbolType.Named) {
            if (!symbols.containsKey(symbol)) {
                symbols.put(symbol, nextAvailableAddress);
                nextAvailableAddress++;
            }

            return symbols.get(symbol);
        }
        else if(type == SymbolType.Literal) {
            return Integer.parseInt(symbol);
        }
        else {
            throw new IllegalArgumentException("Invalid symbol: " + symbol);
        }
    }

    private enum SymbolType {
        Named, Literal, Invalid
    }

    private static SymbolType determineSymbolType(String label) {
        SymbolType type = SymbolType.Invalid;

        if(label != null) {
            if (label.matches("^-?[0-9]+$")) {
                type = SymbolType.Literal;
            }
            else if(label.matches("^[a-zA-Z][0-9a-zA-Z_]*$")) {
                type = SymbolType.Named;
            }
        }

        return type;
    }

}
