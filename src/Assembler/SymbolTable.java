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
        else {
            symbols.put(labelName, instructionIndex);
        }
    }

    /**
     * Grabs the given symbol. If it does not already exist, a new
     * one is created at the next available address. If the symbol
     * is supposed to match a label instead of a variable, add it
     * with addLabel.
     * @param labelName Name of label to fetch/create.
     * @return The value associated with the label.
     */
    public int fetchSymbol(String labelName) {
        if(!symbols.containsKey(labelName)) {
            symbols.put(labelName, nextAvailableAddress);
            nextAvailableAddress++;
        }

        return symbols.get(labelName);
    }
}
