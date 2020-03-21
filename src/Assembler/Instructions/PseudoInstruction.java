//  Author: Daniel Edwards
//   Class: CS 3650 (Section 1)
// Project: 6
//     Due: 3/23/2020


package Assembler.Instructions;

import Assembler.SymbolTable;

/**
 * Represents an instruction which cannot be converted to binary.
 * It is meant to be handled by the preprocessor and then discarded.
 */
public interface PseudoInstruction extends Instruction {

    /**
     * Apply the instruction's effect, whatever that may be.
     * @param table The current symbol table.
     * @param instructionNumber The number of instruction we're on.
     */
    public void apply(SymbolTable table, int instructionNumber);
}
