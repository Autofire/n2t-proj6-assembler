import Assembler.Program;

import java.io.*;

public class Main {

    public static void main(String[] args) {

        // Alright, so there are two parts to a program:
        //  1. The instructions. These can be labelled and referred to
        //  2. The variables. These their own space in memory
        //
        // We can broadly handle all of this in two passes:
        //  1. First, we have our preprocessor. This'll run through,
        //     scanning each line of the file. It'll strip comments
        //     and also track labels. The rest of the lines get
        //     caught as-is, and are held for the next step.
        //
        //  2. Next, our parser takes over. This time, we know
        //     all labels for code, so we can now assume that any
        //     label which doesn't match a line of code must be a
        //     variable. We can simply run through it as we go.
        //
        // Once we've done our first pass, we'll track each statement.
        // Unlike the specification in the book, we aren't going to
        // track these arbitrary "L_COMMANDS," as those are used just
        // for labels. These are handled by the preprocessor, so
        // the parser never sees them.
        //
        // We have a shared symbol table between both steps. If we
        // try to fetch a missing symbol, it can grow automatically.

        if(args.length == 1) {
            String inputFileName = args[0];
            File file = new File(inputFileName);

            Program thisProgram = new Program();

            try(BufferedReader br = new BufferedReader(new FileReader(file))) {

                System.out.println("Running preprocessor...");
                String line = br.readLine();
                while(line != null) {
                    //InstructionParser.parse(line);
                    thisProgram.receiveLine(line);

                    line = br.readLine();
                }

                System.out.println();
                System.out.println("Running parser...");
                String outputFileName = inputFileName.replaceFirst("\\.asm$", ".hack");
                try(PrintStream writer = new PrintStream(outputFileName)) {
                    thisProgram.toBinary(writer);
                }
            }
            catch (FileNotFoundException e) {
                //e.printStackTrace();
                System.out.println("Couldn't open for reading: " + inputFileName);
            }
            catch (IOException e) {
                System.out.println("Failed to read file");
            }

        }
        else {
            System.out.println("Usage: Main input.asm [output.hack]");
        }
        //File file = new File(args[1]);
        /*
        InstructionParser.parse("");
        InstructionParser.parse("// This file is part of www.nand2tetris.org");
        InstructionParser.parse("// and the book The Elements of Computing Systems");
        InstructionParser.parse("// by Nisan and Schocken, MIT Press.");
        InstructionParser.parse("// File name: projects/06/max/Max.asm");
        InstructionParser.parse("");
        InstructionParser.parse("// Computes R2 = max(R0, R1)  (R0,R1,R2 refer to RAM[0],RAM[1],RAM[2])");
        InstructionParser.parse("");
        InstructionParser.parse("@R0");
        InstructionParser.parse("D=M              // D = first number");
        InstructionParser.parse("@R1");
        InstructionParser.parse("D=D-M            // D = first number - second number");
        InstructionParser.parse("@OUTPUT_FIRST");
        InstructionParser.parse("D;JGT            // if D>0 (first is greater) goto output_first");
        InstructionParser.parse("@R1");
        InstructionParser.parse("D=M              // D = second number");
        InstructionParser.parse("@OUTPUT_D");
        InstructionParser.parse("0;JMP            // goto output_d");
        InstructionParser.parse("(OUTPUT_FIRST)");
        InstructionParser.parse("@R0");
        InstructionParser.parse("D=M              // D = first number");
        InstructionParser.parse("(OUTPUT_D)");
        InstructionParser.parse("@R2");
        InstructionParser.parse("M=D              // M[2] = D (greatest number)");
        InstructionParser.parse("(INFINITE_LOOP)");
        InstructionParser.parse("@INFINITE_LOOP");
        InstructionParser.parse("0;JMP            // infinite loop");
         */
    }
}
