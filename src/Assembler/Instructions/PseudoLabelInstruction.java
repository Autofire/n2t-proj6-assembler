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

    }

    @Override
    public void apply(SymbolTable table, int instructionNumber) {
        throw new RuntimeException("Not ready");
    }
}
