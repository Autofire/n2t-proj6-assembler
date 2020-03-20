package Assembler.Instructions;

/**
 * Spews out instructions based on strings.
 */
public final class InstructionParser {
    private InstructionParser() {}

    /**
     * Takes the given line and converts it into an equivalent
     * instruction object. If the line doesn't is entirely blank
     * or comment, then null is returned.
     * @param line Line of code to parse.
     * @return The equivalent instruction object or null.
     */
    public static Instruction parse(String line) {

        // We could say "This will be null by default" or something,
        // but we want to make sure that this gets defined by some
        // string of logic or an exception gets thrown.
        Instruction result;

        // Leading and trailing whitespace is always ignored.
        // Also remove all comments.
        line = line.strip().split("//")[0];

        if(line.isEmpty()) {
            //System.out.println("Blank: " + line);
            result = null;
        }
        else if(line.startsWith("(") && line.endsWith(")")) {
            //System.out.println("Label:  " + line);
            result = new PseudoLabelInstruction(line);
        }
        else if(line.startsWith("@")) {
            //System.out.println("A type: " + line);
            result = new ConcreteAddressingInstruction(line);
        }
        else {
            //System.out.println("C type: " + line);
            result = new ConcreteComputeInstruction(line);
        }

        return result;
    }

}
