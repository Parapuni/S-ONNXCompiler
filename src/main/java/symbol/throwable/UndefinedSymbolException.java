package symbol.throwable;

public class UndefinedSymbolException extends Exception {
    public String location;
    public String symbolicName;
    public UndefinedSymbolException(String symbolicName, String location) {
        super("Undefined quote: " + symbolicName + " at " + location);
        this.location = location;
        this.symbolicName = symbolicName;
    }
}
