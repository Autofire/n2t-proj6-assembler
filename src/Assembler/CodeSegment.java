//  Author: Daniel Edwards
//   Class: CS 3650 (Section 1)
// Project: 6
//     Due: 3/23/2020


package Assembler;

import Assembler.Instructions.ConcreteInstruction;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Vector;

/**
 * Represents all code in the program which will eventually end up
 * in the final hack binary.
 */
public class CodeSegment {
    private List<ConcreteInstruction> instructions = new Vector<ConcreteInstruction>();

    public CodeSegment() { }

    /**
     * Adds another instruction to the code segment so that it can
     * eventually be converted into binary.
     * @param newInstruction Instruction to add.
     * @throws NullPointerException if newInstruction is null.
     */
    public void add(ConcreteInstruction newInstruction) {
        if(newInstruction == null) {
            throw new NullPointerException("Code segment cannot take null instructions");
        }
        else {
            System.out.println("Adding code:  " + newInstruction.toString());
            instructions.add(newInstruction);
        }
    }

    /**
     * If another instruction is added, it will get the index returned.
     * Intended to be used when labels are found.
     * @return Index a new instruction would get.
     */
    public int nextInstructionNumber() {
        return instructions.size();
    }

    public void toBinary(PrintStream stream, SymbolTable symbols) {
        for(ConcreteInstruction i : instructions) {
            System.out.println(i.toBinary(symbols) + "  " + i.toString());
            stream.println(i.toBinary(symbols));
        }
    }
}
