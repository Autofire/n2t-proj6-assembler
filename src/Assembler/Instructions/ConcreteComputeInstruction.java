//  Author: Daniel Edwards
//   Class: CS 3650 (Section 1)
// Project: 6
//     Due: 3/23/2020


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

    private String bitSetToString(BitSet bits, final int bitCount) {
        StringBuilder s = new StringBuilder();
        for( int i = 0; i < bitCount;  i++ )
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
        // Using ALU codes here.
        // Note that D is fed into X and A/M is fed into Y
        final int BIT_COUNT = 7;
        final int  A = 0;
        final int ZX = 1;
        final int NX = 2;
        final int ZY = 3;
        final int NY = 4;
        final int  F = 5;
        final int NO = 6;

        BitSet bits = new BitSet(BIT_COUNT);

        // First, we'll just record whether what's being used.
        // This makes future checks simpler. Again, we're
        // referring to D and A/M by their ALU input names,
        // so X=D, and either Y=A or Y=M.
        boolean hasX = comp.contains("D");
        boolean hasY = comp.matches(".*[AM].*");

        // we'll handle the A bit; if we're using M in
        // the comp part, we definitely want this set.
        // The only other possibilities is that we are using
        // the A register (so then A bit is 0) or we are using
        // neither A nor M (and then it doesn't matter).
        if(comp.contains("M")) {
            bits.set(A);
        }

        // Alright, from here, we have 4 possibilities:
        //  1. Both X and Y
        //  2. Neither X nor Y
        //  3. Just X
        //  4. Just Y
        //
        // Cases 1 and 2 have a lot of unique possibilities
        // which we'll handle directly. Cases 3 and 4 have a
        // lot in common so they'll get handled together.
        if(hasX && hasY) {
            // Ok, so we're working with the format X?Y, where
            // ? is the operator.
            switch(comp.charAt(1)) {
                case '+':
                    bits.set(F);    // addition
                    break;

                case '-':
                    bits.set(F);    // addition
                    bits.set(NO);   // negate result
                    if(comp.matches("D-[AM]")) {
                        bits.set(NX);
                    }
                    else {
                        bits.set(NY);
                    }
                    break;

                case '&':
                    // Nothing to be done; all zeroes
                    break;

                case '|':
                    bits.set(NX);
                    bits.set(NY);
                    bits.set(NO);
                    break;
            }
        }
        else if(!hasX && !hasY) {
            bits.set(ZX);
            bits.set(ZY);
            bits.set(F);

            // We don't need a condition for 0 because no
            // extra bits need to be set for it.
            if(comp.equals("1")) {
                bits.set(NX);
                bits.set(NY);
                bits.set(NO);
            }
            else if(comp.equals("-1")) {
                bits.set(NX);
            }
        }
        else {
            // First, there's some general things we can deduce from
            // the comp string: If it is missing D or A/M, we can zero
            // and negate their inputs.
            bits.set(ZX, !hasX);
            bits.set(NX, !hasX);
            bits.set(ZY, !hasY);
            bits.set(NY, !hasY);

            if(comp.length() == 2) {
                switch(comp.charAt(0)) {
                    case '!':
                        bits.set(NO);
                        break;
                    case '-':
                        bits.set(F);
                        bits.set(NO);
                        break;
                }
            }
            else if(comp.length() == 3) {
                // In this branch, we're either incrementing
                // or decrementing something. Thus, we always add.
                bits.set(F);

                if(comp.endsWith("+1")) {
                    bits.set(NO);

                    // We're being a little redundant here,
                    // but in the case of a '+', both always
                    // get negated.
                    bits.set(NX);
                    bits.set(NY);
                }

                // Nothing needs to be done for -1
            }
        }

        return bitSetToString(bits, 7);
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
                bits.set(J_LT);
            }

            if(jump.matches("(JEQ|JGE|JLE|JMP)")) {
                bits.set(J_EQ);
            }
        }

        return bitSetToString(bits, BIT_COUNT);
    }

    @Override
    public String toBinary(SymbolTable table) {
        String prefix = "111";
        return String.join("", prefix, compBinary(), destBinary(), jumpBinary());
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

        return result;
    }
}
