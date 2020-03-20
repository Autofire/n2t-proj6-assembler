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

        // Note that this count is NOT counting the
        // leading 0 at the start of the A-type instructions.
        final int BYTE_COUNT = 15;

        // Note that this converts the number into a 32bit binary
        // string. This is NOT what we want; we only support up to
        // 15 bits. Thus, in some cases, we may have more bits than
        // we can store. In other cases, we're missing zeroes.
        String rawBinary = Integer.toBinaryString(table.getValue(value));

        StringBuilder s = new StringBuilder();

        if(rawBinary.length() > BYTE_COUNT) {
            int endIndex = rawBinary.length();
            s.append(rawBinary.substring(endIndex-BYTE_COUNT, endIndex));
        }
        else {
            while (s.length() < BYTE_COUNT - rawBinary.length()) {
                s.append(0);
            }

            s.append(rawBinary);
        }

        // All A-type instructions start with a zero.
        s.insert(0, '0');
        return s.toString();
    }

    @Override
    public String toString() {
        return "@" + value;
    }
}
