package symbol;

import symbol.throwable.NameConflictException;

import java.util.HashMap;

public class SymbolTable {
    public String scope;
    /*
    重构优化
     */
    public HashMap<String, Symbol> nodeSymbols;
    public HashMap<String, Symbol> tensorSymbols;
    public HashMap<String, Symbol> weightSymbols;
    public SymbolTable(String scope) {
        this.scope = scope;
        this.nodeSymbols = new HashMap<>();
        this.tensorSymbols = new HashMap<>();
        this.weightSymbols = new HashMap<>();
    }

    public HashMap<String, Symbol> getSubSymbolTable(String subScope) {
        switch (subScope){
            case "node":
                // Return node symbols
                return nodeSymbols;
            case "input":
                // Return tensor symbols
                return tensorSymbols;
            case "output":
                // Return tensor symbols
                return tensorSymbols;
            case "initializer":
                // Return weight symbols
                return weightSymbols;
            default:
                return null;
        }
    }

    public boolean hasSymbol(String symbolicName, String subScope) {
        HashMap<String, Symbol> symbols = getSubSymbolTable(subScope);
        return symbols != null && symbols.containsKey(symbolicName);
    }

    public void addSymbol(Symbol symbol, String subScope) throws NameConflictException {
        HashMap<String, Symbol> symbols = getSubSymbolTable(subScope);
        if (symbols.containsKey(symbol.symbolicName)) {
            throw new NameConflictException(symbols.get(symbol.symbolicName).location, symbol.location,symbol.symbolicName);
        }
        symbols.put(symbol.symbolicName, symbol);
    }

    public Symbol getSymbol(String symbolicName, String subScope) {
        HashMap<String, Symbol> symbols = getSubSymbolTable(subScope);
        return symbols.get(symbolicName);
    }

    public void setTempName(String symbolicName, String subScope, String temp) {
        Symbol symbol = getSymbol(symbolicName, subScope);
        if (symbol != null) {
            symbol.temp = temp;
        }
    }

    public String getTempName(String name, String scope) {
        Symbol symbol = getSymbol(name, scope);
        if (symbol != null) {
            return symbol.temp;
        } else {
            return null;
        }
    }
}
