package symbol;

public enum DataType {
    OPERATOR,
    FLOAT,
    INT,
    STRING,
    BOOL;
    public static DataType fromString(String type) {
        switch (type.toUpperCase()) {
            case "FLOAT":
                return FLOAT;
            case "INT":
                return INT;
            case "STRING":
                return STRING;
            case "BOOL":
                return BOOL;
            default:
                return OPERATOR;
        }
    }
}
