package Assembler.Instructions;

import Assembler.SymbolTable;

import java.util.BitSet;

public class ConcreteComputeInstruction implements ConcreteInstruction {

    public String comp;
    public String dest;
    public String jump;

    ConcreteComputeInstruction(String line) {

        // First a general check; the logic after this check
        // may fail if this regex fails, so we want to just
        // make sure first. We'll avoid weird errors on bad
        // syntax this way.
        if(!line.matches("^([AMD]{1,3}\\=)?.*(;[JGTEQLNMP]{3})?$")) {
            throw new IllegalArgumentException("Syntax error: " + line);
        }

        // If there's an assignment going on, we should see
        // an '=' preceded by the destination. Then we want
        // to discard the assignment portion of the command.
        if(line.contains("=")) {
            String[] parts = line.split("=");

            dest = parts[0].strip();
            line = parts[1];
        }
        else {
            dest = null;
        }

        // Like with dest, we'll grab the jump part of the
        // command and then discard it, if it exists.
        if(line.contains(";")) {
            String[] parts = line.split(";");

            jump = parts[1].strip();
            line = parts[0];
        }
        else {
            jump = null;
        }

        // With the rest of the processing done, whatever
        // is left will be the comp portion. All instructions
        // have a comp portion, so we need no checks.
        comp = line.strip();
    }

    private String bitSetToString(BitSet bits, final int BIT_COUNT) {
        StringBuilder s = new StringBuilder();
        for( int i = 0; i < BIT_COUNT;  i++ )
        {
            if(i < bits.length()) {
                s.append(bits.get(i) == true ? 1 : 0);
            }
            else {
                s.append(0);
            }
        }

        return s.toString();
    }

    private String compBinary() {
        return "0000000";
    }

    private String destBinary() {
        final int BIT_COUNT = 3;
        final int A_INDEX = 0;
        final int D_INDEX = 1;
        final int M_INDEX = 2;

        BitSet bits = new BitSet(BIT_COUNT);

        if(dest != null) {
            if (dest.contains("A")) {
                bits.set(A_INDEX);
            }

            if (dest.contains("D")) {
                bits.set(D_INDEX);
            }

            if (dest.contains("M")) {
                bits.set(M_INDEX);
            }
        }

        return bitSetToString(bits, BIT_COUNT);
    }

    private String jumpBinary() {
        final int BIT_COUNT = 3;
        final int J_LT = 0;
        final int J_EQ = 1;
        final int J_GT = 2;

        BitSet bits = new BitSet(BIT_COUNT);

        if(jump != null) {
            if(jump.matches("(JGT|JGE|JNE|JMP)")) {
                bits.set(J_GT);
            }

            if(jump.matches("(JLT|JNE|JLE|JMP)")) {
                bits.set(J_EQ);
            }

            if(jump.matches("(JEQ|JGE|JLE|JMP)")) {
                bits.set(J_LT);
            }
        }

        return bitSetToString(bits, BIT_COUNT);
    }

    @Override
    public String toBinary(SymbolTable table) {
        String prefix = "111";
        return prefix + " " + compBinary() + " " + destBinary() + " " + jumpBinary();
    }

    @Override
    public String toString() {
        String result = comp;

        if(dest != null) {
            result = dest + "=" + result;
        }

        if(jump != null) {
            result = result + ";" + jump;
        }

        return result + "     " + toBinary(null);
    }
}
