package symbol;

public class Symbol {
    public String symbolicName;
    public String temp;
    public DataType dataType;
    public String location;

    public String subScope;

    public Symbol(String symbolicName, String dataType, String subScope, String location) {
        this.symbolicName = symbolicName;
        this.dataType = DataType.fromString(dataType);
        this.subScope = subScope;
        this.location = location;
    }

}
