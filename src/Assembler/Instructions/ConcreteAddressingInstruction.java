package Assembler.Instructions;

import Assembler.SymbolTable;

public class ConcreteAddressingInstruction implements ConcreteInstruction {

    String value;

    ConcreteAddressingInstruction(String line) {
        if(line.startsWith("@")) {
            value = line.substring(1);
        }
        else {
            throw new IllegalArgumentException("Not proper A-Instruction: " + line);
        }
    }

    @Override
    public String toBinary(SymbolTable table) {
        throw new RuntimeException("Not ready");
    }

    @Override
    public String toString() {
        return "@" + value;
    }
}
