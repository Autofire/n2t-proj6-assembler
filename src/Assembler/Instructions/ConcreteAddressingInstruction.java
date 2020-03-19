package Assembler.Instructions;

import Assembler.SymbolTable;

public class ConcreteAddressingInstruction implements ConcreteInstruction {

    ConcreteAddressingInstruction(String line) {
        // TODO
    }

    @Override
    public String toBinary(SymbolTable table) {
        throw new RuntimeException("Not ready");
    }
}
