package Assembler.Instructions;

import Assembler.SymbolTable;

public class ConcreteComputeInstruction implements ConcreteInstruction {

    ConcreteComputeInstruction(String line) {
        // TODO
    }

    @Override
    public String toBinary(SymbolTable table) {
        throw new RuntimeException("Not ready");
    }
}
