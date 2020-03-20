package Assembler.Instructions;

import Assembler.SymbolTable;
import jdk.jshell.spi.ExecutionControl;

/**
 * A label. This isn't technically an instruction at all.
 */
public class PseudoLabelInstruction implements PseudoInstruction {

    private String label;

    /**
     * Constructs a label based on the given line of code.
     * @param line The entire line of code.
     */
    PseudoLabelInstruction(String line) {
        label = line.substring(1, line.length()-1);
    }

    @Override
    public void apply(SymbolTable table, int instructionNumber) {
        table.addLabel(label, instructionNumber);
    }
}
