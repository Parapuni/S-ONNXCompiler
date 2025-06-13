package symbol.throwable;

import symbol.DataType;

public class TypeUnmatchedException extends Exception {
    public String location;
    public String msg;

    public TypeUnmatchedException(String location, String msg) {
        super("Type mismatch " + msg + "at" + location);
        this.location = location;
        this.msg = msg;
    }
}
