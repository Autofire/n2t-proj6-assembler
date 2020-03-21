//  Author: Daniel Edwards
//   Class: CS 3650 (Section 1)
// Project: 6
//     Due: 3/23/2020


package Assembler;

import Assembler.Instructions.ConcreteInstruction;
import Assembler.Instructions.Instruction;
import Assembler.Instructions.InstructionParser;
import Assembler.Instructions.PseudoInstruction;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * This represents the program as a whole. You feed this lines from
 * the program, and then, once the entire program is read, it can
 * then write out the entire program via an OutputStream.
 */
public class Program {
    private CodeSegment code = new CodeSegment();
    private SymbolTable symbols = new SymbolTable();

    public Program() {}

    /**
     * Parses the given line. Note that this is not necessarily going
     * to modify the program's contents, depending on the line's
     * contents.
     * @param line The next line of the program.
     */
    public void receiveLine(String line) {
        Instruction instr = InstructionParser.parse(line);

        if(instr instanceof ConcreteInstruction) {
            code.add((ConcreteInstruction) instr);
        }
        else if(instr instanceof PseudoInstruction) {
            ((PseudoInstruction) instr).apply(symbols, code.nextInstructionNumber());
        }
    }

    /**
     * Converts the program to its equivalent, binary hack code.
     * This gets sent out the output stream. Call this after
     * feeding every line in.
     * @param stream Stream where the binary output is fed.
     */
    public void toBinary(PrintStream stream) {
        code.toBinary(stream, symbols);
    }
}
