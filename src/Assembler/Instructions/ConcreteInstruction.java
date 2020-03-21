//  Author: Daniel Edwards
//   Class: CS 3650 (Section 1)
// Project: 6
//     Due: 3/23/2020


package Assembler.Instructions;

import Assembler.SymbolTable;

/**
 * Represents a single instruction in the program which can be
 * converted into an assembly instruction. Can be turned into a
 * binary string representing the equivalent instruction in hack.
 */
public interface ConcreteInstruction extends Instruction {

    /**
     * Converts the instruction into a binary string.
     * @param table Current table of symbols. This may get updated.
     * @return A 16bit binary string.
     */
    public String toBinary(SymbolTable table);

}
