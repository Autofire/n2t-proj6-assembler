package Assembler;

import java.util.HashMap;
import java.util.Map;

/**
 * The table of symbols. This is in charge of assigning new symbols
 * their addresses in memory. It can also recall the addresses of
 * symbols which already exist.
 */
public class SymbolTable {
    private Map<String, Integer> symbols = new HashMap<String, Integer>();

    public SymbolTable() {}

}
